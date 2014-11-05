package info.bliki.api;

import co.freeside.betamax.ProxyConfiguration;
import co.freeside.betamax.junit.Betamax;
import co.freeside.betamax.junit.RecorderRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static co.freeside.betamax.TapeMode.READ_ONLY;
import static co.freeside.betamax.TapeMode.READ_SEQUENTIAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.MockitoAnnotations.initMocks;

public class ConnectorTest {
    private Connector subject;

    @Rule public RecorderRule recorder = new RecorderRule(ProxyConfiguration.builder().build());
    private User anonUser;

    @Before public void before() {
        subject = new Connector();
        anonUser = new User(null, null, "http://en.wiktionary.org/w/api.php");
        anonUser.disableContentCompression();
        initMocks(this);
    }

    @Test public void testLoginAsAnonymous() {
        User user = new User(null, null, null);
        User result = subject.login(user);
        assertThat(result).isSameAs(user);
    }

    @Betamax(tape="loginWithUsernameFailed", mode = READ_SEQUENTIAL)
    @Test public void testLoginWithUsernameFailure() throws Exception {
        User user = new User("someUser", "somePassword", "http://meta.wikimedia.org/w/api.php");
        user.disableContentCompression();
        User result = subject.login(user);
        assertThat(result).isNull();
    }

    @Betamax(tape = "loginWithUsernameSuccess", mode = READ_SEQUENTIAL)
    @Test public void testLoginWithUsernameSuccess() throws Exception {
        User user = new User("jberkel", "testing", "http://en.wiktionary.org/w/api.php");
        user.disableContentCompression();
        User result = subject.login(user);

        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo("jberkel");
        assertThat(result.getNormalizedUsername()).isEqualTo("Jberkel");
        assertThat(result.getUserid()).isEqualTo("1580588");
    }

    @Betamax(tape = "queryContentFoo", mode = READ_ONLY)
    @Test public void testQueryContent() throws Exception {
        List<Page> pages = subject.queryContent(anonUser, Arrays.asList("foo"));
        assertThat(pages).hasSize(1);

        Page page = pages.get(0);

        assertThat(page.getTitle()).isEqualTo("foo");
        assertThat(page.getCurrentContent()).isNotEmpty();
        assertThat(page.sizeOfCategoryList()).isZero();
        assertThat(page.getPageid()).isEqualTo("39480");
    }
}
