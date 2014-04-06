package info.bliki.api;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Fixtures {
    public static String xml(String name) throws IOException {
        try (InputStream is = Fixtures.class.getResourceAsStream("/xml/"+name+".xml")) {
            assert (is != null);
            try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
                byte[] buffer = new byte[8192];
                int n;
                while ((n = is.read(buffer)) != -1) {
                    bos.write(buffer, 0, n);
                }
                return new String(bos.toByteArray());
            }
        }
    }
}
