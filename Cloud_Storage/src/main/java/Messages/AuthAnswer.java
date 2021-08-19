package Messages;

// класс для отправки ответа от сервера об успешности логина/регистрации
public class AuthAnswer implements Message {

    private boolean loginAccepted;
    private int reason;
    //    счетчик reason указывает почему loginAccepted == false.
//    1 - ошибака при логине (логин/пароль)
//    2 - ошибка при регистрации
//    Пока оставлю так, потом добавлью enum, boolean loginAccepted вообще уберу
    private String name;

    public boolean isLoginAccepted() {
        return loginAccepted;
    }

    public void setLoginAccepted(boolean loginAccepted) {
        this.loginAccepted = loginAccepted;
    }

    public int getReason() {
        return reason;
    }

    public void setReason(int reason) {
        this.reason = reason;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
