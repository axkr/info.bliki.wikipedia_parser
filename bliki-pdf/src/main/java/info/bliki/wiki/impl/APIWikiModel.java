package info.bliki.wiki.impl;

import info.bliki.api.Page;
import info.bliki.api.User;
import info.bliki.api.creator.ImageData;
import info.bliki.api.creator.TopicData;
import info.bliki.api.creator.WikiDB;
import info.bliki.htmlcleaner.TagNode;
import info.bliki.wiki.filter.Encoder;
import info.bliki.wiki.filter.ParsedPageName;
import info.bliki.wiki.filter.WikipediaParser;
import info.bliki.wiki.model.Configuration;
import info.bliki.wiki.model.ImageFormat;
import info.bliki.wiki.model.WikiModel;
import info.bliki.wiki.model.WikiModelContentException;
import info.bliki.wiki.namespaces.INamespace.NamespaceCode;

import javax.annotation.Nullable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Wiki model implementation which uses the <code>info.bliki.api</code> package
 * for downloading templates and images from a defined wiki.
 *
 */
public class APIWikiModel extends WikiModel {
    private WikiDB fWikiDB;

    private final File fImageDirectory;
    static {
        TagNode.addAllowedAttribute("style");
    }

    private final User fUser;

    /**
     * WikiModel which loads the templates and images through the <a
     * href="http://meta.wikimedia.org/w/api.php">Wikimedia API</a>
     *
     * @param user
     *          a user for the <a
     *          href="http://meta.wikimedia.org/w/api.php">Wikimedia API</a>
     * @param wikiDB
     *          a wiki database to retrieve already downloaded topics and
     *          templates
     * @param imageBaseURL
     *          a url string which must contains a &quot;${image}&quot; variable
     *          which will be replaced by the image name, to create links to
     *          images.
     * @param linkBaseURL
     *          a url string which must contains a &quot;${title}&quot; variable
     *          which will be replaced by the topic title, to create links to
     *          other wiki topics.
     * @param imageDirectory
     *          a directory for storing downloaded Wikipedia images. The directory
     *          must already exist.
     */
    public APIWikiModel(User user, WikiDB wikiDB, String imageBaseURL, String linkBaseURL, File imageDirectory) {
        this(user, wikiDB, Locale.ENGLISH, imageBaseURL, linkBaseURL, imageDirectory);
    }

    /**
     * WikiModel which loads the templates and images through the <a
     * href="http://meta.wikimedia.org/w/api.php">Wikimedia API</a>
     *
     * @param user
     *          a user for the <a
     *          href="http://meta.wikimedia.org/w/api.php">Wikimedia API</a>
     * @param wikiDB
     *          a wiki database to retrieve already downloaded topics and
     *          templates
     * @param locale
     *          a locale for loading language specific resources
     * @param imageBaseURL
     *          a url string which must contains a &quot;${image}&quot; variable
     *          which will be replaced by the image name, to create links to
     *          images.
     * @param linkBaseURL
     *          a url string which must contains a &quot;${title}&quot; variable
     *          which will be replaced by the topic title, to create links to
     *          other wiki topics.
     * @param imageDirectory
     *          a directory for storing downloaded Wikipedia images. The directory
     *          must already exist.
     */
    public APIWikiModel(User user, WikiDB wikiDB, Locale locale, String imageBaseURL, String linkBaseURL, File imageDirectory) {
        super(Configuration.DEFAULT_CONFIGURATION, locale, imageBaseURL, linkBaseURL);

        fUser = user;
        fWikiDB = wikiDB;
        if (imageDirectory != null) {
            if (!imageDirectory.exists()) {
                assert(imageDirectory.mkdir());
            }
        }
        fImageDirectory = imageDirectory;
    }

    /**
     * Get the raw wiki text for the given namespace and article name. This model
     * implementation uses a Derby database to cache downloaded wiki template
     * texts.
     *
     * @param parsedPagename
     *          the parsed template name
     * @param templateParameters
     *          if the namespace is the <b>Template</b> namespace, the current
     *          template parameters are stored as <code>String</code>s in this map
     *
     * @return <code>null</code> if no content was found
     *
     * @see info.bliki.api.User#queryContent(String[])
     */
    @Override @Nullable
    public String getRawWikiContent(final ParsedPageName parsedPagename, final Map<String, String> templateParameters)
            throws WikiModelContentException {
        String result = super.getRawWikiContent(parsedPagename, templateParameters);
        if (result != null) {
            // found magic word template
            return result;
        }

        final boolean isTemplate = parsedPagename.namespace.isType(NamespaceCode.TEMPLATE_NAMESPACE_KEY);
        final boolean isModule   = parsedPagename.namespace.isType(NamespaceCode.MODULE_NAMESPACE_KEY);

        if (isTemplate || isModule) {
            return getTemplateOrModule(parsedPagename.fullPagename(), templateParameters);
        } else {
            logger.warn("'"+parsedPagename+"' is not a template or module - returning null");
            return null;
        }
    }

