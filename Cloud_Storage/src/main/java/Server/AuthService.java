package Server;

import java.sql.*;


public class AuthService {
    private static Connection connection;
    private static Statement statement;

    static void connect() {
//        Подключаемся к БД
        final String URL = "jdbc:mysql://localhost:3306/SCS";
        final String USER = "server";
        final String PASSWORD = "server";
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            statement = connection.createStatement();
            System.out.println("Successfully connected to DB: " + URL);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    //    сделать login в БД уникальным и убрать эту проверку
    public static boolean isLoginOccupied(String loginFromClient) {
        String sql = "SELECT login FROM users";
        try {
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                String loginFromDB = rs.getString(1);
                if (loginFromDB.equals(loginFromClient)) {
                    return true;
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public static void setLoginAndPass(String loginFromClient, String passFromClient) throws SQLException {
//        поменять на prepared Statement
        String sql = String.format("INSERT INTO users (login, pass) VALUES ('%s', '%s')",
                loginFromClient, passFromClient);
        statement.executeUpdate(sql);
    }

    public static boolean checkLoginAndPass(String loginFromClient, String passFromClient) {
        String sql = String.format("SELECT login, pass FROM users");
        try {
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                String loginFromDB = rs.getString(1);
                String passFromDB = rs.getString(2);
                if (loginFromDB.equals(loginFromClient) && passFromDB.equals(passFromClient)) {
                    return true;
                }
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    static void disconnect() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
