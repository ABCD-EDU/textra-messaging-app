package preproject.server;

import java.sql.*;

public class Connector {
    public static Connection connect;

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
}
