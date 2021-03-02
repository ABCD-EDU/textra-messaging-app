package preproject.server;

import preproject.server.handlers.RegisterHandler;

import javax.xml.transform.Result;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.ResultSet;
import java.util.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class ChatServer {
    private final int PORT;
    private final List<String> onlineUserNames;
    private final List<UserThread> onlineUsers;
    private Map<String, List<String>> groupList;

    public static void main(String[] args) {
        ChatServer c = new ChatServer(2000);
        c.init();
    }

    public ChatServer(int port) {
        Connector.createConnection();
        this.PORT = port;
        this.onlineUserNames = new ArrayList<>();
        this.onlineUsers = new ArrayList<>();
        // TODO: populate groupList
        groupList = new HashMap<>();
        initializeGroupList();
        // TODO: need to get group list as users add members and create new groups
     }

    /**
     * initialize and populate groupList with all of the groups created upon creation of Server.
     * Group list will be updated
     *
     * Algorithm:
     * 1. Get list of group id's
     * 2. Get list of members per group id
     */
    private void initializeGroupList() {
        try{
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
        }catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("INITIALIZED GROUPS LIST");
        System.out.println("Groups:");
        for (String group : groupList.keySet()) {
            System.out.println(group);
            System.out.println(groupList.get(group));
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
        try (ServerSocket serverSocket = new ServerSocket(this.PORT)){
            System.out.println("Server listening on PORT: " + this.PORT);
            while (true) {
                Socket socket = serverSocket.accept();
                UserThread userThread = new UserThread(socket, this);

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
     * @param dataMap
     * @param userIds
     * @param senderId
     */
    protected  void sendMapToListOfUsers(Map<String, Object> dataMap, ArrayList<String> userIds, String senderId) {
        for (UserThread userThread : onlineUsers)  // for each online user
            if (userIds.contains(String.valueOf(userThread.getUser().getUserId())) &&
                    !senderId.equals(String.valueOf(userThread.getUser().getUserId()))) { // if user is part of list of users and is not sender
                userThread.sendMap(dataMap);
            }
    }

    /**
     * TODO: CHOOSE ONLY THE USERS IN A SELECTED GROUP, AS OF NOW IT SENDS THE MESSAGE TO ALL THE PEOPLE IN THE SERVER
     * This method broadcasts the message to all of the users in a certain group. For example, the user = 2203683,
     * message = "Foo Bar" and the recipient = group_1111, all of the users inside group_1111 will receive the message
     *
     * @param user sender - user ID3
     * @param message message - literal String message
     * @param address receiver - group ID
     */
    protected void broadcast(String user, String message, String address) {
        for (UserThread userThread : onlineUsers) { // check each user logged in
            if (groupList.get(address).contains(String.valueOf(userThread.getUser().getUserId()))) { // if user is a member of the group
                if (!user.equals(String.valueOf(userThread.getUser().getUserId()))) { // if the current thread in loop is not the sender
                    try {
                        PreparedStatement preparedStatement = Connector.connect.prepareStatement(
                                "INSERT INTO message (from_user, group_id, message, time_sent) " +
                                        "VALUES (?, ?, ?, ?)"
                        );

                        Timestamp messageTimeSent = new Timestamp(new Date().getTime());

                        preparedStatement.setString(1, user);
                        preparedStatement.setString(2, address);
                        preparedStatement.setString(3, message);
                        preparedStatement.setTimestamp(4, messageTimeSent);

                        userThread.sendMessage(user, address, message, messageTimeSent);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void importMessageToDatabase(String user, String message, String address) {

    }

    protected void removeUser(String email, UserThread user) {
        boolean removed = onlineUserNames.remove(email);
        if (removed) {
            onlineUsers.remove(user);
            System.out.println("User with email " + email + " has logged out");
        }
    }
}
