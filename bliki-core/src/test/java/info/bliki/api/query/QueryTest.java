package info.bliki.api.query;


import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class QueryTest extends BaseQueryTest {

    @Test public void test001() {
        RequestBuilder request = Query.create().titles("Main Page", "API");
        assertThat(request.toString()).isEqualTo("action=query&amp;format=xml&amp;titles=Main Page|API");
    }

    @Test public void test002() {
        RequestBuilder request = Query.create().list("allpages").apfrom("Java").aplimit(20).format("json");
        assertThat(request.toString()).isEqualTo("action=query&amp;apfrom=20&amp;format=json&amp;list=allpages");
    }

    @Test public void test003() {
        RequestBuilder request = Query.create().list("categorymembers");
        request.put("cmtitle", "Category:Physics");
        assertThat(request.toString()).isEqualTo("action=query&amp;cmtitle=Category:Physics&amp;format=xml&amp;list=categorymembers");
    }
}
