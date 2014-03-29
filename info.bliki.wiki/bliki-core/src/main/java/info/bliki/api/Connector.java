package info.bliki.api;

import info.bliki.api.query.Edit;
import info.bliki.api.query.Query;
import info.bliki.api.query.RequestBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.xml.sax.SAXException;

/**
 * Manages the queries for the <a
 * href="http://meta.wikimedia.org/w/api.php">Wikimedia API</a>. See the <a
 * href="http://www.mediawiki.org/wiki/API:Main_page">API Documentation</a>.
 * 
 * The queries set their own user-agent string. See <a
 * href="http://meta.wikimedia.org/wiki/User-Agent_policy">User-Agent policy</a>
 */
public class Connector {
	final static public String USER_AGENT = "JavaWikipediaAPI/3.0 http://code.google.com/p/gwtwiki/";

	final static public String UTF8_CHARSET = "utf-8";

	public final static String PARAM_LOGIN_USERNAME = "lgusername";
	public final static String PARAM_LOGIN_USERID = "lguserid";
	public final static String PARAM_LOGIN_TOKEN = "lgtoken";
	public final static String PARAM_FORMAT = "format";
	public final static String PARAM_ACTION = "action";
	public final static String PARAM_TITLES = "titles";
	public final static String PARAM_PAGE = "page";

	// create a ConnectionManager
	private MultiThreadedHttpConnectionManager manager;

	private HttpClient client;

	/**
	 * Format the response body as XML String. Especially for some obscure <a
	 * href="http://en.wikipedia.org/wiki/Byte_order_mark ">byte order mark</a>
	 * cases. See <a
	 * href="http://code.google.com/p/gwtwiki/issues/detail?id=33">Issue #33</a>
	 * 
	 * @param method
	 * @return XML string
	 * @throws IOException
	 */
	public static String getAsXmlString(HttpMethod method) throws IOException {
		Header contentEncoding = method.getResponseHeader("Content-Encoding");
		InputStream instream = method.getResponseBodyAsStream();
		if (contentEncoding != null && contentEncoding.getValue().equalsIgnoreCase("gzip")) {
			instream = new GZIPInputStream(instream);
		}
		String responseBody = convertStreamToString(instream);
		if (responseBody.length() > 0 && responseBody.charAt(0) != '<') {
			// try to find XML.
			int indx = responseBody.indexOf("<?xml");
			if (indx > 0) {
				responseBody = responseBody.substring(indx);
			}
		}
		return responseBody;
	}

