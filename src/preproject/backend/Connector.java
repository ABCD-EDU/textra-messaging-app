package preproject.backend;

import preproject.backend.handlers.DatabaseHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Connector {
    private Connection connect;

    public static void main(String[] args) {
        new Connector();
    }

    private Connector() {
        createConnection();
    }

    private void createConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");

            connect = DriverManager.getConnection("jdbc:mysql://localhost/feedback?autoReconnect=true&useSSL=false"
                    ,"cs222-pregrp3", "prelimgroup3");
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("DATABASE CONNECTION FAIL");
            e.printStackTrace();
        } finally {
            System.out.println("DATABASE CONNECTION SUCCESS");
        }
    }

    private ResultSet getAllData(String table) {
        DatabaseHandler handler = new DatabaseHandler(connect);
        return handler.readDatabase(table);
    }
}