    private String getTemplateOrModule(String pageName, Map<String, String> templateParameters) throws WikiModelContentException {
        try {
            TopicData topicData = fWikiDB.selectTopic(pageName);
            if (topicData != null) {
                final String content = getRedirectedWikiContent(topicData.getContent(), templateParameters);
                if (content != null && content.length() > 0) {
                    logger.debug("retrieved '"+pageName+"' from cache");
                    return content;
                } else {
                    return null;
                }
            } else {
                return fetchAndCacheContent(pageName, templateParameters);
            }
        } catch (SQLException e) {
            logger.warn(null, e);
            final String message = e.getMessage() == null ? e.getClass().getSimpleName() : e.getMessage();
            throw new WikiModelContentException("<span class=\"error\">Exception: " + message + "</span>", e);
        }
    }

    private String fetchAndCacheContent(String fullPageName, Map<String, String> templateParameters) throws SQLException {
        logger.debug("fetching '"+fullPageName+"' from API");
        fUser.login();
        List<Page> listOfPages = fUser.queryContent(fullPageName);
        if (listOfPages.size() > 0) {
            final Page page = listOfPages.get(0);
            String content = page.getCurrentContent();
            if (content != null) {
                fWikiDB.insertTopic(new TopicData(fullPageName, content));
                content = getRedirectedWikiContent(content, templateParameters);
                return content != null && content.length() > 0 ?  content : null;
            } else {
                logger.warn("no content for page "+page);
                return null;
            }
        } else {
            logger.warn("no content for page "+fullPageName);
            return null;
        }
    }

    private String getRedirectedWikiContent(String rawWikitext, Map<String, String> templateParameters) {
        if (rawWikitext == null || rawWikitext.length() < 9) {
            // less than "#REDIRECT" string
            return rawWikitext;
        }
        String redirectedLink = WikipediaParser.parseRedirect(rawWikitext, this);
        if (redirectedLink != null) {
            ParsedPageName redirParsedPage = ParsedPageName.parsePageName(this, redirectedLink, fNamespace.getTemplate(), true, true);
            return WikipediaParser.getRedirectedRawContent(this, redirParsedPage, templateParameters);
        }
        return rawWikitext;
    }

    public void appendInternalImageLink(String hrefImageLink, String srcImageLink, ImageFormat imageFormat) {
        try {
            String imageName = imageFormat.getFilename();
            ImageData imageData = fWikiDB.selectImage(imageName);
            if (imageData != null) {
                File file = new File(imageData.getFilename());
                if (file.exists()) {
                    super.appendInternalImageLink(hrefImageLink, "file:///" + imageData.getFilename(), imageFormat);
                    return;
                }
            }
            String imageNamespace = fNamespace.getImage().getPrimaryText();
            setDefaultThumbWidth(imageFormat);
            String[] listOfTitleStrings = { imageNamespace + ":" + imageName };
            fUser.login();
            List<Page> listOfPages;
            if (imageFormat.getWidth() > 0) {
                listOfPages = fUser.queryImageinfo(listOfTitleStrings, imageFormat.getWidth());
            } else {
                listOfPages = fUser.queryImageinfo(listOfTitleStrings);
            }
            if (listOfPages.size() > 0) {
                Page page = listOfPages.get(0);
                imageData = new ImageData(imageName);

                // download the image to fImageDirectory directory
                String imageUrl;
                if (imageFormat.getWidth() > 0) {
                    imageUrl = page.getImageThumbUrl();
                } else {
                    imageUrl = page.getImageUrl();
                }

                String urlImageName = Encoder.encodeTitleLocalUrl(page.getTitle());
                if (imageUrl != null) {
                    int index = imageUrl.lastIndexOf('/');
                    if (index > 0) {
                        urlImageName = Encoder.encodeTitleLocalUrl(imageUrl.substring(index + 1));
                    }
                }
                if (fImageDirectory != null) {
                    String filename = fImageDirectory + urlImageName;
                    File file = new File(filename);
                    if (!file.exists()) {
                        try (OutputStream os = new FileOutputStream(filename)) {
                            page.downloadImageUrl(os, imageUrl);
                        } catch (IOException e) {
                            logger.warn(null, e);
                        }
                    }
                    imageData.setUrl(imageUrl);
                    imageData.setFilename(filename);
                    fWikiDB.insertImage(imageData);
                    super.appendInternalImageLink(hrefImageLink, "file:///" + filename, imageFormat);
                }
            }
        } catch (SQLException e) {
            logger.warn(null, e);
        }
    }

    public void parseInternalImageLink(String imageNamespace, String rawImageLink) {
        String imageSrc = getImageBaseURL();
        if (imageSrc != null) {
            String imageHref = getWikiBaseURL();
            ImageFormat imageFormat = ImageFormat.getImageFormat(rawImageLink, imageNamespace);

            String imageName = imageFormat.getFilename();
            imageName = Encoder.encodeUrl(imageName);
            if (replaceColon()) {
                imageHref = imageHref.replace("${title}", imageNamespace + '/' + imageName);
                imageSrc = imageSrc.replace("${image}", imageName);
            } else {
                imageHref = imageHref.replace("${title}", imageNamespace + ':' + imageName);
                imageSrc = imageSrc.replace("${image}", imageName);
            }
            appendInternalImageLink(imageHref, imageSrc, imageFormat);
        }
    }
}
