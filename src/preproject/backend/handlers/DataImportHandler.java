package preproject.backend.handlers;

import preproject.backend.models.Message;
import preproject.backend.models.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

/**
 * The DataImportHandler should only be used if the user is done logging in. In order to improve memory efficiency, we would need
 * to import only the data that concerns with the User currently logged in. For example, if user u2203683 is currently
 * logged in, we must import all of the group messages that has u2203683 id as a recipient. Doing this, we won't need to
 * import all of the information in the database. Which can save a lot of data.
 */
public final class DataImportHandler {
    public static Map<String, Set<String>> parseGroupChatTable(ResultSet table) throws SQLException {
        Map<String, Set<String>> groupMap = null;
        try {
            groupMap = new HashMap<>();
            while (table.next()) {
                String groupId = table.getString("group_id");
                String userId = table.getString("user_id");

                if (groupMap.containsKey(groupId)) {
                    groupMap.get(groupId).add(userId);
                } else {
                    Set<String> set = new HashSet<>();
                    set.add(userId);
                    groupMap.put(groupId, set);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return groupMap;
    }

    public static List<User> parseUserTable(ResultSet table) throws SQLException {
        List<User> userList = null;
        try {
            userList = new ArrayList<>();
            while (table.next()) {
                String userId = table.getString("user_id");
                String email = table.getString("email");
                String firstName = table.getString("user_fname");
                String lastName = table.getString("user_lname");

                User user = new User(userId, email, firstName, lastName);
                userList.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userList;
    }

    public static Map<String, List<Message>> parseMessageTable(ResultSet table) throws SQLException {
        Map<String, List<Message>> messageMap = null;
        try {
            messageMap = new HashMap<>();
            while (table.next()) {
                String groupId = table.getString("to_group");
                String userId = table.getString("from_user");
                String message = table.getString("message");
                Timestamp timeSent = table.getTimestamp("time_sent");

                Message msg = new Message(groupId, message, timeSent);

                if (messageMap.containsKey(userId)) {
                    messageMap.get(userId).add(msg);
                } else {
                    List<Message> messages = new ArrayList<>();
                    messages.add(msg);
                    messageMap.put(userId, messages);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messageMap;
    }
}