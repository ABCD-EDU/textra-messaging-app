package preproject.backend;

import java.sql.*;

public class Connector {
    public static Connection connect;

    /**
     * The joined table for finding the users for a certain group chat.
     */
    private static final String GROUP_CHAT_TABLE = "messenger.group_chat";

    /**
     * The table consisting all the current user in the database.
     */
    private static final String USER_ACCOUNTS_TABLE = "messenger.user_acc";

    /**
     * The table consisting of all the messages from each user.
     */
    private static final String MESSAGES_TABLE = "messenger.message";

    public Connector() {}

    public static void createConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");

            connect = DriverManager.getConnection("jdbc:mysql://localhost/messenger?allowPublicKeyRetrieval=true&autoReconnect=true&useSSL=false"
                    ,"cs222-pregrp3", "prelimgroup3");

            System.out.println("DATABASE CONNECTION SUCCESS");
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("DATABASE CONNECTION FAIL");
            e.printStackTrace();
        }
    }

    public ResultSet readDatabase(String table) {
        ResultSet resultSet = null;
        try {
            Statement statement = connect.createStatement();
            resultSet = statement.executeQuery(String.format("SELECT * FROM %s", table));

            System.out.println("GET data from " + table + " SUCCESS");
        } catch (SQLException throwables) {
            System.err.println("GET data from " + table + " FAIL");
            throwables.printStackTrace();
        }
        return resultSet;
    }

    public static Connection getConnect() {
        return connect;
    }
}
