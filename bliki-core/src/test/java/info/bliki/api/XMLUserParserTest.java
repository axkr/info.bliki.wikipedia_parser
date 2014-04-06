package info.bliki.api;

import org.junit.Before;
import org.junit.Test;

import static info.bliki.api.Fixtures.xml;
import static org.fest.assertions.api.Assertions.assertThat;

public class XMLUserParserTest {
    private User user;

    @Before public void before() {
        user = new User("name", "password", "url");
    }

    @Test public void testParseNeedsTokenResponse() throws Exception {
        XMLUserParser parser = new XMLUserParser(user, xml("needsToken"));
        parser.parse();

        assertThat(user.getToken()).isEqualTo("35bfdf8bfe86f5e3f420cb3296a9f2fc");
        assertThat(user.getResult()).isEqualTo("NeedToken");
    }
}
