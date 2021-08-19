package Messages;

import java.util.Scanner;

public class AuthRequest implements Message {
    private String login;
    private String password;

//    public AuthRequest (String login, String password) {
//        this.login = login;
//        this.password = password;
//    }

    public AuthRequest() {
    }

    public String getLogin() {
        return login;
    }

    public void setLogin() {
        Scanner in = new Scanner(System.in);
        System.out.print("Enter Login: ");
        login = in.nextLine();
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
