package preproject.backend;

import preproject.backend.handlers.LoginHandler;
import preproject.backend.handlers.RegisterHandler;
import preproject.backend.models.User;

import java.io.*;
import java.net.PasswordAuthentication;
import java.net.Socket;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

/**
 * This class runs a session for the server. Where each {@code UserThread} is considered as one session.
 * The server will continually accept the ongoing connection to the socket as much as possible which leads to
 * multiple users accessing in one client.
 */
public class UserThread {
    private final Socket SOCKET;
    private final ChatServer SERVER;
    private ObjectOutputStream writer;
    private User user;

    public UserThread(Socket socket, ChatServer server) {
        this.SOCKET = socket;
        this.SERVER = server;
    }

    public void init() {
        try {
            InputStream input = SOCKET.getInputStream();
            ObjectInputStream reader = new ObjectInputStream(input);

            OutputStream output = SOCKET.getOutputStream();
            writer = new ObjectOutputStream(output);

            // constantly receive data being sent and process it
            while (true) {
                processReadData(reader);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private void processReadData(ObjectInputStream reader) {
        try {
            Map.Entry<String, Object> readData = (Map.Entry<String, Object>) reader.readObject();
            switch (readData.getKey()) {
                case Action.LOGIN_USER:
                    this.loginAuthentication(new LoginHandler(), (PasswordAuthentication) readData.getValue());
                    break;
                case Action.LOGOUT_USER:
                    this.SOCKET.close(); // close the socket when user logouts
                    break;
                case Action.REGISTER_USER:
                    this.registerUser(new RegisterHandler(), (Map<String, String>) readData.getValue());
                    break;
                case Action.SEND_MESSAGE:
                    this.broadcastMessage((Map<String, String>) readData.getValue());
                    break;
                case Action.ADD_FAVOURITE:
                    this.addUserFavouriteGroup((Map.Entry<String, String>) readData.getValue());
                    break;
                case Action.ADD_GROUP:
                    this.addGroup((Map<String, Object>) readData.getValue());
                    break;
                case Action.ADD_GROUP_NEW_MEMBER:
                    this.addGroupMembers((Map<String, Object>) readData.getValue());
                    break;
            }
        } catch (IOException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private void addGroupMembers(Map<String, Object> groupMap) {
        String groupAlias = (String) groupMap.get("alias");
        String creator = (String) groupMap.get("creator");
        List<String> userList = (List<String>) groupMap.get("members");
        List<Integer> userIdList = getUserIdList(userList);
        try {
            this.importGroupMembers(userIdList, groupAlias, creator);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<Integer> getUserIdList(List<String> emailList) {
        List<Integer> userIdList = new ArrayList<>();
        for (String email : emailList) {
            int userId = getUserId(email);
            if (userId != -1) {
                userIdList.add(getUserId(email));
            }
        }
        return userIdList;
    }

    private void importGroupMembers(List<Integer> userIdList, String groupAlias, String creator) throws IOException {
        try {
            PreparedStatement insertGroupMembers = Connector.connect.prepareStatement(
                    "INSERT INTO group_msg (group_id, user_id, is_fav) VALUES (?,?,?)"
            );

            int groupId = getGroupId(groupAlias, getUserId(creator));

            for (Integer userId : userIdList) {
                insertGroupMembers.setInt(1, groupId);
                insertGroupMembers.setInt(2, userId);
                insertGroupMembers.setBoolean(3, false);
            }

            writer.writeObject(Map.entry(State.SUCCESS_ADD_GROUP_NEW_MEMBER, true));
        } catch (SQLException e) {
            writer.writeObject(Map.entry(State.FAIL_ADD_GROUP_NEW_MEMBER, false));
            e.printStackTrace();
        }
    }

    private int getUserId(String email) {
        try {
            PreparedStatement getUserId = Connector.connect.prepareStatement(
                    "SELECT * FROM user_acc WHERE email = ?");

            getUserId.setString(1, email);
            ResultSet resultSet = getUserId.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("user_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private int getGroupId(String alias, int userId) {
        try {
            PreparedStatement getGroupId = Connector.connect.prepareStatement(
                    "SELECT * FROM group_repo WHERE alias = ? AND uid_admin = ?");

            getGroupId.setString(1, alias);
            getGroupId.setInt(2, userId);

            return getGroupId.executeQuery().getInt("group_id");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * TODO: CONVERT THE EMAIL LIST INTO LIST OF UID FROM SQL QUERY
     * This function creates a new group in the database then adds all the given users into it,
     * it then returns a group id to the client for rendering the group name
     * <p>
     * steps
     * 1. add user to group repo (is admin, alias, uid_admin or creator)
     * 3. get the id of that newly created group and insert all members to group_msg
     *
     * @param groupMap a map of the group alias and list of users in it
     */
    @SuppressWarnings("unchecked")
    private void addGroup(Map<String, Object> groupMap) throws IOException {
        String groupAlias = (String) groupMap.get("alias");
        String creator = (String) groupMap.get("creator");
        List<String> userList = (List<String>) groupMap.get("members");
        List<Integer> userIdList = getUserIdList(userList);

        if (doesGroupExists(groupAlias, creator)) {
            return;
        }

        try {
            PreparedStatement insertGroupRepo = Connector.connect.prepareStatement(
                    "INSERT INTO group_repo (is_admin, alias, uid_admin) VALUES (?,?,?)"
            );

            insertGroupRepo.setBoolean(1, false);
            insertGroupRepo.setString(2, groupAlias);
            insertGroupRepo.setInt(3, userIdList.get(0));

            this.importGroupMembers(userIdList, groupAlias, creator);

            writer.writeObject(Map.entry(State.SUCCESS_ADD_GROUP, true));
        } catch (SQLException e) {
            writer.writeObject(Map.entry(State.FAIL_ADD_GROUP, false));
            e.printStackTrace();
        }
    }

    private boolean doesGroupExists(String groupAlias, String groupCreator) {
        try {
            PreparedStatement findGroup = Connector.connect.prepareStatement(
                    "SELECT * FROM group_repo WHERE alias = ? AND uid_admin = ?"
            );

            findGroup.setString(1, groupAlias);
            findGroup.setInt(2, Integer.parseInt(groupCreator));

            if (!findGroup.executeQuery().wasNull()) {
                writer.writeObject(Map.entry(State.FAIL_ADD_FAVOURITE, false));
            }
            return true;
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void addUserFavouriteGroup(Map.Entry<String, String> faveRepo) throws IOException {
        String userId = faveRepo.getKey();
        String groupId = faveRepo.getValue();

        try {
            // TODO: find the group id of the given data first
            PreparedStatement preparedStatement = Connector.connect.prepareStatement(
                    "UPDATE group_msg SET is_fav = 1 where user_id = ? AND group_id = ?"
            );

            preparedStatement.setString(1, userId);
            preparedStatement.setString(2, groupId);

            preparedStatement.executeUpdate();

            writer.writeObject(Map.entry(State.SUCCESS_ADD_FAVOURITE, true));
        } catch (SQLException e) {
            writer.writeObject(Map.entry(State.FAIL_ADD_FAVOURITE, false));
            e.printStackTrace();
        }
    }

    private void broadcastMessage(Map<String, String> messageRepo) {
        try {
            PreparedStatement messageInput = Connector.connect.prepareStatement(
                    "INSERT INTO messenger.message VALUES (?, ?, ?, ?)"
            );

            String sender = messageRepo.get("userId");
            String recipient = messageRepo.get("groupId");
            String message = messageRepo.get("messageSent");
            Timestamp timeSent = Timestamp.valueOf(messageRepo.get("timeSent"));

            messageInput.setString(1, sender);
            messageInput.setString(1, recipient);
            messageInput.setString(1, message);
            messageInput.setTimestamp(1, timeSent);

            messageInput.executeUpdate();

            SERVER.broadcast(sender, message, recipient);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // TODO: Register the student into admin braodcast group
    private void registerUser(RegisterHandler registerHandler, Map<String, String> userRepo) throws IOException {
        boolean registerAttempt = registerHandler.registerUser(
                new PasswordAuthentication(userRepo.get("email"), userRepo.get("password").toCharArray()),
                userRepo.get("firstName"), userRepo.get("lastName"));

        // send a boolean back to the client that identifies if the register attempt is a success or not
        // if not, tell the user to try again later.
        if (registerAttempt) {
            writer.writeObject(Map.entry(State.SUCCESS_REGISTER_USER, true));
        } else {
            writer.writeObject(Map.entry(State.FAIL_REGISTER_USER, false));
        }
    }

    private void loginAuthentication(LoginHandler loginHandler, PasswordAuthentication userAuth) throws IOException, SQLException {
        Optional<ResultSet> userLoginRepo = loginHandler.loginUser(userAuth);
        if (userLoginRepo.isPresent()) {
            // TODO: if userRepo exists then load all of the data that needs to be rendered and send it to the client
            //  data includes: contacts (groups), messages in each group, user account information
            Map<String, String> userRepo = new HashMap<>();
            ResultSet resultSet = userLoginRepo.get();
            String email = resultSet.getString("email");
            String firstName = resultSet.getString("user_fname");
            String lastName = resultSet.getString("user_lname");

            userRepo.put("email", email);
            userRepo.put("firstName", firstName);
            userRepo.put("lastName", lastName);

            // send data back to client
            writer.writeObject(Map.entry(State.SUCCESS_LOGIN_USER, userRepo));

            // TODO:after sending the data into the client, set this thread into the current user's information containing
            //  the data for the group lists, user info, and all of the messages connected to them
            this.user = new User(resultSet.getInt("user_id"), email, firstName, lastName);
        } else {
            writer.writeObject(Map.entry(State.FAIL_LOGIN_USER, false)); // if userRepo does not have any value in it, return false (login failed)
        }
    }

    /**
     * This is used to send the message from each user to the client. This is a helper method for the {@link ChatServer}
     * class
     *
     * @param message message to send
     * @param address who receives the message
     */
    protected void sendMessage(String message, String address) {
        try {
            writer.writeObject(Map.entry(address, message)); // send message to the server
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected User getUser() {
        return this.user;
    }
}
