package info.bliki.api.creator;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class DbUtils {
    public static void uncompressDb(File input, File targetDir) throws IOException {
        if (targetDir.isDirectory()) return;
        if (!input.canRead()) throw new FileNotFoundException("cannot find input "+input);

        System.out.println("unpacking db to "+targetDir);

        TarArchiveEntry entry;
        try (TarArchiveInputStream tis = new TarArchiveInputStream(
                new BZip2CompressorInputStream(
                        new FileInputStream(input))
        )) {
            while ((entry = tis.getNextTarEntry()) != null) {
                File file = new File(targetDir.getParentFile(), entry.getName());
                if (entry.isDirectory()) {
                    if (!file.exists()) assert(file.mkdirs());
                } else if (entry.isFile()) {
                    if (!file.getParentFile().exists()) assert (file.getParentFile().mkdirs());
                    long readBytes = 0;
                    byte[] buffer = new byte[8192];
                    try (FileOutputStream fos = new FileOutputStream(file)) {
                        int n;
                        while (readBytes < entry.getSize()) {
                            n = tis.read(buffer, 0, (int) Math.min(entry.getSize() - readBytes, buffer.length));
                            if (n == -1) {
                                break;
                            }
                            fos.write(buffer, 0, n);
                            readBytes += n;
                        }
                    }
                }
            }
        }
    }
}
