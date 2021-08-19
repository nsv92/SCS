package Messages;

// для отправки информационных сообщений
public class InfoMessage implements Message{
    private String information;

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }
}
