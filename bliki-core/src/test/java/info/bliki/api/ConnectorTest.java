package info.bliki.api;

import co.freeside.betamax.ProxyConfiguration;
import co.freeside.betamax.TapeMode;
import co.freeside.betamax.junit.Betamax;
import co.freeside.betamax.junit.RecorderRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.MockitoAnnotations.initMocks;

public class ConnectorTest {
    private Connector subject;
    private User user;

    @Rule public RecorderRule recorder = new RecorderRule(ProxyConfiguration.builder().build());

    @Before public void before() {
        subject = new Connector();
        initMocks(this);
    }

    @Test public void testLoginAsAnonymous() {
        user = new User(null, null, null);
        User result = subject.login(user);
        assertThat(result).isSameAs(user);
    }

    @Betamax(tape="loginWithUsernameFailed", mode = TapeMode.READ_SEQUENTIAL)
    @Test public void testLoginWithUsernameFailure() throws Exception {
        user = new User("someUser", "somePassword", "http://meta.wikimedia.org/w/api.php");

        User result = subject.login(user);
        assertThat(result).isNull();
    }

    @Betamax(tape = "loginWithUsernameSuccess",
             mode = TapeMode.READ_SEQUENTIAL)
    @Test public void testLoginWithUsernameSuccess() throws Exception {
        user = new User("jberkel", "testing", "http://en.wiktionary.org/w/api.php");

        User result = subject.login(user);

        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo("jberkel");
        assertThat(result.getNormalizedUsername()).isEqualTo("Jberkel");
        assertThat(result.getUserid()).isEqualTo("1580588");
    }
}
