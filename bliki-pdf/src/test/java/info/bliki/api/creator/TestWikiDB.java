package info.bliki.api.creator;

import org.apache.commons.compress.utils.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TestWikiDB extends WikiDB {
    public TestWikiDB(File directory) throws SQLException {
        super(directory);
    }

    public void dumpToDirectory(File directory) throws IOException, SQLException {
        if (directory.exists()) {
            logger.debug("directory "+directory+ " already exists, skipping");
        } else {
            assert(directory.mkdirs());
        }

        logger.debug("dumping content of database to directory "+directory);
        Statement stmt = fConnection.createStatement();
        if (stmt.execute("SELECT topic_name, version_content FROM topic")) {
            try (ResultSet resultSet = stmt.getResultSet()) {
                while (resultSet.next()) {
                    String name = resultSet.getString(1);
                    try (InputStream input = resultSet.getClob(2).getAsciiStream()) {
                        String quotedName = name
                                .replaceAll("/", "_")
                                .replaceAll(":", "_");

                        if (quotedName.startsWith("Module")) {
                            quotedName = quotedName + ".lua";
                        }

                        File page = new File(directory, quotedName);
                        logger.debug("writing "+page);

                        try (FileOutputStream fos = new FileOutputStream(page)) {
                            IOUtils.copy(input, fos);
                        }
                    }
                }
            }
        } else {
            logger.warn("did not get any results");
        }
    }
}
