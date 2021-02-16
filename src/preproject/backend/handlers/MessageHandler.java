package preproject.backend.handlers;

import preproject.backend.Connector;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class MessageHandler {
    public MessageHandler() {}

    public void writeMessageToDatabase(int id, String message, String receiver, Timestamp timestamp) {
        try {
            PreparedStatement statement = Connector.connect
                    .prepareStatement("INSERT INTO user_acc VALUES (?, ?, ?, ?)");

            statement.setInt(1, id);
            statement.setString(1, message);
            statement.setString(1, receiver);
            statement.setTimestamp(1, timestamp);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
