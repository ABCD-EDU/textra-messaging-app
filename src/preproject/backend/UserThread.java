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
import static preproject.backend.State.SUCCESS_POST_VERIFIED_USERS;

/**
 * This class runs a session for the server. Where each {@code UserThread} is considered as one session.
 * The server will continually accept the ongoing connection to the socket as much as possible which leads to
 * multiple users accessing in one client.
 */
public class UserThread extends Thread {
    private final Socket SOCKET;
    private final ChatServer SERVER;
    private ObjectOutputStream objOut;
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
            objOut = new ObjectOutputStream(output);

            // constantly receive data being sent and process it
            SOCKET.setKeepAlive(true);
            while (SOCKET.isConnected()) {
                processReadData(reader);
            }

        } catch (IOException | SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private void processReadData(ObjectInputStream reader) throws IOException, SQLException, ClassNotFoundException {

        HashMap<String, Object> readData = (HashMap<String, Object>) reader.readObject();
        switch (String.valueOf(readData.get("action"))) {
            case Action.LOGIN_USER:
                this.loginAuthentication(new LoginHandler(), readData);
                break;
            case Action.LOGOUT_USER:
                this.SOCKET.close(); // close the socket when user logouts
                break;
            case Action.REGISTER_USER:
                this.registerUser(new RegisterHandler(), readData);
                break;
            case Action.SEND_MESSAGE:
                this.broadcastMessage((Map<String, String>) readData.get("messageRepo")
                );
                break;
            case Action.ADD_FAVOURITE:
                this.addUserFavouriteGroup((Map<String, String>) readData.get("favRepo"));
                break;
            case Action.ADD_GROUP:
                this.addGroup((Map<String, Object>) readData.get("groupRepo"));
                break;
            case Action.ADD_GROUP_NEW_MEMBER:
                this.addGroupMembers((Map<String, Object>) readData.get("membersRepo"));
                break;
            case Action.GET_VERIFIED_USERS:
                this.getUsersForAdmin(1);
                break;
            case Action.GET_UNVERIFIED_USERS:
                this.getUsersForAdmin(0);
                break;
            case Action.POST_VERIFIED_USERS:
                this.updateVerifiedUsers((String)readData.get("email"), (String)readData.get("isVerified"));
                break;
            case Action.ACCEPT_ALL_USERS:
                this.updateALlRegistrations(1);
                break;
            case Action.DECLINE_ALL_USERS:
                this.updateALlRegistrations(0);
        }
    }

    private void updateALlRegistrations(int isVerified){
        try {
            if (isVerified == 1) {

                PreparedStatement updateStatement = Connector.connect.prepareStatement(
                        "UPDATE user_acc SET verified = ? WHERE verified=?");
                updateStatement.setInt(1, 1);
                updateStatement.setInt(2, 0); //Verify unverified users
                updateStatement.executeUpdate();

                PreparedStatement selectStatement= Connector.connect.prepareStatement("SELECT * FROM user_acc WHERE verified=1");
                ResultSet resultSet = selectStatement.executeQuery();

                Map<String, List<Map<String, String>>> writeObject = new HashMap<>(); // write back users that have been
                List<Map<String, String>> userRepo = new ArrayList<>(); // list of userRepo

                while (resultSet.next()) {

                    Map<String, String> mapRepo = new HashMap<>();
                    String userId = String.valueOf(resultSet.getInt("user_id"));
                    String email = resultSet.getString("email");
                    String firstName = resultSet.getString("user_fname");
                    String lastName = resultSet.getString("user_lname");
                    String isAdmin = String.valueOf(resultSet.getBoolean("is_admin"));

                    mapRepo.put("userId", userId);
                    mapRepo.put("email", email);
                    mapRepo.put("firstName", firstName);
                    mapRepo.put("lastName", lastName);
                    mapRepo.put("isVerified", String.valueOf(1));
                    mapRepo.put("isAdmin", isAdmin);
                    userRepo.add(mapRepo);
                }
                writeObject.put(SUCCESS_ACCEPT_ALL_USERS, userRepo);
                objOut.writeObject(writeObject);
            } else {
                PreparedStatement preparedStatement = Connector.connect.prepareStatement(
                        "DELETE FROM user_acc WHERE verified =?");
                preparedStatement.setInt(1, isVerified);
                preparedStatement.executeUpdate();
                Map<String, List<Map<String, String>>> writeObject = new HashMap<>();
                List<Map<String, String>> nullUsers = new ArrayList<>();
                writeObject.put(SUCCESS_DECLINE_ALL_USERS, nullUsers);
                objOut.writeObject(writeObject);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void updateVerifiedUsers(String email, String isVerified) {

        try {
            Map<String, Boolean> writeObject= new HashMap<>();
            if (Integer.parseInt(isVerified)==1) {
                PreparedStatement preparedStatement = Connector.connect.prepareStatement(
                        "UPDATE user_acc SET verified = ? WHERE user_id = ?");
                preparedStatement.setInt(1, Integer.parseInt(isVerified));
                preparedStatement.setInt(2, getUserId(email));
                preparedStatement.executeUpdate();
                writeObject.put(SUCCESS_POST_VERIFIED_USERS,true);
            }else {
                PreparedStatement preparedStatement = Connector.connect.prepareStatement(
                        "DELETE FROM user_acc WHERE user_id = ?");
                preparedStatement.setInt(1, getUserId(email));
                preparedStatement.executeUpdate();
                writeObject.put(SUCCESS_POST_VERIFIED_USERS,false);
            }
            objOut.writeObject(writeObject);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getUsersForAdmin(int isVerified) {
        try {
            PreparedStatement preparedStatement = Connector.connect.prepareStatement(
                    "SELECT * FROM user_acc WHERE verified = ?"
            );
            preparedStatement.setInt(1, isVerified);

            ResultSet resultSet = preparedStatement.executeQuery();
//            System.out.println("test");

            Map<String, List<Map<String,String>>> writeObject = new HashMap<>(); // write back
            List<Map<String, String>> userRepo = new ArrayList<>(); // list of userRepo
            while (resultSet.next()) {
                Map<String, String> mapRepo = new HashMap<>();
                String userId = String.valueOf(resultSet.getInt("user_id"));
                String email = resultSet.getString("email");
                String firstName = resultSet.getString("user_fname");
                String lastName = resultSet.getString("user_lname");
                String isAdmin = String.valueOf(resultSet.getBoolean("is_admin"));

                mapRepo.put("userId", userId);
                mapRepo.put("email", email);
                mapRepo.put("firstName", firstName);
                mapRepo.put("lastName", lastName);
                mapRepo.put("isVerified", String.valueOf(isVerified));
                mapRepo.put("isAdmin", isAdmin);

                userRepo.add(mapRepo);
//                System.out.println("SIZE OF REPO " + userRepo.size());
            }

            if (isVerified == 1) {
                writeObject.put(SUCCESS_GET_VERIFIED_USERS, userRepo);
            } else {
                writeObject.put(SUCCESS_GET_UNVERIFIED_USERS, userRepo);
            }
            objOut.writeObject(writeObject);
        } catch (SQLException | IOException e) {
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

            objOut.writeObject(new HashMap<String, Boolean>().put(SUCCESS_ADD_GROUP_NEW_MEMBER, true));
        } catch (SQLException e) {
            objOut.writeObject(new HashMap<String, Boolean>().put(FAIL_ADD_GROUP_NEW_MEMBER, false));
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

            objOut.writeObject(new HashMap<String, Boolean>().put(SUCCESS_ADD_GROUP, true));
        } catch (SQLException e) {
            objOut.writeObject(new HashMap<String, Boolean>().put(FAIL_ADD_GROUP, false));
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
                objOut.writeObject(new HashMap<String, Boolean>().put(FAIL_ADD_FAVOURITE, false));
            }
            return true;
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void addUserFavouriteGroup(Map<String, String> faveRepo) throws IOException {
        String userId = faveRepo.get("userId");
        String groupId = faveRepo.get("groupId");

        try {
            // TODO: find the group id of the given data first
            PreparedStatement preparedStatement = Connector.connect.prepareStatement(
                    "UPDATE group_msg SET is_fav = 1 where user_id = ? AND group_id = ?"
            );

            preparedStatement.setString(1, userId);
            preparedStatement.setString(2, groupId);

            preparedStatement.executeUpdate();

            objOut.writeObject(new HashMap<String, Boolean>().put(SUCCESS_ADD_FAVOURITE, true));
        } catch (SQLException e) {
            objOut.writeObject(new HashMap<String, Boolean>().put(FAIL_ADD_FAVOURITE, false));
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
    private void registerUser(RegisterHandler registerHandler, HashMap<String, Object> userRepo) throws IOException {
        boolean registerAttempt = registerHandler.registerUser(
                new PasswordAuthentication((String) userRepo.get("email"), ((String)userRepo.get("password")).toCharArray()),
                (String)userRepo.get("firstName"), (String)userRepo.get("lastName"));

        // send a boolean back to the client that identifies if the register attempt is a success or not
        // if not, tell the user to try again later.
        if (registerAttempt) {
            objOut.writeObject(new HashMap<String, Boolean>().put(SUCCESS_REGISTER_USER, true));
        } else {
            objOut.writeObject(new HashMap<String, Boolean>().put(FAIL_REGISTER_USER, false));
        }
    }

    private void loginAuthentication(LoginHandler loginHandler, HashMap<String, Object> userAuth) throws IOException, SQLException {
        System.out.println(userAuth.get("email") + " " + userAuth.get("password"));
        Optional<ResultSet> userLoginRepo = loginHandler.loginUser(new PasswordAuthentication((String) userAuth.get("email"), ((String) userAuth.get("password")).toCharArray()));
        if (userLoginRepo.isPresent()) {
            // TODO: if userRepo exists then load all of the data that needs to be rendered and send it to the client
            //  data includes: contacts (groups), messages in each group, user account information
            Map<String, String> userRepo = new HashMap<>();
            ResultSet resultSet = userLoginRepo.get();
            String userId = String.valueOf(resultSet.getInt("user_id"));
            String email = resultSet.getString("email");
            String firstName = resultSet.getString("user_fname");
            String lastName = resultSet.getString("user_lname");
            String isVerified = String.valueOf(resultSet.getBoolean("verified"));
            String isAdmin = String.valueOf(resultSet.getBoolean("is_admin"));

            userRepo.put(SUCCESS_LOGIN_USER, String.valueOf(true));
            userRepo.put("userId", userId);
            userRepo.put("email", email);
            userRepo.put("firstName", firstName);
            userRepo.put("lastName", lastName);
            userRepo.put("isVerified", isVerified);
            userRepo.put("isAdmin", isAdmin);

            // send data back to client
            objOut.writeObject(userRepo);

            // TODO:after sending the data into the client, set this thread into the current user's information containing
            //  the data for the group lists, user info, and all of the messages connected to them
            this.user = new User(resultSet.getInt("user_id"), email, firstName, lastName);
        } else {
            objOut.writeObject(new HashMap<String, Boolean>().put(FAIL_LOGIN_USER, false)); // if userRepo does not have any value in it, return false (login failed)
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
            objOut.writeObject(new HashMap<String, String>().put(address, message)); // send message to the server
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected User getUser() {
        return this.user;
    }
}