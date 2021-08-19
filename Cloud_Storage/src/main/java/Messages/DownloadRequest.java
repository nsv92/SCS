package Messages;

public class DownloadRequest implements Message{
    private String path;

    public DownloadRequest(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