	private static String convertStreamToString(InputStream is) throws IOException {
		/*
		 * To convert the InputStream to String we use the BufferedReader.readLine()
		 * method. We iterate until the BufferedReader return null which means
		 * there's no more data to read. Each line will appended to a StringBuilder
		 * and returned as String.
		 */
		if (is != null) {
			StringBuilder sb = new StringBuilder();
			String line;

			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				while ((line = reader.readLine()) != null) {
					sb.append(line).append("\n");
				}
			} finally {
				is.close();
			}
			return sb.toString();
		}
		return "";
	}

	private String executeHttpMethod(HttpMethod method) {
		try {
			method.addRequestHeader("Accept-encoding", "gzip");
			int responseCode = client.executeMethod(method);
			if (responseCode == HttpStatus.SC_OK) {
				return getAsXmlString(method);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			method.releaseConnection();
		}
		return null;
	}

	public Connector() {
		manager = new MultiThreadedHttpConnectionManager();
		// manager.setMaxConnectionsPerHost(6);
		// manager.setMaxTotalConnections(18);
		// manager.setConnectionStaleCheckingEnabled(true);
		// open the conversation
		client = new HttpClient(manager);
		// setHTTPClientParameters(client);
	}

	/**
	 * Complete the users login information. The user must contain a username,
	 * password and actionURL. See <a
	 * href="http://www.mediawiki.org/wiki/API:Login">Mediawiki API:Login</a>
	 * 
	 * @param user
	 *          a user account from a Mediawiki installation with filled out user
	 *          name, password and the installations API url.
	 * @return the completed user information or <code>null</code>, if the login
	 *         fails
	 */
	public User login(User user) {
		// The first pass gets the secret token and the second logs the user in
		for (int i = 0; i < 2; i++) {
			PostMethod method = new PostMethod(user.getActionUrl());
			String userName = user.getUsername();

			if (userName == null || userName.trim().length() == 0) {
				// no nothing for dummy users
				return user;
			}

			method.setFollowRedirects(false);
			method.addRequestHeader("User-Agent", USER_AGENT);
			String lgDomain = user.getDomain();
			List<NameValuePair> params = new ArrayList<NameValuePair>();

			method.addParameter("action", "login");
			method.addParameter("format", "xml");
			method.addParameter("lgname", userName);
			method.addParameter("lgpassword", user.getPassword());
			if (lgDomain.length() > 0) {
				method.addParameter("lgdomain", user.getDomain());
			}
			if (i > 0) {
				method.addParameter("lgtoken", user.getToken());
			}

			try {
				int responseCode = client.executeMethod(method);
				if (responseCode == HttpStatus.SC_OK) {
					String responseBody = getAsXmlString(method);
					XMLUserParser parser = new XMLUserParser(user, responseBody);
					parser.parse();
					if (i == 0 && user.getResult().equals(User.NEED_TOKEN_ID)) {
						// try again
					} else if (user.getResult().equals(User.SUCCESS_ID)) {
						return user;
					} else {
						break;
					}
				}
			} catch (HttpException e) {
				e.printStackTrace();
				return null;
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			} catch (SAXException e) {
				e.printStackTrace();
				return null;
			} finally {
				method.releaseConnection();
			}
		}
		// What's the correct way to log a message here?

		return null;
	}

	/**
	 * Get the HttpClient.
	 * 
	 * @return http client
	 */
	public HttpClient getClient() {
		return client;
	}

	/**
	 * Get the HttpConnection manager.
	 * 
	 * @return http connection manager
	 */
	public MultiThreadedHttpConnectionManager getManager() {
		return manager;
	}

	/**
	 * Get the content of Mediawiki wiki pages.
	 * 
	 * @param user
	 *          user login data
	 * @param listOfTitleStrings
	 *          a list of title Strings "ArticleA,ArticleB,..."
	 * @return a list of downloaded Mediawiki pages.
	 */
	public List<Page> queryContent(User user, List<String> listOfTitleStrings) {
		String[] valuePairs = { "prop", "revisions", "rvprop", "timestamp|user|comment|content" };
		return query(user, listOfTitleStrings, valuePairs);
	}

	/**
	 * List all categories the page(s) belong to.
	 * 
	 * @param user
	 *          user login data
	 * @param listOfTitleStrings
	 *          a list of title Strings "ArticleA,ArticleB,..."
	 * @return page list
	 */
	public List<Page> queryCategories(User user, List<String> listOfTitleStrings) {
		String[] valuePairs = { "prop", "categories" };
		return query(user, listOfTitleStrings, valuePairs);
	}

	/**
	 * Get basic page information such as namespace, title, last touched date, ..
	 * 
	 * @param user
	 *          user login data
	 * @param listOfTitleStrings
	 *          a list of title Strings "ArticleA,ArticleB,..."
	 * @return page list
	 */
	public List<Page> queryInfo(User user, List<String> listOfTitleStrings) {
		String[] valuePairs = { "prop", "info" };
		return query(user, listOfTitleStrings, valuePairs);
	}

	/**
	 * Returns all links from the given page(s).
	 * 
	 * @param user
	 *          user login data
	 * @param listOfTitleStrings
	 *          a list of title Strings "ArticleA,ArticleB,..."
	 * @return page list
	 */
	public List<Page> queryLinks(User user, List<String> listOfTitleStrings) {
		String[] valuePairs = { "prop", "links" };
		return query(user, listOfTitleStrings, valuePairs);
	}

	/**
	 * Returns image information and upload history. Only a URL to an unscaled
	 * image will be returned in the page data. Use
	 * {@link #queryImageinfo(User, List, int)} if you need additional information
	 * about the URL of the scaled image.
	 * 
	 * @param user
	 *          user login data
	 * @param listOfImageStrings
	 *          a list of title Strings "ArticleA,ArticleB,..."
	 * @return page list
	 */
	public List<Page> queryImageinfo(User user, List<String> listOfImageStrings) {
		String[] valuePairs = { "prop", "imageinfo", "iiprop", "url" };
		return query(user, listOfImageStrings, valuePairs);
	}

	/**
	 * Returns image information and upload history
	 * 
	 * @param user
	 *          user login data
	 * @param listOfImageStrings
	 *          list of image title strings
	 * @param imageWidth
	 *          a URL to an image scaled to this width will be returned. Only the
	 *          current version of the image can be scaled.
	 * @return page list
	 */
	public List<Page> queryImageinfo(User user, List<String> listOfImageStrings, int imageWidth) {
		String[] valuePairs = { "prop", "imageinfo", "iiprop", "url", "iiurlwidth", Integer.toString(imageWidth) };
		return query(user, listOfImageStrings, valuePairs);
	}

	/**
	 * Returns page info with edit token which is required for the edit action.
	 * 
	 * @param user
	 *          user login data
	 * @param title
	 *          title of the page
	 * @return
	 */
	private List<Page> queryInfoWithEditToken(User user, String title) {
		Query query = Query.create().prop("info", "revisions").titles(title).intoken("edit");
		return query(user, query);
	}

	/**
	 * Query the Mediawiki API for some wiki pages.
	 * 
	 * @param user
	 *          user login data
	 * @param query
	 *          a user defined query
	 * @return page list
	 */
	public List<Page> query(User user, Query query) {
		String response = sendXML(user, query);
		try {
			XMLPagesParser xmlPagesParser = new XMLPagesParser(response);
			xmlPagesParser.parse();
			return xmlPagesParser.getPagesList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Query the Mediawiki API for some wiki pages.
	 * 
	 * @param user
	 *          user login data
	 * @param listOfTitleStrings
	 *          a list of title Strings "ArticleA,ArticleB,..."
	 * @param valuePairs
	 *          pairs of query strings which should be appended to the Mediawiki
	 *          API URL
	 * @return page list
	 */
	public List<Page> query(User user, List<String> listOfTitleStrings, String[] valuePairs) {
		try {
			String responseBody = queryXML(user, listOfTitleStrings, valuePairs);
			if (responseBody != null) {
				// System.out.println(responseBody);
				XMLPagesParser parser = new XMLPagesParser(responseBody);
				parser.parse();
				return parser.getPagesList();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
		// no pages parsed!?
		return new ArrayList<Page>();
	}

	/**
	 * Get the raw XML result from the Mediawiki API
	 * 
	 * @param user
	 *          user login data
	 * @param valuePairs
	 *          pairs of query strings which should be appended to the Mediawiki
	 *          API URL
	 * @return the raw XML string produced by the query; <code>null</code>
	 *         otherwise
	 */
	public String queryXML(User user, String[] valuePairs) {
		return queryXML(user, new ArrayList<String>(), valuePairs);
	}

	/**
	 * Get the raw XML result from the Mediawiki API
	 * 
	 * @param user
	 *          user login data
	 * @param listOfTitleStrings
	 *          a list of possibly empty title Strings "ArticleA,ArticleB,..."
	 * @param valuePairs
	 *          pairs of query strings which should be appended to the Mediawiki
	 *          API URL
	 * @return the raw XML string produced by the query; <code>null</code>
	 *         otherwise
	 */
	public String queryXML(User user, List<String> listOfTitleStrings, String[] valuePairs) {
		PostMethod method = createAuthenticatedPostMethod(user);

		StringBuffer titlesString = new StringBuffer();
		for (int i = 0; i < listOfTitleStrings.size(); i++) {
			titlesString.append(listOfTitleStrings.get(i));
			if (i < listOfTitleStrings.size() - 1) {
				titlesString.append("|");
			}
		}

		method.addParameter(new NameValuePair(PARAM_ACTION, "query"));
		if (titlesString.length() > 0) {
			// don't encode the title for the NameValuePair !
			method.addParameter(new NameValuePair(PARAM_TITLES, titlesString.toString()));
		}
		if (valuePairs != null && valuePairs.length > 0) {
			for (int i = 0; i < valuePairs.length; i += 2) {
				method.addParameter(new NameValuePair(valuePairs[i], valuePairs[i + 1]));
			}
		}
		return executeHttpMethod(method);
	}

	public ParseData parse(User user, RequestBuilder requestBuilder) {
		String xmlResponse = sendXML(user, requestBuilder);
		if (xmlResponse != null) {
			try {
				XMLParseParser xmlParseParser = new XMLParseParser(xmlResponse);
				xmlParseParser.parse();
				return xmlParseParser.getParse();
			} catch (SAXException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public void edit(User user, Edit editQuery) throws UnexpectedAnswerException {
		// get edit token
		String title = editQuery.get("title");
		if (title != null) {
			List<Page> pages = queryInfoWithEditToken(user, title);
			if (pages != null && pages.size() == 1) {
				Page page = pages.get(0);
				if (page.getEditToken() != null) {
					// ok, edit token was received
					editQuery.token(page.getEditToken());
					String response = sendXML(user, editQuery);
					try {
						if (response != null) {
							XMLEditParser editParser = new XMLEditParser(response);
							editParser.parse();
							ErrorData errorData = editParser.getErrorData();
							if (errorData != null) {
								// if there is error data
								UnexpectedAnswerException ex = new UnexpectedAnswerException(errorData.getInfo());
								ex.setErrorData(errorData);
								throw ex;
							}
						}
					} catch (SAXException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
					throw new UnexpectedAnswerException("Edit token was not obtained");
				}
			} else {
				throw new UnexpectedAnswerException("The specified page was not found");
			}
		}
	}

	/**
	 * Sends request to get the raw format from the Wikipedia API.
	 * 
	 * @param user
	 *          user login data
	 * @param requestBuilder
	 *          additional parameters
	 * @return the raw XML string produced by the query; <code>null</code>
	 *         otherwise
	 */
	public String sendXML(User user, RequestBuilder requestBuilder) {
		PostMethod method = createAuthenticatedPostMethod(user);
		method.addParameters(requestBuilder.getParameters());
		return executeHttpMethod(method);
	}

	private PostMethod createAuthenticatedPostMethod(User user) {
		PostMethod method = new PostMethod(user.getActionUrl());

		method.setFollowRedirects(false);

		method.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
		method.setRequestHeader("User-Agent", USER_AGENT);

		method.addParameter(new NameValuePair(PARAM_LOGIN_USERNAME, user.getUserid()));
		method.addParameter(new NameValuePair(PARAM_LOGIN_USERID, user.getNormalizedUsername()));
		method.addParameter(new NameValuePair(PARAM_LOGIN_TOKEN, user.getToken()));
		method.addParameter(new NameValuePair(PARAM_FORMAT, "xml"));
		return method;
	}

	// TODO: this doesn't work at the moment:
	// public boolean submit(User user, String actionUrl, String title, String
	// uploadContent, String summary, String timestamp,
	// boolean minorEdit, boolean watchThis) {
	//
	// PostMethod method = new PostMethod(actionUrl);
	//
	// method.setFollowRedirects(false);
	// method.addRequestHeader("User-Agent", USER_AGENT);
	// method.addRequestHeader("Content-Type",
	// PostMethod.FORM_URL_ENCODED_CONTENT_TYPE + "; charset=" + UTF8_CHARSET);
	// try {
	//
	// NameValuePair[] params = new NameValuePair[] { new NameValuePair("title",
	// title),
	// new NameValuePair("wpTextbox1", uploadContent), new
	// NameValuePair("wpEdittime", timestamp),
	// new NameValuePair("wpSummary", summary), new NameValuePair("wpEditToken",
	// user.getToken()),
	// new NameValuePair("wpSave", "yes"), new NameValuePair("action", "submit")
	// };
	// method.addParameters(params);
	// if (minorEdit)
	// method.addParameter("wpMinoredit", "1");
	// if (watchThis)
	// method.addParameter("wpWatchthis", "1");
	//
	// int responseCode = client.executeMethod(method);
	// String responseBody = method.getResponseBodyAsString();
	// // log(method);
	//
	// // since 11dec04 there is a single linefeed instead of an empty
	// // page.. trim() helps.
	// if (responseCode == 302 && responseBody.trim().length() == 0) {
	// // log("store successful, reloading");
	// // Loaded loaded = load(actionUrl, config.getUploadCharSet(),
	// // title);
	// // result = new Stored(actionUrl, config.getUploadCharSet(),
	// // loaded.title, loaded.content, false);
	// return true;
	// } else if (responseCode == 200) {
	// // // log("store not successful, conflict detected");
	// // Parsed parsed = parseBody(config.getUploadCharSet(),
	// // responseBody);
	// // Content cont = new Content(parsed.timestamp, parsed.body);
	// // result = new Stored(actionUrl, config.getUploadCharSet(),
	// // parsed.title, cont, true);
	// // } else {
	// // throw new UnexpectedAnswerException(
	// // "store not successful: expected 200 OK, got "
	// // + method.getStatusLine());
	// }
	// } catch (UnsupportedEncodingException e) {
	// e.printStackTrace();
	// } catch (HttpException e) {
	// e.printStackTrace();
	// } catch (IOException e) {
	// e.printStackTrace();
	// } finally {
	// method.releaseConnection();
	// }
	// return false;
	// }

}
