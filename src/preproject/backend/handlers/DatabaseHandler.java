package preproject.backend.handlers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseHandler {
    private Connection connect;

    public DatabaseHandler(Connection connect) {
        this.connect = connect;
    }

    public ResultSet readDatabase(String table) {
        ResultSet resultSet = null;
        try {
            Statement statement = connect.createStatement();
            resultSet = statement.executeQuery(String.format("SELECT * FROM %s", table));
        } catch (SQLException throwables) {
            System.err.println("GET data from " + table + " FAIL");
            throwables.printStackTrace();
        } finally {
            System.out.println("GET data from " + table + " SUCCESS");
        }
        return resultSet;
    }

    public void writeDatabase() {
        // TODO
    }
}