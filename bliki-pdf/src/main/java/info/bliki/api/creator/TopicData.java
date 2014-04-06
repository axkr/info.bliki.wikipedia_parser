package info.bliki.api.creator;

public class TopicData {
    private String fTopicName;
    private String fTopicContent;

    public TopicData(String name) {
        this(name, "");
    }

    public TopicData(String name, String content) {
        fTopicName = name;
        fTopicContent = content;
    }

    public String getName() {
        return fTopicName;
    }

    public void setName(String topicName) {
        this.fTopicName = topicName;
    }

    public String getContent() {
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
        if (fTopicContent != null ? !fTopicContent.equals(topicData.fTopicContent) : topicData.fTopicContent != null)
            return false;
        if (fTopicName != null ? !fTopicName.equals(topicData.fTopicName) : topicData.fTopicName != null) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = fTopicName != null ? fTopicName.hashCode() : 0;
        result = 31 * result + (fTopicContent != null ? fTopicContent.hashCode() : 0);
        return result;
    }
}
