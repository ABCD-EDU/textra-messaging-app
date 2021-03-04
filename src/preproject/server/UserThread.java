package preproject.server;

import preproject.server.handlers.LoginHandler;
import preproject.server.handlers.RegisterHandler;
import preproject.server.models.User;

import java.io.*;
import java.net.PasswordAuthentication;
import java.net.Socket;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

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
        try (
                InputStream input = SOCKET.getInputStream();
                OutputStream output = SOCKET.getOutputStream();
                ObjectInputStream reader = new ObjectInputStream(input)
        ) {
            objOut = new ObjectOutputStream(output);

            // constantly receive data being sent and process it
//            SOCKET.setKeepAlive(true);
            while (!SOCKET.isClosed()) {
                try {
                processReadData(reader);
                } catch (EOFException e) {
                    System.out.println("There is no data to read");
                    SOCKET.close();
                    break;
                }
            }

            if (SOCKET.isClosed()) {
                SERVER.removeUser(this);
                objOut.close();
                System.out.println("SOCKET closed successfully");
            }
        } catch (IOException | SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private void processReadData(ObjectInputStream reader) throws IOException, SQLException, ClassNotFoundException {
        HashMap<String, Object> readData = (HashMap<String, Object>) reader.readObject();
        System.out.println("============ NEW ACTION ============ USER: ");
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
                this.broadcastMessage(readData);
                break;
            case Action.ADD_FAVOURITE:
                this.toggleFavorite(readData);
                break;
            case Action.ADD_GROUP:
                this.addGroup(readData);
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
                this.updateVerifiedUsers((String) readData.get("email"), (String) readData.get("isVerified"));
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
                this.writeUserInformation(this.user.getEmail());
                break;
            case Action.GET_GROUP_MEMBERS:
                System.out.println("GETTING GROUP MEMBERS");
                this.getGroupMembersList((String) readData.get("groupId"));
                break;
            case Action.GET_GROUP_MESSAGES:
                this.getGroupMessages((String) readData.get("groupId"));
                break;
            case Action.REMOVE_A_MEMBER:
                this.removeUser((String)readData.get("email"), (String) readData.get("groupId"));
                break;
            case Action.GET_SEARCHED_USERS:

            case Action.SEND_BROADCAST_MESSAGE:
                System.out.println(readData.keySet());
                SERVER.broadcastMessageToAllOnlineUsers((List<Map<String, String>>) readData.get("messagesList"),
                        (String) readData.get("senderId"));
                break;
        }
    }

    private List<Map<String, String>> getUnreadMessages(int id) throws IOException {
        List<Map<String, String>> unreadRepo = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = Connector.connect.prepareStatement(
                    "SELECT group_id, COUNT(`group_id`) as unread_messages, user_id " +
                            "FROM unread_msg GROUP BY group_id HAVING user_id = ?"
            );

            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Map<String, String> unreadMap = new HashMap<>();
                unreadMap.put("groupId", resultSet.getString("group_id"));
                unreadMap.put("unreadMessages", resultSet.getString("unread_messages"));
                unreadRepo.add(unreadMap);
            }
            preparedStatement = Connector.connect.prepareStatement(
              "DELETE FROM `unread_msg` WHERE user_id = ?"
            );
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return unreadRepo;
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        objOut.writeObject(false);
    }

    private void updateVerifiedUsers(String email, String isVerified) throws IOException {
        Map<String, Boolean> writeObject = new HashMap<>();
        try {
            PreparedStatement preparedStatement;
            if (Boolean.parseBoolean(isVerified)) {
                preparedStatement = Connector.connect.prepareStatement(
                        "UPDATE user_acc SET verified = ? WHERE user_id = ?");
                preparedStatement.setBoolean(1, true);
                preparedStatement.setInt(2, getUserId(email));
            } else {
                preparedStatement = Connector.connect.prepareStatement(
                        "DELETE FROM user_acc WHERE user_id = ?");
                preparedStatement.setInt(1, getUserId(email));
            }
            preparedStatement.executeUpdate();
            writeObject.put("response", true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        writeObject.put("response", true);
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

    // TODO: hook up with SERVER groupList update
    @SuppressWarnings("unchecked")
    private void addGroupMembers(Map<String, Object> groupMap) {
        System.out.println("ADD GROUP MEMBER");
        String groupAlias = (String) groupMap.get("alias");
        String creator = (String) groupMap.get("creator");
        List<String> userList = (List<String>) groupMap.get("members");
        List<Integer> userIdList = getUserIdList(userList);
        userIdList.forEach((id) ->
                SERVER.updateGroupList(
                        String.valueOf(getGroupId(groupAlias, Integer.parseInt(creator))),
                        id.toString()));
        try {
            this.importGroupMembers(userIdList, groupAlias, creator);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<Integer> getUserIdList(List<String> emailList) {
        System.out.println("=== getUserIDList");
        List<Integer> userIdList = new ArrayList<>();
        for (String email : emailList) {
            System.out.println("email: " + email);
            int userId = getUserId(email);
            if (userId != -1) {
                userIdList.add(getUserId(email));
            }
        }
        return userIdList;
    }

    /**
     * @param userIdList list of user ID's
     * @param groupAlias name of the group
     * @param creator    creator's email
     * @throws IOException in case read/write error occurs
     */
    private void importGroupMembers(List<Integer> userIdList, String groupAlias, String creator) throws IOException {
        System.out.println("==== Import group members");
        System.out.println("creator: " + creator);
        System.out.println("UserIDLIST " + userIdList);
        System.out.println("Alias " + groupAlias);
        System.out.println("userid(0) " + userIdList.get(0));

        try {
            PreparedStatement insertGroupMembers = Connector.connect.prepareStatement(
                    "INSERT INTO group_msg (group_id, user_id, is_fav) VALUES (?,?,?)"
            );

            int groupId = getGroupId(groupAlias, getUserId(creator));

            for (Integer userId : userIdList) {
                insertGroupMembers.setInt(1, groupId);
                insertGroupMembers.setInt(2, userId);
                insertGroupMembers.setBoolean(3, false);
                insertGroupMembers.executeUpdate();
            }

//            objOut.writeObject(true);
        } catch (SQLException e) {
//            objOut.writeObject(false);
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

    private int getGroupId(String alias, int adminUserId) {
        try {
            PreparedStatement getGroupId = Connector.connect.prepareStatement(
                    "SELECT * FROM group_repo WHERE alias = ? AND uid_admin = ?");

            getGroupId.setString(1, alias);
            getGroupId.setInt(2, adminUserId);

            ResultSet returnedValue = getGroupId.executeQuery();
            if (returnedValue.next()) {
                return returnedValue.getInt("group_id");
            }
//            return getGroupId.executeQuery().getInt("group_id");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
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
    private void addGroup(HashMap<String, Object> groupMap) throws IOException {
        System.out.println("==== add group ====");
        String groupAlias = (String) groupMap.get("alias");
        String creator = (String) groupMap.get("creator");
        List<String> userList = (List<String>) groupMap.get("members");
        userList.add(0, creator);
        List<Integer> userIdList = getUserIdList(userList);
        System.out.println("Alias " + groupMap.get("alias"));
        System.out.println("Users " + userList);
        System.out.println("IDs " + userIdList);
        System.out.println("properties succesfully initialized");
        Map<String, Object> responseMap = new HashMap<>();

        if (doesGroupExists(groupAlias, getUserId(creator))) {
            responseMap.put("action", Action.ON_GROUP_CREATION);
            responseMap.put("status", "false");
            Map<String, String> groupMapToReturn = new HashMap<>();
            responseMap.put("groupMap", groupMapToReturn);
            objOut.writeObject(responseMap);
//            objOut.writeObject(false);
            System.out.println("Group already exists");
            return;
        }

        try {
            PreparedStatement insertGroupRepo = Connector.connect.prepareStatement(
                    "INSERT INTO group_repo (is_admin, alias, uid_admin) VALUES (?,?,?)"
            );

            System.out.println("prepared the query statement");

            insertGroupRepo.setBoolean(1, false);
            insertGroupRepo.setString(2, groupAlias);
            insertGroupRepo.setInt(3, userIdList.get(0));

            System.out.println("creator: " + userIdList.get(0));

            insertGroupRepo.executeUpdate();
            System.out.println("query executed update");

            this.importGroupMembers(userIdList, groupAlias, creator);

            ArrayList<String> stringIdList = userIdList.stream().map(String::valueOf).collect(Collectors.toCollection(ArrayList::new));
            // Update groupList in server
            SERVER.updateGroupList(String.valueOf(getGroupId(groupAlias, userIdList.get(0))), stringIdList);

            // send back to client that the creation is successful
            responseMap.put("action", Action.ON_GROUP_CREATION);
            responseMap.put("status", "true");
            Map<String, String> groupMapToSend = new HashMap<>();
            groupMapToSend.put("alias", groupAlias);
            groupMapToSend.put("uidAdmin", creator);
            groupMapToSend.put("is_fav", "0");
            groupMapToSend.put("groupId", String.valueOf(getGroupId(groupAlias, Integer.parseInt(stringIdList.get(0)))));
            groupMapToSend.put("unreadMessages", "0");
            responseMap.put("groupMap", groupMapToSend);
            objOut.writeObject(responseMap);
            // send to everyone in group excluding user
            SERVER.sendMapToListOfUsers(responseMap, stringIdList, stringIdList.get(0));
            System.out.println("successful group creation");
        } catch (SQLException e) {
            responseMap.put("status", "false");
            objOut.writeObject(responseMap);
            System.out.println("unsuccessful group creation");
            e.printStackTrace();
        }
    }

    private void removeUser(String email, String groupId){
        try {
            HashMap<String,Object> response = new HashMap<>();
            PreparedStatement preparedStatement = Connector.connect.prepareStatement(
              "DELETE FROM group_msg WHERE group_id=? AND user_id=?"
            );

            System.out.println(groupId);
            System.out.println(getUserId(email));
            preparedStatement.setInt(1,Integer.parseInt(groupId));
            preparedStatement.setInt(2, getUserId(email));

            preparedStatement.executeUpdate();

            response.put("action", Action.ON_REMOVE_A_MEMBER);
            response.put("emailRemoved", email);

            objOut.writeObject(response);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * @param groupAlias group name
     * @param groupCreator group creator ID
     * @return if group exists from group_repo table
     */
    private boolean doesGroupExists(String groupAlias, int groupCreator) {
        try {
            PreparedStatement findGroup = Connector.connect.prepareStatement(
                    "SELECT * FROM group_repo WHERE alias = ? AND uid_admin = ?"
            );

            findGroup.setString(1, groupAlias);
            findGroup.setInt(2, groupCreator);

            ResultSet rowsAffected = findGroup.executeQuery();
            if (rowsAffected.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void toggleFavorite(HashMap<String, Object> faveRepo) throws IOException {
        System.out.println("FAVORITE TOGGLED");
        String userId = (String) faveRepo.get("userId");
        String groupId = (String) faveRepo.get("groupId");
        String isFav = (String) faveRepo.get("isFav");

        try {
            PreparedStatement preparedStatement = Connector.connect.prepareStatement(
                    "UPDATE group_msg SET is_fav = ? where user_id = ? AND group_id = ?"
            );

            preparedStatement.setInt(1, Integer.parseInt(isFav));
            preparedStatement.setString(2, userId);
            preparedStatement.setString(3, groupId);

            preparedStatement.executeUpdate();

            Map<String, Object> response = new HashMap<>();
            response.put("action", Action.ON_FAVORITE_TOGGLED);
            response.put("groupId", groupId);
            response.put("isFav", isFav);
            objOut.writeObject(response);
        } catch (SQLException e) {
            System.out.println("FAVORITE TOGGLE UNSUCCESSFUL");
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private void broadcastMessage(HashMap<String, Object> messagesRepo) {
        System.out.println("=== BROADCAST MESSAGE");
        List<Map<String, Object>> messageList = (List<Map<String, Object>>) messagesRepo.get("messagesList");
        for (Map<String, Object> messageRepo : messageList) {
            try {
                PreparedStatement messageInput = Connector.connect.prepareStatement(
                        "INSERT INTO message (from_user, group_id, message, time_sent) VALUES (?, ?, ?, ?)"
                );

                int sender = Integer.parseInt((String) messageRepo.get("userId"));
                String recipient = (String) messageRepo.get("groupId");
                String message = (String) messageRepo.get("messageSent");
                Timestamp timeSent = Timestamp.valueOf((String) messageRepo.get("timeSent"));

                messageInput.setInt(1, sender);
                messageInput.setString(2, recipient);
                messageInput.setString(3, message);
                messageInput.setTimestamp(4, timeSent);

                messageInput.executeUpdate();

                SERVER.broadcast(sender, message, recipient);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void registerUser(RegisterHandler registerHandler, HashMap<String, Object> userRepo) throws IOException {
        boolean registerAttempt = registerHandler.registerUser(
                new PasswordAuthentication((String) userRepo.get("email"), ((String) userRepo.get("password")).toCharArray()),
                (String) userRepo.get("firstName"), (String) userRepo.get("lastName"));

        // send a boolean back to the client that identifies if the register attempt is a success or not
        // if not, tell the user to try again later.
        if (registerAttempt)
            objOut.writeObject(true);
        else
            objOut.writeObject(false);
    }

    private void loginAuthentication(LoginHandler loginHandler, HashMap<String, Object> userAuth) throws IOException, SQLException {
        System.out.println("\"" + userAuth.get("email") + "\" \"" + userAuth.get("password") + "\"");
        Optional<ResultSet> userLoginRepo = loginHandler.loginUser(new PasswordAuthentication((String) userAuth.get("email"), ((String) userAuth.get("password")).toCharArray()));
        if (userLoginRepo.isPresent()) {
            ResultSet resultSet = userLoginRepo.get();
            String email = resultSet.getString("email");
            String firstName = resultSet.getString("user_fname");
            String lastName = resultSet.getString("user_lname");
            boolean isAdmin = resultSet.getBoolean("is_admin");
            boolean verified = resultSet.getBoolean("verified");
            int id = resultSet.getInt("user_id");
            System.out.println("login success id + " + id);

            Map<String, String> userRepo = new HashMap<>();
            userRepo.put("isAdmin", String.valueOf(isAdmin));
            userRepo.put("isVerified", String.valueOf(verified));

            // send data back to client
            objOut.writeObject(true);
            objOut.writeObject(userRepo);
            System.out.println("USER SUCCESSFULLY LOGGED IN");

            if (verified) {
                SERVER.addOnlineUser(String.valueOf(getUserId(email)));
                this.user = new User(id, email, firstName, lastName);
            }
        } else {
            objOut.writeObject(false); // if userRepo does not have any value in it, return false (login failed)
            System.out.println("userRepo no value log in UNSUCCESSFUL");
        }
    }

    private void writeUserInformation(String email) {
        System.out.println("USER INFORMATION REQUEST");
        try {
            PreparedStatement preparedStatement = Connector.connect.prepareStatement(
                    "SELECT * FROM user_acc WHERE email = ?"
            );

            preparedStatement.setString(1, email);
            System.out.println("email: " + email);

            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
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

            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("action", Action.ON_USER_INFO_SEND);
            responseMap.put("data", userRepo);

            objOut.writeObject(responseMap);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * TODO: Only return groups that the user requesting is part of
     */
    private void writeGroupList() {
        System.out.println("GET GROUP LIST REQUEST");
        try {
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("action", Action.ON_GROUP_LIST_SEND);
            System.out.println("responseMap created");
            List<Map<String, String>> groupList = getGroupList();
            responseMap.put("data", groupList);
            responseMap.put("unreadMessages", getUnreadMessages(this.user.getUserId()));
            System.out.println("groupList added to responseMap");
            objOut.writeObject(responseMap);
            System.out.println("group list returned");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Return a list of groups the client is a member of.
     * <p>
     * Algorithm:
     * 1. Get group_ids the client is part of in group_msg table
     * 2. Get rows corresponding to those group_id's in the group_repo table
     */
    private List<Map<String, String>> getGroupList() {
        List<Map<String, String>> groupList = new ArrayList<>();
        try {

            // get group_ids the client is part of
            PreparedStatement getGroup_idsStatement = Connector.connect.prepareStatement(
                    "SELECT group_id, is_fav FROM group_msg WHERE user_id = ?"
            );
            getGroup_idsStatement.setInt(1, this.user.getUserId());
            ResultSet group_idsStatement = getGroup_idsStatement.executeQuery();

            // get group info of those group_ids and add each of them to groupList
            while (group_idsStatement.next()) {
                PreparedStatement getGroupInfoStatement = Connector.connect.prepareStatement(
                        "SELECT * FROM group_repo WHERE group_id = ?"
                );
                getGroupInfoStatement.setInt(1, group_idsStatement.getInt("group_id"));
                ResultSet resultSet = getGroupInfoStatement.executeQuery();

                resultSet.next();
                String groupId = String.valueOf(resultSet.getInt("group_id"));
                String isAdmin = String.valueOf(resultSet.getBoolean("is_admin"));
                String alias = resultSet.getString("alias");
                String uidAdmin = String.valueOf(resultSet.getInt("uid_admin"));

                Map<String, String> groupMap = new HashMap<>();
                groupMap.put("groupId", groupId);
                groupMap.put("isAdmin", isAdmin);
                groupMap.put("alias", alias);
                groupMap.put("uidAdmin", uidAdmin);
                // TODO: Update this when messages table is already functional
                groupMap.put("unreadMessages", "0");
                groupMap.put("is_fav", String.valueOf(group_idsStatement.getInt("is_fav")));

                groupList.add(groupMap);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return groupList;
    }

    private void getGroupMembersList(String groupId) {
        HashMap<String,Object> response = new HashMap<>();
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
                    if ( userResult.next()) {
                        userMap.put("userId", userId);
                        userMap.put("email", userResult.getString("email"));
                        userMap.put("firstName", userResult.getString("user_fname"));
                        userMap.put("lastName", userResult.getString("user_lname"));
                    }
                    memberRepo.add(userMap);
                }
            }
            response.put("action", Action.ON_GROUP_MEMBERS_SEND);
            response.put("members", memberRepo);
            objOut.writeObject(response);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public void getSearchedUsers(String searchQuery){
        try {
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * @param groupId id of the specific group
     */
    private void getGroupMessages(String groupId) {
        System.out.println("GET INITIAL GROUP MESSAGES");
        Map<String, Object> response = new HashMap<>();
        response.put("action", Action.ON_INITIAL_MESSAGES_RECEIVED);
        List<Map<String, String>> groupMsgRepo = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = Connector.connect.prepareStatement(
                    "SELECT * FROM message WHERE group_id = ?"
            );
            preparedStatement.setInt(1, Integer.parseInt(groupId));

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                PreparedStatement getSenderInformation = Connector.connect.prepareStatement(
                        "SELECT user_fname, user_lname, email FROM user_acc WHERE user_id = ?"
                );

                getSenderInformation.setInt(1, Integer.parseInt(resultSet.getString("from_user")));

                ResultSet userInformationSent = getSenderInformation.executeQuery();

                Map<String, String> msgRepo = new HashMap<>();
                if (userInformationSent.next()) {
                    msgRepo.put("firstName", userInformationSent.getString("user_fname"));
                    msgRepo.put("lastName", userInformationSent.getString("user_lname"));
                    msgRepo.put("email", userInformationSent.getString("email"));
                    msgRepo.put("senderName", msgRepo.get("firstName") + " " + msgRepo.get("lastName"));
                }
                msgRepo.put("senderId", resultSet.getString("from_user"));
                msgRepo.put("groupId", groupId);
                msgRepo.put("message", resultSet.getString("message"));
                msgRepo.put("timeSent", String.valueOf(resultSet.getTimestamp("time_sent")));
                groupMsgRepo.add(msgRepo);
            }
            response.put("messages", groupMsgRepo);
            objOut.writeObject(response);
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
     * <p>
     *
     * @param sender who sent the message
     * @param address who receives the message
     * @param message message to send
     */
    protected void sendMessage(int sender, String address, String message, Timestamp timeSent) {
        try {
            PreparedStatement preparedStatement = Connector.connect.prepareStatement(
                    "SELECT user_fname, user_lname, email FROM user_acc WHERE user_id = ?"
            );

            preparedStatement.setInt(1, sender);

            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();

            Map<String, Object> response = new HashMap<>();
            response.put("action", Action.ON_MESSAGE_RECEIVE);
            Map<String, String> messageRepo = new HashMap<>();
            messageRepo.put("sender", String.valueOf(sender));
            messageRepo.put("firstName", resultSet.getString("user_fname"));
            messageRepo.put("lastName", resultSet.getString("user_lname"));
            messageRepo.put("email", resultSet.getString("email"));
            messageRepo.put("address", address);
            messageRepo.put("message", message);
            messageRepo.put("timeSent", timeSent.toString());
            response.put("messages", messageRepo);
            objOut.writeObject(response); // send message to the server
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    protected void sendMap(Map<String, Object> dataMap) {
        try {
            objOut.writeObject(dataMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected User getUser() {
        return this.user;
    }
}