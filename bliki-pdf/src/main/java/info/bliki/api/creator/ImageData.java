package info.bliki.api.creator;

import java.io.File;

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

        if (fImageFile != null ? !fImageFile.equals(imageData.fImageFile) : imageData.fImageFile != null) return false;
        if (fImageName != null ? !fImageName.equals(imageData.fImageName) : imageData.fImageName != null) return false;
        if (fImageUrl != null ? !fImageUrl.equals(imageData.fImageUrl) : imageData.fImageUrl != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = fImageName != null ? fImageName.hashCode() : 0;
        result = 31 * result + (fImageUrl != null ? fImageUrl.hashCode() : 0);
        result = 31 * result + (fImageFile != null ? fImageFile.hashCode() : 0);
        return result;
    }
}
