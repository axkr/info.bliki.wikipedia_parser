package info.bliki.api.creator;

public class ImageData {
    private String fImageName;
    private String fImageUrl;
    private String fImageFilename;

    public ImageData(String name) {
        this(name, "", "");
    }

    public ImageData(String name, String url) {
        this(name, url, "");
    }

    public ImageData(String name, String url, String filename) {
        fImageName = name;
        fImageUrl = url;
        fImageFilename = filename;
    }

    public String getName() {
        return fImageName;
    }

    public void setName(String imageName) {
        this.fImageName = imageName;
    }

    public String getUrl() {
        return fImageUrl;
    }

    public void setUrl(String imageUrl) {
        this.fImageUrl = imageUrl;
    }

    public String getFilename() {
        return fImageFilename;
    }

    public void setFilename(String imageFilename) {
        this.fImageFilename = imageFilename;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ImageData imageData = (ImageData) o;

        if (fImageFilename != null ? !fImageFilename.equals(imageData.fImageFilename) : imageData.fImageFilename != null)
            return false;
        if (fImageName != null ? !fImageName.equals(imageData.fImageName) : imageData.fImageName != null) return false;
        if (fImageUrl != null ? !fImageUrl.equals(imageData.fImageUrl) : imageData.fImageUrl != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = fImageName != null ? fImageName.hashCode() : 0;
        result = 31 * result + (fImageUrl != null ? fImageUrl.hashCode() : 0);
        result = 31 * result + (fImageFilename != null ? fImageFilename.hashCode() : 0);
        return result;
    }
}
