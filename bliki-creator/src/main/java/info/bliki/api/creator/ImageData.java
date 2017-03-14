package info.bliki.api.creator;

import java.io.File;
import java.util.Objects;

public class ImageData {
    private String fImageName;
    private String fImageUrl;
    private File fImageFile;

    public ImageData(String name) {
        this(name, "", null);
    }

    public ImageData(String name, String url) {
        this(name, url, null);
    }

    public ImageData(String name, String url, File filename) {
        fImageName = name;
        fImageUrl = url;
        fImageFile = filename;
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

    public File getFile() {
        return fImageFile;
    }

    public void setFile(File imageFilename) {
        this.fImageFile = imageFilename;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImageData imageData = (ImageData) o;
        return Objects.equals(fImageName, imageData.fImageName) &&
                Objects.equals(fImageUrl, imageData.fImageUrl) &&
                Objects.equals(fImageFile, imageData.fImageFile);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fImageName, fImageUrl, fImageFile);
    }
}
