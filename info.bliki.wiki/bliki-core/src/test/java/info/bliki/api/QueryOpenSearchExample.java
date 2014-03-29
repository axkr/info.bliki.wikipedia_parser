package info.bliki.api;

import info.bliki.api.query.OpenSearch;
import info.bliki.api.query.RequestBuilder;

/**
 * Example for querying the open search interface
 */
public class QueryOpenSearchExample {
	public static void main(String[] args) {
		User user = new User("", "", "http://en.wikipedia.org/w/api.php");
		Connector connector = new Connector();
		user = connector.login(user);

		RequestBuilder request = OpenSearch.create().search("test").format("json");
		String jsonResponse = connector.sendXML(user, request);
		System.out.println(jsonResponse);
	}

}
