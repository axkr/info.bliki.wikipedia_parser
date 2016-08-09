package info.bliki.api;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;

public class UserTest {
    @Test
    public void testEnforceHttpsAPIURL() throws Exception {
        try {
            new User("", "", "http://foo.com");
            fail("expected exception");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage()).isEqualTo(
                "the mediawiki API url must use HTTPS (https://lists.wikimedia.org/pipermail/mediawiki-api-announce/2016-May/000110.html)");
        }
    }

    @Test
    public void testUserConstruction() throws Exception {
        User user = new User("", "", "https://foo.com");
        assertThat(user.getUsername()).isEqualTo("");
        assertThat(user.getPassword()).isEqualTo("");
        assertThat(user.getActionUrl()).isEqualTo("https://foo.com");
    }
}
