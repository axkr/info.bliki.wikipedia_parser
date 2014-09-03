package info.bliki.extensions.scribunto.template;

import info.bliki.wiki.namespaces.INamespace;
import info.bliki.wiki.namespaces.Namespace;
import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class TitleTest {
    INamespace.INamespaceValue ns;
    Title title;

    @Before public void before() {
        ns = new Namespace().getMain();
        title = Title.makeTitle(ns, "some title", "", "");
    }

    @Test public void testMakeTitleCreatesCorrectNamespace() throws Exception {
        assertThat(title.ns).isSameAs(ns);
    }

    @Test public void testMakeTitleCreatesCorrectDbKeyForm() throws Exception {
        assertThat(title.dbKeyForm).isEqualTo("some_title");
    }
}
