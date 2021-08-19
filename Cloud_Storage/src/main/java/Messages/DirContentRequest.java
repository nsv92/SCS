package Messages;

//запрос списка содержимого папки
public class DirContentRequest implements Message {
    private String dirPath;


    public String getDirPath() {
        return dirPath;
    }

    public void setDirPath(String dirPath) {
        this.dirPath = dirPath;
    }
}
