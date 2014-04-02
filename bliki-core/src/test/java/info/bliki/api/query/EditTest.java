package info.bliki.api.query;

import org.junit.Test;

/**
 * Tests edit query.
 */
public class EditTest extends BaseQueryTest {

    @Test public void testDummy() {
        // ..
    }

    /**
     * To make this test work correctly uncomment it and be sure that you have a
     * internet connection running.
     */
    // @Test public void testSmokeTest() {
    // User user = getAnonymousUser();
    // Connector connector = new Connector();
    // connector.login(user);
    //
    // try {
    // // there is no rights to do this action. The error must be received.
    // connector.edit(user, Edit.create().title("MainTestTest").text("blabla"));
    // fail("UnexpectedAnswerException must be thrown");
    // } catch (UnexpectedAnswerException e) {
    // // ok
    // }
    // }

}
