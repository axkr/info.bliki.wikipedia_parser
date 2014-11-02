package info.bliki.api.creator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * A simple Apache Derby Database to store the retrieved Wiki contents
 */
public class WikiDB {
    private final PreparedStatement fSelectContent;
    private final PreparedStatement fInsertTopic;
    private final PreparedStatement fUpdateTopicContent;
    private final PreparedStatement fSelectImage;
    private final PreparedStatement fInsertImage;
    private final PreparedStatement fUpdateImage;

    protected Connection fConnection;
    protected Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * The Wiki database constructor. Creates a new Derby Wiki database, if it
     * doesn't already exists.
     *
     * @param directory the main directory where the database should be created
     * @throws java.sql.SQLException
     */
    public WikiDB(File directory) throws SQLException {
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("embedded derby driver not found, check your classpath");
        }

        Properties properties = new Properties();
        properties.put("user", "user1");
        properties.put("password", "user1");
        final String jdbcUrl = "jdbc:derby:" + directory.getAbsolutePath() + ";create=true;characterEncoding=utf-8";

        logger.debug("using JDBC url "+jdbcUrl+" with properties "+properties);
        fConnection = DriverManager.getConnection(jdbcUrl, properties);
        createTableIfItDoesntExist();

        fSelectContent = fConnection.prepareStatement("SELECT version_content FROM topic WHERE topic_name = ?");
        fInsertTopic = fConnection.prepareStatement("INSERT INTO topic (topic_name, version_content) VALUES (?,?)");
        fUpdateTopicContent = fConnection.prepareStatement("UPDATE topic SET version_content = ?  WHERE topic_name = ?");

        fSelectImage = fConnection.prepareStatement("SELECT image_url, image_filename FROM image WHERE image_name = ?");
        fInsertImage = fConnection.prepareStatement("INSERT INTO image (image_name, image_url, image_filename) VALUES (?,?, ?)");
        fUpdateImage = fConnection.prepareStatement("UPDATE image SET image_url = ?,  image_filename = ? WHERE image_name = ?");
    }

    public void tearDown() throws SQLException {
        fSelectContent.close();
        fInsertTopic.close();
        fUpdateTopicContent.close();
        fSelectImage.close();
        fInsertImage.close();
        fUpdateImage.close();
        fConnection.close();
    }

    /**
     * Select the topic data from the database
     *
     * @param name
     *          the name of the topic
     * @return <code>null</code> if no data was found
     * @throws SQLException
     */
    @Nullable public TopicData selectTopic(String name) throws SQLException {
        TopicData topicData = new TopicData(name);
        fSelectContent.setString(1, name);
        try (ResultSet resultSet = fSelectContent.executeQuery()) {
            if (resultSet.next()) {
                topicData.setContent(resultSet.getString(1));
                return topicData;
            }
        }
        return null;
    }

    public void insertTopic(TopicData topic) throws SQLException {
        fInsertTopic.setString(1, topic.getName());
        fInsertTopic.setString(2, topic.getContent());
        fInsertTopic.execute();
    }

    public void updateTopic(TopicData topic) throws SQLException {
        fUpdateTopicContent.setString(1, topic.getContent());
        fUpdateTopicContent.setString(2, topic.getName());
        fUpdateTopicContent.execute();
    }

    /**
     * Select the image data from the database
     *
     * @param imageName
     *          the name of the image
     * @return <code>null</code> if no data was found
     * @throws SQLException
     */
    @Nullable public ImageData selectImage(String imageName) throws SQLException {
        ImageData imageData = new ImageData(imageName);
        fSelectImage.setString(1, imageName);
        try (ResultSet resultSet = fSelectImage.executeQuery()) {
            if (resultSet.next()) {
                imageData.setUrl(resultSet.getString(1));
                imageData.setFilename(resultSet.getString(2));
                return imageData;
            }
        }
        return null;
    }

    public void insertImage(ImageData imageData) throws SQLException {
        fInsertImage.setString(1, imageData.getName());
        fInsertImage.setString(2, imageData.getUrl() != null ? imageData.getUrl() : "");
        fInsertImage.setString(3, imageData.getFilename());
        fInsertImage.execute();
    }

    public void updateImage(ImageData imageData) throws SQLException {
        fUpdateTopicContent.setString(1, imageData.getUrl());
        fUpdateTopicContent.setString(2, imageData.getFilename());
        fUpdateTopicContent.setString(3, imageData.getName());
        fUpdateTopicContent.execute();
    }

    private void createTableIfItDoesntExist() throws SQLException {
        ResultSet resultSet = fConnection.getMetaData().getTables("%", "%", "%", new String[] { "TABLE" });
        boolean shouldCreateTableTopic = true;
        boolean shouldCreateTableImage = true;
        String tableName;
        while (resultSet.next() && shouldCreateTableTopic) {
            tableName = resultSet.getString("TABLE_NAME");
            if (tableName.equalsIgnoreCase("topic")) {
                shouldCreateTableTopic = false;
            } else if (tableName.equalsIgnoreCase("image")) {
                shouldCreateTableImage = false;
            }
        }
        resultSet.close();

        if (shouldCreateTableTopic) {
            logger.debug("Creating Table topic...");
            Statement statement = fConnection.createStatement();
            statement.execute("CREATE TABLE topic "
                    + "(topic_id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),"
                    + " topic_name VARCHAR(256) NOT NULL, " + " version_content CLOB, " + " CONSTRAINT PK_Topic PRIMARY KEY (topic_id) "
                    + " ) ");
            statement.execute("CREATE INDEX indx_topic ON topic(topic_name)");
            statement.close();
        }
        if (shouldCreateTableImage) {
            logger.debug("Creating Table image...");
            Statement statement = fConnection.createStatement();
            statement.execute("CREATE TABLE image "
                    + "(image_id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),"
                    + " image_name VARCHAR(256) NOT NULL, " + " image_url VARCHAR(1024) NOT NULL, "
                    + " image_filename VARCHAR(1024) NOT NULL, " + " CONSTRAINT PK_Image PRIMARY KEY (image_id) " + " ) ");
            statement.execute("CREATE INDEX indx_image ON image(image_name)");
            statement.close();
        }
    }
}
