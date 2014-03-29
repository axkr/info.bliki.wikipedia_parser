package info.bliki.api;

import java.util.List;

/**
 * Example for querying image info data
 */
public class QueryImageInfoExample {
	public static void main(String[] args) {
		String pageName = "File:Mona Lisa.jpg";
		User user = new User("", "", "http://en.wikipedia.org/w/api.php");
		Connector connector = new Connector();
		user = connector.login(user);

		System.out.println("PAGE-NAME: " + pageName);
		// set image width thumb size to 200px
		List<Page> pages = user.queryImageinfo(new String[] { pageName }, 200);

		if (pages != null) {
			System.out.println("PAGES: " + pages.size());
			if (pages.size() > 0) {
				System.out.println("PAGES: " + pages.get(0).getTitle());
			}

		} else {
			System.out.println("PAGES: NULL!");
		}

		for (Page page : pages) {
			System.out.println("IMG-THUMB-URL: " + page.getImageThumbUrl());
			System.out.println("IMG-URL: " + page.getImageUrl());
		}
	}

}
