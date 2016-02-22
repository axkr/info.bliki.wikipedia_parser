package info.bliki.api.creator;

import co.freeside.betamax.ProxyConfiguration;
import co.freeside.betamax.junit.RecorderRule;
import info.bliki.annotations.IntegrationTest;
import info.bliki.api.TestUser;
import info.bliki.api.User;
import info.bliki.wiki.impl.APIWikiModel;
import info.bliki.wiki.model.Configuration;
import org.junit.Rule;
import org.junit.experimental.categories.Category;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Locale;

import static info.bliki.wiki.filter.Encoder.encodeTitleLocalUrl;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.contentOf;

@Category(IntegrationTest.class)
public abstract class HTMLCreatorIntegrationTest {
    @Rule
    public RecorderRule recorder = new RecorderRule(ProxyConfiguration.builder().sslEnabled(true).build());

    protected  TestWikiDB createTestDB(String name) throws SQLException, IOException {
        File directory = File.createTempFile("bliki-integration-tests", name);
        assert directory.delete();
        return new TestWikiDB(directory);
    }

    protected Configuration getConfiguration() {
        return new Configuration();
    }

    protected Result testAPI(String title, String apiLink, WikiDB db, Locale locale) throws IOException {
        User user = new TestUser(null, null, apiLink);
        user.login();

        Path mainDirectory = Files.createTempDirectory("bliki-" + encodeTitleLocalUrl(title).replace("/", "_"));
        Path imageDirectory = mainDirectory.resolve("WikiImages");

        APIWikiModel wikiModel = new APIWikiModel(user, db,
                getConfiguration(),
                locale,
                "${image}",
                "${title}",
                imageDirectory.toFile());

        DocumentCreator creator = new DocumentCreator(wikiModel, user, new String[]{title});
        @SuppressWarnings("StringBufferReplaceableByString")
        StringBuilder builder = new StringBuilder();
        builder.append(HTMLConstants.HTML_HEADER1)
                .append(HTMLConstants.CSS_MAIN_STYLE)
                .append(HTMLConstants.CSS_SCREEN_STYLE)
                .append(HTMLConstants.HTML_HEADER2);
        creator.setHeader(builder.toString());
        creator.setFooter(HTMLConstants.HTML_FOOTER);
        wikiModel.setUp();
        wikiModel.setTemplateCallsCache(new HashMap<String, String>());
        Path generatedHTMLFilename = mainDirectory.resolve(encodeTitleLocalUrl(title) + ".html");
        creator.renderToFile(generatedHTMLFilename.toString());
        System.out.println("Created file: " + generatedHTMLFilename);

        assertThat(generatedHTMLFilename.toFile()).isFile();
        assertThat(generatedHTMLFilename.toFile().length()).isGreaterThan(0);

        return new Result(generatedHTMLFilename.toFile(), wikiModel.getRedirectLink());
    }

    protected static class Result {
        final String redirectLink;
        final File content;

        Result(File content, String redirectLink) {
            this.redirectLink = redirectLink;
            this.content = content;
        }

        public void assertContains(CharSequence ...values) {
            assertThat(contentOf(content)).contains(values);
        }

        public void assertNoUnexpandedTemplates() {
            assertThat(contentOf(content)).doesNotContain("Template:");
        }

        public void assertNoUnexpandedLinks() {
            assertThat(contentOf(content)).doesNotContain("[[");
            assertThat(contentOf(content)).doesNotContain("]]");
        }

        public Result assertNoErrors() {
            assertNoSubstLeft();
            assertNoTemplateError();
            assertNoExpressionError();
            assertNoInclude();
            assertNoUnexpandedLinks();
            assertNoUnexpandedTemplates();
            return this;
        }

        public void assertNoTemplateError() {
            assertThat(contentOf(content)).doesNotContain("TemplateParserError:");
        }

        public void assertNoExpressionError() {
            assertThat(contentOf(content)).doesNotContain("Expression error");
        }

        public void assertNoSubstLeft() {
            assertThat(contentOf(content)).doesNotContain("safesubst:");
        }

        public void assertNoTemplatesLeft() {
            assertThat(contentOf(content)).doesNotContain("{{");
        }

        public  void assertNoInclude() {
            assertThat(contentOf(content)).doesNotContain("noinclude");
        }
    }
}
