package info.bliki.extensions.scribunto.interfaces;


import java.util.HashMap;
import java.util.Map;

final class Languages {
    private Map<String,String> codes = new HashMap<>();

    {
        codes.put("en", "English");
        codes.put("ru", "русский");
    }

    public String getName(String code, String inLang) {
        return codes.get(code);
    }
}
