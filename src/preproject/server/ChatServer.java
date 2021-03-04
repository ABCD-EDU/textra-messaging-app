package preproject.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.util.*;
import java.util.Date;

public class ChatServer {
    private final int PORT;
    private final List<String> onlineUserIds;
    private final List<UserThread> onlineUsers;
    private final Map<String, List<String>> groupList;

    public static void main(String[] args) {
        ChatServer c = new ChatServer(2000);
        c.init();
    }

    public ChatServer(int port) {
        Connector.createConnection();
        this.PORT = port;
        this.onlineUserIds = new ArrayList<>();
        this.onlineUsers = new ArrayList<>();
        // TODO: populate groupList
        groupList = new HashMap<>();
        initializeGroupList();
        // TODO: need to get group list as users add members and create new groups
    }

    /**
     * initialize and populate groupList with all of the groups created upon creation of Server.
     * Group list will be updated
     * <p>
     * Algorithm:
     * 1. Get list of group id's
     * 2. Get list of members per group id
     */
    private void initializeGroupList() {
        try {
            // Step 1 - Get list of group_ids
            List<String> groupIds = new ArrayList<>();
            PreparedStatement group_idStatement = Connector.connect.prepareStatement(
                    "SELECT group_id FROM group_repo"
            );
            ResultSet group_idResultSet = group_idStatement.executeQuery();
            while (group_idResultSet.next()) {
                groupIds.add(String.valueOf(group_idResultSet.getInt("group_id")));
            }
            // Step 2 - Get list of member ids per group
            for (String grpId : groupIds) {
                List<String> memberIds = new ArrayList<>();
                PreparedStatement membersIdStatement = Connector.connect.prepareStatement(
                        "SELECT user_id FROM group_msg WHERE group_id = ?"
                );
                membersIdStatement.setInt(1, Integer.parseInt(grpId));
                ResultSet membersIdResultSet = membersIdStatement.executeQuery();
                while (membersIdResultSet.next()) {
                    memberIds.add(String.valueOf(membersIdResultSet.getInt("user_id")));
                }
                groupList.put(grpId, memberIds);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Will be used by UserThread to update server's groupList
     * everytime a new group is created
     */
    protected void updateGroupList(String groupId, List<String> memberIds) {
        groupList.put(groupId, memberIds);
    }

    /**
     * Will be used by UserThread to update server's groupList
     * everytime a new members is added to a group
     */
    protected void updateGroupList(String groupId, String newUserId) {
        List<String> members = groupList.get(groupId);
        members.add(newUserId);
        groupList.replace(groupId, members);
    }

    protected void init() {
        try (ServerSocket serverSocket = new ServerSocket(this.PORT)) {
            System.out.println("Server listening on PORT: " + this.PORT);
            while (true) {
                Socket socket = serverSocket.accept();
                UserThread userThread = new UserThread(socket, this);
                userThread.setDaemon(true);

                onlineUsers.add(userThread);
                userThread.start();
                System.out.println("A client has connected to the server. Total number of users: " + onlineUsers.size());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Send a Map of <String, Object> to everyone in a given group.
     */
    protected void sendMapToGroup(Map<String, Object> dataMap, String groupId, String senderId) {
        for (UserThread userThread : onlineUsers)  // for each online user
            if (groupList.get(groupId).contains(String.valueOf(userThread.getUser().getUserId())))  // if user is part of given group
                if (senderId.equals(String.valueOf(userThread.getUser().getUserId())))   // check if user is sender
                    userThread.sendMap(dataMap);
    }

    /**
     * Send a Map of <String, Object> to everyone in a given list of users.
     * If userIds.length = 0 then send to all online users
     * If senderId == "-1", send to everyone in group including sender
     */
    protected void sendMapToListOfUsers(Map<String, Object> dataMap, ArrayList<String> userIds, String senderId) {
        if (userIds.size() == 0) { // send to all online user except sender
            for (UserThread userThread : onlineUsers)
                if (!senderId.equals(String.valueOf(userThread.getUser().getUserId())))
                    userThread.sendMap(dataMap);
        } else { // send to all users in the userIds ArrayList
            for (UserThread userThread : onlineUsers)  // for each online user
                if (userIds.contains(String.valueOf(userThread.getUser().getUserId())) &&
                        !senderId.equals(String.valueOf(userThread.getUser().getUserId()))
                        && userThread.getUser().isLoggedIn()) { // if user is part of list of users and is not sender
                    userThread.sendMap(dataMap);
                }
        }
    }

    protected void broadcastMessageToAllOnlineUsers(List<Map<String, String>> messagesList, String senderId) {
        System.out.println(messagesList);
        System.out.println(senderId);
        for (Map<String, String> messageMap : messagesList) {
            for (UserThread userThread : onlineUsers) {
                if (!senderId.equals(String.valueOf(userThread.getUser().getUserId())) && userThread.getUser().isLoggedIn()) {
                    userThread.sendMessage(
                            Integer.parseInt(messageMap.get("userId")),
                            "-1",
                            messageMap.get("messageSent"),
                            new Timestamp(new Date().getTime()));
                }
            }
        }

    }

    /**
     * TODO: CHOOSE ONLY THE USERS IN A SELECTED GROUP, AS OF NOW IT SENDS THE MESSAGE TO ALL THE PEOPLE IN THE SERVER
     * This method broadcasts the message to all of the users in a certain group. For example, the user = 2203683,
     * message = "Foo Bar" and the recipient = group_1111, all of the users inside group_1111 will receive the message
     *
     * @param user    sender - user ID
     * @param message message - literal String message
     * @param address receiver - group ID
     */
    protected void broadcast(int user, String message, String address) {
        writeUnreadMessage(address);
        for (UserThread userThread : onlineUsers) { // check each user logged in
            if (user != userThread.getUser().getUserId() && userThread.getUser().isLoggedIn()) { // if the current thread in loop is not the sender
                Timestamp messageTimeSent = new Timestamp(new Date().getTime());
                userThread.sendMessage(user, address, message, messageTimeSent);
            }
        }
    }

    private void writeUnreadMessage(String address) {
        try {
            PreparedStatement getLastMessageId = Connector.connect.prepareStatement(
                    "SELECT LAST_INSERT_ID() FROM message"
            );

            ResultSet idSet = getLastMessageId.executeQuery();
            if (idSet.next()) {
                int id = idSet.getInt(1);
                addUnreadMessage(id, Integer.parseInt(address));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addUnreadMessage(int msgId, int grpId) {
        try {
            PreparedStatement preparedStatement = Connector.connect.prepareStatement(
                    "SELECT user_id FROM group_msg WHERE group_id = ?;"
            );

            preparedStatement.setInt(1, grpId);
            List<Integer> offlineUsers = new ArrayList<>();
            ResultSet userListQuery = preparedStatement.executeQuery();
            while (userListQuery.next()) {
                int id = userListQuery.getInt("user_id");
                offlineUsers.add(id);
            }

            PreparedStatement writeUnreadMsg = Connector.connect.prepareStatement(
                    "INSERT INTO unread_msg (message_id, user_id, group_id) VALUES (?, ?, ?)"
            );

            for (int user : offlineUsers) {
                if (!onlineUserIds.contains(String.valueOf(user))) {
                    writeUnreadMsg.setInt(1, msgId);
                    writeUnreadMsg.setInt(2, user);
                    writeUnreadMsg.setInt(3, grpId);
                    writeUnreadMsg.executeUpdate();
                }
            }
        } catch (SQLException e) {
            System.out.println("Database is empty");
        }
    }

    protected void removeUser(UserThread user) {
        onlineUsers.remove(user);
        try {
            System.out.println("SOCKET closed successfully! | user: \"" + user.getUser().getEmail() + "\" has successfully LOGGED OUT at " + new Timestamp(System.currentTimeMillis()).toString());
        } catch (NullPointerException e) {
            System.out.println("SOCKET closed successfully! | a user who has not yet logged in has disconnected at " + new Timestamp(System.currentTimeMillis()).toString());
        }
    }

    protected void addOnlineUser(String id) {
        System.out.println("ID ADDED! " + id);
        this.onlineUserIds.add(id);
    }
}
