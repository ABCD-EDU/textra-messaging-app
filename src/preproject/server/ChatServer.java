package preproject.server;

import preproject.server.handlers.RegisterHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class ChatServer {
    private final int PORT;
    private final List<String> onlineUserNames;
    private final List<UserThread> onlineUsers;
    private Map<String, Set<String>> groupList;

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
        // TODO: need to get group list as users add members and create new groups
        RegisterHandler registerHandler = new RegisterHandler();
//        for (int i = 0; i < 100; i++) {
//            registerHandler.registerUser(new PasswordAuthentication("test" + i + "@gmail.com", "password".toCharArray()), "test", String.valueOf(i));
//        }
     }

     private void populateGroupList() {

     }

    public void init() {
        try (ServerSocket serverSocket = new ServerSocket(this.PORT)){
            System.out.println("Server listening on PORT: " + this.PORT);
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("A client has connected to the server");

                UserThread userThread = new UserThread(socket, this);
                onlineUsers.add(userThread);
                userThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * TODO: CHOOSE ONLY THE USERS IN A SELECTED GROUP, AS OF NOW IT SENDS THE MESSAGE TO ALL THE PEOPLE IN THE SERVER
     * This method broadcasts the message to all of the users in a certain group. For example, the user = 2203683,
     * message = "Foo Bar" and the recipient = group_1111, all of the users inside group_1111 will receive the message
     *
     * @param user sender
     * @param message message
     * @param address receiver
     */
    protected void broadcast(String user, String message, String address) {
        for (UserThread userThread : onlineUsers) { // check each user logged in
            if (groupList.get(address).contains(userThread.getUser().getEmail())) { // if user is a member of the group
                if (!userThread.getUser().getEmail().equals(user)) { // if the current thread in loop is not the sender
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
