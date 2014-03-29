package info.bliki.api.query;

import info.bliki.api.User;
import junit.framework.TestCase;

/**
 * Base test case for all query test cases.
 */
public abstract class BaseQueryTest extends TestCase {

	public static final String DEFAULT_MEDIA_WIKI_API_URL = "http://meta.wikimedia.org/w/api.php";
	private static final String DEFAULT_USER = "wrong_user";
	private static final String DEFAULT_PASSWORD = "wrong_password";

	protected BaseQueryTest() {
	}

	protected BaseQueryTest(String name) {
		super(name);
	}

	protected User getAnonymousUser() {
		// no username and password
		return new User("", "", DEFAULT_MEDIA_WIKI_API_URL);
	}

	/**
	 * Define <code>DEFAULT_USER</code> and <code>DEFAULT_PASSWORD</code> with a
	 * correct user/password and modify the
	 * <code>ParseTest#testParseRegisteredQuery()</code> JUnit method to test your
	 * user.
	 * 
	 * @return
	 * @see ParseTest#testParseRegisteredQuery()
	 */
	protected User getRegisteredUser() {
		return new User(DEFAULT_USER, DEFAULT_PASSWORD, DEFAULT_MEDIA_WIKI_API_URL);
	}
}
