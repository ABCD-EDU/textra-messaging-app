package preproject.backend;

import preproject.backend.handlers.LoginHandler;
import preproject.backend.handlers.RegisterHandler;
import preproject.backend.models.User;

import java.io.*;
import java.net.PasswordAuthentication;
import java.net.Socket;
import java.sql.*;
import java.util.*;

import static preproject.backend.State.*;

/**
 * This class runs a session for the server. Where each {@code UserThread} is considered as one session.
 * The server will continually accept the ongoing connection to the socket as much as possible which leads to
 * multiple users accessing in one client.
 */
public class UserThread extends Thread {
    private final Socket SOCKET;
    private final ChatServer SERVER;
    private ObjectOutputStream writer;
    private User user;

    public UserThread(Socket socket, ChatServer server) {
        this.SOCKET = socket;
        this.SERVER = server;
    }

    @Override
    public void run() {
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
                case Action.GET_VERIFIED_USERS:
                    this.getVerifiedUsers();
                case Action.GET_UNVERIFIED_USERS:
                    this.getUnverifiedUsers();
                case Action.ADD_VERIFIED_USERS:
                    this.addVerifiedUsers((List<Integer>) readData.getValue());
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

            writer.writeObject(Map.entry(SUCCESS_ADD_GROUP_NEW_MEMBER, true));
        } catch (SQLException e) {
            writer.writeObject(Map.entry(FAIL_ADD_GROUP_NEW_MEMBER, false));
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

            writer.writeObject(Map.entry(SUCCESS_ADD_GROUP, true));
        } catch (SQLException e) {
            writer.writeObject(Map.entry(FAIL_ADD_GROUP, false));
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
                writer.writeObject(Map.entry(FAIL_ADD_FAVOURITE, false));
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

            writer.writeObject(Map.entry(SUCCESS_ADD_FAVOURITE, true));
        } catch (SQLException e) {
            writer.writeObject(Map.entry(FAIL_ADD_FAVOURITE, false));
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
            writer.writeObject(Map.entry(SUCCESS_REGISTER_USER, true));
        } else {
            writer.writeObject(Map.entry(FAIL_REGISTER_USER, false));
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
            writer.writeObject(Map.entry(SUCCESS_LOGIN_USER, userRepo));

            // TODO:after sending the data into the client, set this thread into the current user's information containing
            //  the data for the group lists, user info, and all of the messages connected to them
            this.user = new User(resultSet.getInt("user_id"), email, firstName, lastName);
        } else {
            writer.writeObject(Map.entry(FAIL_LOGIN_USER, false)); // if userRepo does not have any value in it, return false (login failed)
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

    /**
     * This is used to send a list of verified users from the database to the admin panel
     */
    private void getVerifiedUsers(){
        List<User> verifiedUsers = getUsersForAdmin(1);
        try {
            writer.writeObject(Map.entry(SUCCESS_GET_VERIFIED_USERS, verifiedUsers));
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * This is used to send a list of verified users from the database to the admin panel
     */
    private void getUnverifiedUsers(){
        List<User> unVerifiedUsers = getUsersForAdmin(0);
        try {
            writer.writeObject(Map.entry(SUCCESS_GET_UNVERIFIED_USERS, unVerifiedUsers));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    /**
     * This is a helper method for each getVerified and getUnverified methods. It gets called by either of the 2 methods mentioned
     * and returns a list of users where they could be either verified or not depending on the parameter.
     * @param verifiedOrUnverified is an integer value that stands as a boolean where 1 is considered true and 0 false.
     * @return users that falls under the parameter provided.
     */
    private List<User> getUsersForAdmin(int verifiedOrUnverified){
        try {
            //Declare SQL statement that gets the necessary columns needed for building User model
            String SQL = "SELECT user_id, email, user_fname, user_lname WHERE verified ="+Integer.toString(verifiedOrUnverified);
            PreparedStatement statement = Connector.connect.prepareStatement(SQL);
            ResultSet resultSet = statement.executeQuery();

            List<User> users = new ArrayList<>();
            while (resultSet.next()){
                int userId = resultSet.getInt("user_id");
                String email = resultSet.getString("email");
                String firstName = resultSet.getString("user_fname");
                String lastName = resultSet.getString("user_lname");
                //Add the users with verified status to the list which will be returned to the admin client
                users.add(new User(userId,email,firstName,lastName));
            }
            return users;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Adds the users that have been verified by the admin
     * @param newlyVerifiedIDs the user ID of users that have been verified
     */
    public void addVerifiedUsers(List<Integer> newlyVerifiedIDs){
        try {
            //Declare SQL statement that changes the verified status of unregistered users
            String SQL = "UPDATE messenger.user_acc SET (verified) WHERE (user_id)"+"VALUES(?,?)";
            PreparedStatement statement = Connector.connect.prepareStatement(SQL);

            for (Integer id: newlyVerifiedIDs){
                statement.setString(1, "1"); //Assign the value one to the verified column to verify a user
                statement.setInt(2, id);
                //Add the statement to the batch of statements to be executed
                statement.addBatch();
            }
            statement.executeBatch();
            writer.writeObject(Map.entry(SUCCESS_ADD_VERIFIED_USERS, true));
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    protected User getUser() {
        return this.user;
    }
}
