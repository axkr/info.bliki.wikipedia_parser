package info.bliki.api.creator;

import javax.annotation.Nullable;
import java.util.Objects;

public class TopicData {
    private String fTopicName;
    private String fTopicContent;

    public TopicData(String name) {
        this(name, "");
    }

    public TopicData(String name, String content) {
        fTopicName = name.replace(' ', '_'); // dbKeyForm
        fTopicContent = content;
    }

    @Nullable public String getName() {
        return fTopicName;
    }

    public void setName(String topicName) {
        this.fTopicName = topicName;
    }

    @Nullable public String getContent() {
        return fTopicContent;
    }

    public void setContent(String topicContent) {
        this.fTopicContent = topicContent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TopicData topicData = (TopicData) o;
        return Objects.equals(fTopicName, topicData.fTopicName) &&
                Objects.equals(fTopicContent, topicData.fTopicContent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fTopicName, fTopicContent);
    }
}
