package preproject.backend;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
        // TODO: need to get group list as users add members and create new groups

//        RegisterHandler registerHandler = new RegisterHandler();
//        registerHandler.registerUser(new PasswordAuthentication("johndoe@gmail.com", "password123".toCharArray()), "john", "doe");
    }

    public void init() {
        try (ServerSocket serverSocket = new ServerSocket(this.PORT)){
            System.out.println("Server listening on PORT: " + this.PORT);
//            RegisterHandler registerHandler = new RegisterHandler();
//            registerHandler.registerUser(new PasswordAuthentication("testemail@gmail.com", "password123".toCharArray()), "test", "user");
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
                    userThread.sendMessage(message, address);
                }
            }
        }
    }

    protected void removeUser(String email, UserThread user) {
        boolean removed = onlineUserNames.remove(email);
        if (removed) {
            onlineUsers.remove(user);
            System.out.println("User with email " + email + " has logged out");
        }
    }
}
