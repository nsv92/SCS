package Messages;

import java.util.Scanner;

public class RegRequest implements Message {
    private String login;
    private String password;

    public RegRequest() {
    }

    public String getLogin() {
        return login;
    }

    public void setLogin() {
        Scanner in = new Scanner(System.in);
        System.out.print("Enter Login: ");
        login = in.nextLine();
//        добавить проверку на латиницу
    }

    public String getPassword() {
        return password;
    }

    public void setPassword() {
        Scanner in = new Scanner(System.in);
        System.out.print("Enter password: ");
        password = in.nextLine();
    }
}
