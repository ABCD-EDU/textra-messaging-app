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
                this.getUsersForAdmin(true);
                break;
            case Action.GET_UNVERIFIED_USERS:
                this.getUsersForAdmin(false);
                break;
            case Action.POST_VERIFIED_USERS:
                this.updateVerifiedUsers((String)readData.get("email"), (String)readData.get("isVerified"));
                break;
            case Action.ACCEPT_ALL_USERS:
                this.updateALlRegistrations(true);
                break;
            case Action.DECLINE_ALL_USERS:
                this.updateALlRegistrations(false);
                break;
            case Action.GET_GROUP_LIST:
                this.writeGroupList();
                break;
            case Action.GET_USER_INFORMATION:
                this.writeUserInformation((String) readData.get("email"));
                break;
            case Action.GET_GROUP_MEMBERS:
                this.getGroupMembersList((String) readData.get("groupId"));
                break;
            case Action.GET_GROUP_MESSAGES:
                this.getGroupMessages((String) readData.get("groupId"));
                break;
        }
    }

    private void updateALlRegistrations(boolean isVerified) throws IOException {
        try {
            if (isVerified) {
                PreparedStatement updateStatement = Connector.connect.prepareStatement(
                        "UPDATE user_acc SET verified = ? WHERE verified = ?");
                updateStatement.setBoolean(1, false);
                updateStatement.setBoolean(2, true); //Verify unverified users
                updateStatement.executeUpdate();
            } else {
                PreparedStatement preparedStatement = Connector.connect.prepareStatement(
                        "DELETE FROM user_acc WHERE verified =?");
                preparedStatement.setBoolean(1, false);
                preparedStatement.executeUpdate();
            }
            objOut.writeObject(true);
            return;
        }catch(Exception e){
            e.printStackTrace();
        }
        objOut.writeObject(false);
    }

    private void updateVerifiedUsers(String email, String isVerified) throws IOException {
        Map<String, Boolean> writeObject= new HashMap<>();
        try {
            if (Boolean.parseBoolean(isVerified)) {
                PreparedStatement preparedStatement = Connector.connect.prepareStatement(
                        "UPDATE user_acc SET verified = ? WHERE user_id = ?");
                preparedStatement.setBoolean(1, true);
                preparedStatement.setInt(2, getUserId(email));
                preparedStatement.executeUpdate();
            }else {
                PreparedStatement preparedStatement = Connector.connect.prepareStatement(
                        "DELETE FROM user_acc WHERE user_id = ?");
                preparedStatement.setInt(1, getUserId(email));
                preparedStatement.executeUpdate();
            }
            writeObject.put(SUCCESS_POST_VERIFIED_USERS,true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        writeObject.put(SUCCESS_POST_VERIFIED_USERS,true);
        objOut.writeObject(writeObject);
    }

    private void getUsersForAdmin(boolean isVerified) {
        try {
            PreparedStatement preparedStatement = Connector.connect.prepareStatement(
                    "SELECT * FROM user_acc WHERE verified = ?"
            );
            preparedStatement.setBoolean(1, isVerified);

            ResultSet resultSet = preparedStatement.executeQuery();
//            System.out.println("test");

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
                mapRepo.put("isVerified", String.valueOf(resultSet.getBoolean("verified")));
                mapRepo.put("isAdmin", isAdmin);

                userRepo.add(mapRepo);
//                System.out.println("SIZE OF REPO " + userRepo.size());
            }

            objOut.writeObject(userRepo);
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

            insertGroupRepo.executeUpdate();

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

            return true;
        } catch (SQLException e) {
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
            ResultSet resultSet = userLoginRepo.get();
            String email = resultSet.getString("email");
            String firstName = resultSet.getString("user_fname");
            String lastName = resultSet.getString("user_lname");

            // send data back to client
            objOut.writeObject(true);

            // TODO:after sending the data into the client, set this thread into the current user's information containing
            //  the data for the group lists, user info, and all of the messages connected to them
            this.user = new User(resultSet.getInt("user_id"), email, firstName, lastName);
        } else {
            objOut.writeObject(false); // if userRepo does not have any value in it, return false (login failed)
        }
    }

    private void writeUserInformation(String email) {
        try {
            PreparedStatement preparedStatement = Connector.connect.prepareStatement(
                    "SELECT * FROM user_acc WHERE email = ?"
            );

            preparedStatement.setString(1, email);

            ResultSet resultSet = preparedStatement.executeQuery();
            // TODO: if userRepo exists then load all of the data that needs to be rendered and send it to the client
            //  data includes: contacts (groups), messages in each group, user account information
            Map<String, String> userRepo = new HashMap<>();
            String userId = String.valueOf(resultSet.getInt("user_id"));
            String firstName = resultSet.getString("user_fname");
            String lastName = resultSet.getString("user_lname");
            String isVerified = String.valueOf(resultSet.getBoolean("verified"));
            String isAdmin = String.valueOf(resultSet.getBoolean("is_admin"));

            userRepo.put("userId", userId);
            userRepo.put("email", email);
            userRepo.put("firstName", firstName);
            userRepo.put("lastName", lastName);
            userRepo.put("isVerified", isVerified);
            userRepo.put("isAdmin", isAdmin);

            objOut.writeObject(userRepo);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    private void writeGroupList() {
        try {
            List<Map<String, String>> groupList = getGroupList();
            objOut.writeObject(groupList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<Map<String, String>> getGroupList() {
        List<Map<String, String>> groupList = new ArrayList<>();

        try {
            PreparedStatement preparedStatement = Connector.connect.prepareStatement(
                    "SELECT * FROM group_repo"
            );

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String groupId = String.valueOf(resultSet.getInt("group_id"));
                String isAdmin = String.valueOf(resultSet.getBoolean("is_admin"));
                String alias = resultSet.getString("alias");
                Timestamp timeSent = resultSet.getTimestamp("time_sent");

                Map<String, String> groupMap = new HashMap<>();
                groupMap.put("groupId", groupId);
                groupMap.put("isAdmin", isAdmin);
                groupMap.put("alias", alias);
                groupMap.put("timeSent", String.valueOf(timeSent));

                groupList.add(groupMap);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return groupList;
    }

    private void getGroupMembersList(String groupId) {
        List<Map<String, String>> memberRepo = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = Connector.connect.prepareStatement(
                    "SELECT user_id FROM group_msg WHERE group_id = ?"
            );

            preparedStatement.setInt(1, Integer.parseInt(groupId));

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String userId = resultSet.getString("user_id");
                Optional<ResultSet> resultSetOptional = queryUserInformation(userId);

                if (resultSetOptional.isPresent()) {
                    ResultSet userResult = resultSetOptional.get();
                    Map<String, String> userMap = new HashMap<>();
                    userMap.put("userId", userId);
                    userMap.put("email", userResult.getString("email"));
                    userMap.put("firstName", userResult.getString("user_fname"));
                    userMap.put("lastName", userResult.getString("user_lname"));

                    memberRepo.add(userMap);
                }
            }
            objOut.writeObject(memberRepo);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    private void getGroupMessages(String groupId) {
        List<Map<String, String>> groupMsgRepo = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = Connector.connect.prepareStatement(
                    "SELECT * FROM message WHERE group_id = ?"
            );
            preparedStatement.setInt(1, Integer.parseInt(groupId));

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Map<String, String> msgRepo = new HashMap<>();
                msgRepo.put("sender", resultSet.getString("from_user"));
                msgRepo.put("groupId", groupId);
                msgRepo.put("message", resultSet.getString("message"));
                msgRepo.put("timeSent", String.valueOf(resultSet.getTimestamp("time_sent")));
                groupMsgRepo.add(msgRepo);
            }
            objOut.writeObject(groupMsgRepo);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    private Optional<ResultSet> queryUserInformation(String userId) {
        try {
            PreparedStatement preparedStatement = Connector.connect.prepareStatement(
                    "SELECT email,user_fname,user_lname FROM user_acc WHERE user_id = ?"
            );

            preparedStatement.setInt(1, Integer.parseInt(userId));

            return Optional.ofNullable(preparedStatement.executeQuery());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    /**
     * This is used to send the message from each user to the client. This is a helper method for the {@link ChatServer}
     * class
     *
     * @param message message to send
     * @param address who receives the message
     */
    protected void sendMessage(String sender, String address, String message, Timestamp timeSent) {
        try {
            Map<String, String> messageRepo = new HashMap<>();
            messageRepo.put("sender", sender);
            messageRepo.put("address", address);
            messageRepo.put("message", message);
            messageRepo.put("timeSent", timeSent.toString());
            objOut.writeObject(messageRepo); // send message to the server
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected User getUser() {
        return this.user;
    }
}