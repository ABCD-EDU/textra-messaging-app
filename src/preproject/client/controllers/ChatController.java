package preproject.client.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import preproject.client.Action;
import preproject.client.ClientExecutable;

import java.io.IOException;
import java.net.SocketException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.*;

import static preproject.client.ClientExecutable.serverConnector;

public class ChatController implements Initializable {

    private ClientThread clientThread;
    private String fName;
    private String lName;
    private ArrayList<Message> messages;
    private String ID;
    private String email;
    private List<Map<String, String>> groupsList;
    private String currentlySelectedGroupID;
    private String userColor;

    ConvoInfoController convoController;

    private int unreadBroadcastMessages;
    private ArrayList<Message> broadCastMessages;

    //left pane lcomponents
    @FXML
    private Label email_label;

    @FXML
    private TextField searchContacts_field;

    @FXML
    private Pane newConversations_Pane;

    @FXML
    private ScrollPane conversation_scrollPane;

    @FXML
    private Button broadcast_button;

    @FXML
    private Button logout_button;

    @FXML
    private Pane chatBoxPane;

    @FXML
    private Circle group_picture;

    @FXML
    private Label groupAlias_label;

    @FXML
    private Label unreadMsgs_label;

    @FXML
    private Button favorite_button;
    boolean favIsPressed = false;

    @FXML
    private Circle broadcastNotif_circle;

    //right pane comopnents
    @FXML
    private Pane header_pane;

    @FXML
    private Label header_label;

    @FXML
    private ScrollPane messages_scrollPane;

    @FXML
    private VBox people_vBox;

    @FXML
    private TextArea message_area;

    @FXML
    private VBox messages_vBox;

    @FXML
    private Label timeStamp_label;

    @FXML
    private Label fInitial_label;

    @FXML
    private Label lInitial_label;

    @FXML
    private Button send_button;


    private class ClientThread extends Thread {

        @SuppressWarnings("unchecked")
        public void run() {
            System.out.println("CLIENT THREAD STARTED");
            while (!serverConnector.getSocket().isClosed()) {
                Map<String, Object> readData;
                try {
                    readData = (Map<String, Object>) serverConnector.getObjIn().readObject();
                } catch (SocketException e) {
                    break;
                } catch (IOException | ClassNotFoundException exc) {
                    continue;
                }

                System.out.println("==== NEW ACTION RECEIVED ==== " + readData.get("action"));
                switch (String.valueOf(readData.get("action"))) {
                    case Action.ON_GROUP_LIST_SEND:
                        System.out.println("GROUP LIST RECEIVED");
                        groupsList = (List<Map<String, String>>) readData.get("data");
                        System.out.println("Received groups list size: " + groupsList.size());
                        List<Map<String, String>> unreadMessagesList =
                                (List<Map<String, String>>)readData.get("unreadMessages");
                        setGroupsListNotificationAttributes(unreadMessagesList);
                        renderGroupsList(sortGroupList(groupsList, "-1"));
                        break;
                    case Action.ON_USER_INFO_SEND:
                        System.out.println("USER INFO RECEIVED");
                        handleUserInformationSend((Map<String, String>) readData.get("data"));
                        break;
                    case Action.ON_MESSAGE_RECEIVE:
                        System.out.println("MESSAGE RECEIVED");
                        handleMessagesReceived((Map<String, String>) readData.get("messages"));
                        break;
                    case Action.ON_GROUP_CREATION:
                        System.out.println("GROUP CREATION");
                        handleGroupCreation((Map<String, String>)readData.get("groupMap"), (String)readData.get("status"));
                        break;
                    case Action.ON_INITIAL_MESSAGES_RECEIVED:
                        System.out.println("INITIAL MESSAGES RECEIVED");
                        handleInitialMessagesReceived((List<Map<String, String>>) readData.get("messages"));
                        break;
                    case Action.ON_FAVORITE_TOGGLED:
                        System.out.println("FAVORITE TOGGLED");
                        handleFavoriteToggle((String) readData.get("groupId"), (String) readData.get("isFav"));
                        break;
                    case Action.ON_GROUP_MEMBERS_SEND:
                        System.out.println("GROUP MEMBERS REQUESTED");
                        handleGroupInfo((List<HashMap<String,String>>)readData.get("members"));
                        break;
                    case Action.ON_REMOVE_A_MEMBER:
                        System.out.println("A MEMBER IS REMOVED");
                        handleMemberRemoval((String)readData.get("email"), (String)readData.get("groupId"));
                        break;
                    case Action.ON_ADD_NEW_GROUP_MEMBER:
                        System.out.println("A NEW MEMBER IS ADDED");
                        handleGroupCreation((Map<String, String>)readData.get("groupMap"), "true");
                        break;
                }
            }
        }
    }

    private void setGroupsListNotificationAttributes(List<Map<String, String>> unreadList) {
        for (Map<String, String> unreadMap : unreadList) {
            String unreadGrpId = unreadMap.get("groupId");
            for (Map<String, String> groupMap : groupsList) {
                if (groupMap.get("groupId").equals(unreadGrpId))
                    groupMap.replace("unreadMessages", unreadMap.get("unreadMessages"));
            }
        }
    }

    //TODO: TEST FIRST
    @FXML
    void onKeyPress(KeyEvent event) throws IOException {
        KeyCombination shiftEnter = new KeyCodeCombination(KeyCode.ENTER, KeyCodeCombination.SHIFT_DOWN);
        if (shiftEnter.match(event)) {
            message_area.appendText("\n");
        } else if (event.getCode().equals(KeyCode.ENTER)) {
            handleMessageSend(message_area.getText().trim());
            message_area.clear();
        }
    }

    @FXML
    void onClick() throws IOException {
        handleMessageSend(message_area.getText().trim());
        message_area.clear();
    }

    /**
     * update people_vBox such that the converstaionBoxes inside it will
     * be those that have an alias that match the inputted string
     *
     * Algorithm:
     * 1. Get list of groups that match the specified string
     * 2. Render that list of groups
     * @param event
     */
    @FXML
    void onSearchFieldInput(KeyEvent event) {
        if (searchContacts_field.getText().isBlank())
            renderGroupsList(sortGroupList(groupsList, "-1"));
        List<Map<String, String>> results = new ArrayList<>();
        for (Map<String, String> group : groupsList) {
            if (group.get("alias").startsWith(searchContacts_field.getText().trim())) {
                results.add(group);
            }
        }
        renderGroupsList(sortGroupList(results, "-1"));
    }

    private void handleFavoriteToggle(String groupId, String isFav) {
        Platform.runLater(() -> people_vBox.getChildren().clear());
        for (Map<String, String> group : groupsList) {
            if (group.get("groupId").equals(groupId)) {
                group.replace("is_fav", isFav);
                System.out.println("group id: " + groupId + " " + isFav);
                System.out.println("favorite value replace ---");
            }
        }
        System.out.println("Handle favorite toggle: " + groupsList.size());
        renderGroupsList(sortGroupList(groupsList, "-1"));
    }

    private void handleMessageSend(String message) throws IOException {
        if (message.isEmpty()) return;
        Message[] messages = processMessage(message);
        if (currentlySelectedGroupID.equals("-1"))
            broadCastMessages.addAll(Arrays.asList(messages));
        for (Message m : broadCastMessages)
            System.out.println(m);
        previewMessage(messages);
        sendMessage(messages);
    }

    private Message[] processMessage(String message) {
        System.out.println("Processing Message: " + message);
        // Include additional parsing - specify the max amount of characters per line
        // so that there will be no problems with wrapping
        String[] msgData = message.split("\n");
        Message[] messages = new Message[msgData.length];

        for (int i = 0; i < msgData.length; i++) {
            Timestamp time = new Timestamp(new Date().getTime());
            messages[i] = new Message(this.ID,time,this.fName, this.lName, msgData[i], this.userColor);
        }

        return messages;
    }

    /**
     * Send message to database here
     */
    private void sendMessage(Message[] msgData) throws IOException {
        Map<String, Object> request = new HashMap<>();
        if (currentlySelectedGroupID.equals("-1")) // send to all online users
            request.put("action", Action.SEND_BROADCAST_MESSAGE);
        else
            request.put("action", Action.SEND_MESSAGE);
        List<Map<String, String>> messagesList = new ArrayList<>();
        for (Message msg : msgData) {
            Map<String, String> msgMap = new HashMap<>();
            msgMap.put("userId", this.ID);
            msgMap.put("groupId", currentlySelectedGroupID);
            msgMap.put("messageSent", msg.getMessage());
            msgMap.put("timeSent", msg.getTimeStamp().toString());
            messagesList.add(msgMap);
        }
        request.put("messagesList", messagesList);
        request.put("senderId", msgData[0].getSenderID());
        serverConnector.getObjOut().writeObject(request);
    }

    /**
     * Load a new MessagesHBoxPrimary and MessagesHBoxSecondary for every message depending
     * on previous message sent.
     */
    private void previewMessage(Message[] msgData) {

        for (int i = 0; i < msgData.length; i++) {
            Node node = null;
            // last sender == incoming sender?
            boolean primary = true;
            if (messages.size() != 0)
                primary = !messages.get(messages.size()-1).getSenderID().equals(msgData[i].getSenderID());

            FXMLLoader loader = null;
            if (primary)
                loader = new FXMLLoader(getClass().getResource("../resources/view/MessageBoxPrimary.fxml"));
            else
                loader = new FXMLLoader(getClass().getResource("../resources/view/MessageBoxSecondary.fxml"));

            try { node = loader.load(); }
            catch (IOException e) { e.printStackTrace(); }
            assert node != null;

            // initialize message box properties
            for (Node component : ((Pane) node).getChildren())
                initializeMessageBoxProperties(component, primary, msgData[i]);

            messages.add(msgData[i]);
            Node finalNode = node;
            Platform.runLater(() -> messages_vBox.getChildren().add(finalNode));
        }
    }

    private void initializeMessageBoxProperties(Node component, boolean primary,Message msgData) {
        if (component.getId().equals("message_label"))
            ((Label)component).setText(msgData.getMessage());
        if (primary) {
            if (component.getId().equals("name_label"))
                ((Label)component).setText(msgData.getFirstName() + " " + msgData.getLastName());
            if (component.getId().equals("picture")) {
                System.out.println("SETTING COLOR TO: " + msgData.getColor());
                if (msgData.getColor() == null || msgData.equals("null")) {
                }else {
                    ((Circle)component).setFill(Color.web(msgData.getColor()));
                }
            }
            if (component.getId().equals("fInitial_label"))
                if (fName.length() > 0)
                    ((Label)component).setText(String.valueOf(msgData.getFirstName().charAt(0)));
            if (component.getId().equals("lInitial_label"))
                if (lName.length() > 0)
                    ((Label)component).setText(String.valueOf(msgData.getLastName().charAt(0)));
            if (component.getId().equals("timeStamp_label"))
                ((Label)component).setText(msgData.getTimeStamp().toString());
        }
    }

    private void handleInitialMessagesReceived(List<Map<String, String>> initialMessages) {
        Platform.runLater(() -> messages_vBox.getChildren().clear());
        if (initialMessages == null || initialMessages.size() == 0) {
            System.out.println("initial message null or size 0");
            return;
        }
        for (Map<String, String> message : initialMessages){
            Message[] msgData = {new Message(
                    message.get("senderId"),
                    Timestamp.valueOf(message.get("timeSent")),
                    message.get("firstName"),
                    message.get("lastName"),
                    message.get("message"),
                    message.get("color")
            )};
            previewMessage(msgData);
        }
    }

    // TODO: Get sender name as well - fix in backend
    private void handleMessagesReceived(Map<String, String> message) {
        System.out.println("======== message received ========== address:" + message.get("address"));
        Message[] msgData = {new Message(
                message.get("sender"),
                Timestamp.valueOf(message.get("timeSent")),
                message.get("firstName"),
                message.get("lastName"),
                message.get("message"),
                message.get("color")
        )};
        if (message.get("address").equals("-1")) { // if message is from broadcast to all
            broadCastMessages.addAll(Arrays.asList(msgData));
            if (currentlySelectedGroupID.equals("-1"))
                previewMessage(msgData);
            else
                broadcastNotif_circle.setVisible(true);
            return;
        }
        if (!message.get("address").equals(this.currentlySelectedGroupID)) {
            for (Map<String, String> groupMap : groupsList) {
                if (groupMap.get("groupId").equals(message.get("address"))){
                    groupMap.replace("unreadMessages",
                            String.valueOf(Integer.parseInt(groupMap.get("unreadMessages"))+1));
                    break;
                }
            }
            renderGroupsList(sortGroupList(groupsList, message.get("address")));
            return;
        }
        // if message received is from group currently being viewed
        previewMessage(msgData);
    }

    private void renderGroupsList(List<Map<String, String>> groupsList) {
        System.out.println("Render Groups List: " + groupsList.size());
        try {
            System.out.println("GROUP REQUEST ACCEPTED NUMBER OF GROUPS: " + groupsList.size());
            Platform.runLater(() -> people_vBox.getChildren().clear());
            for (Map<String, String> groupMap : groupsList) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("../resources/view/ConversationBox.fxml"));
                Node node = loader.load();
                setConvoBoxOnMouseClick(node, groupMap);
                setConvoBoxChildProperties(node, groupMap);

                Platform.runLater(() -> people_vBox.getChildren().add(node));
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setConvoBoxOnMouseClick(Node node, Map<String, String> groupMap) {
        node.setOnMouseClicked((event) -> { // on mouse click of convo box
            messages = new ArrayList<>();
            message_area.clear();
            message_area.setPromptText("message " + groupMap.get("alias"));
            message_area.requestFocus();
            message_area.setDisable(false);
            HashMap<String, Object> request = new HashMap<>();
            request.put("action", Action.GET_GROUP_MESSAGES);
            request.put("groupId", groupMap.get("groupId"));
            try {
                serverConnector.getObjOut().writeObject(request);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Remove unread messages circle
            for (Node component : ((Pane)node).getChildren()) {
                if (component.getId().equals("groupAlias_label"))
                    ((Label)component).setStyle("-fx-font-weig3333333ht: regular");
                if (component.getId().equals("unreadMsgs_label"))
                    component.setVisible(false);
                for (Map<String, String> globalGrpMap : groupsList) {
                    if (globalGrpMap.equals(groupMap)) {
                        globalGrpMap.replace("unreadMessages", "0");
                    }
                }
            }
//            setConversationHeader(groupMap.get("alias"), String.valueOf(this.ID).equals(groupMap.get("uidAdmin")));
            setConversationHeader(groupMap.get("alias"), false);
            currentlySelectedGroupID = groupMap.get("groupId");
        });
    }

    private void setConvoBoxChildProperties(Node node, Map<String, String> groupMap) {
        for (Node component : ((Pane)node).getChildren()) {
            if (Integer.parseInt(groupMap.get("unreadMessages")) > 0) {
                if (component.getId().equals("notif_circle"))
                    component.setVisible(true);
                if (component.getId().equals("unreadMsgs_label")) {
                    component.setVisible(true);
                    ((Label)component).setText(groupMap.get("unreadMessages") + " unread messages");
                }
            }else {
                if (component.getId().equals("notif_circle"))
                    component.setVisible(false);
                if (component.getId().equals("unreadMsgs_label"))
                    component.setVisible(false);
            }
            if (component.getId().equals("groupAlias_label")){
                ((Label)component).setText(groupMap.get("alias"));
                if (Integer.parseInt(groupMap.get("unreadMessages")) > 0)
                    ((Label)component).setStyle("-fx-font-weight: bold");
                else
                    ((Label)component).setStyle("-fx-font-weig3333333ht: regular");
            }
            if (component.getId().equals("favorite_button")) {
                if (!groupMap.get("is_fav").equals("1")) {
                    ((Button)component).setStyle("-fx-shape:  \"M462.3 62.6C407.5 15.9 326 24.3 275.7 76.2L256 96.5l-19.7-20.3C186.1 24.3 104.5 15.9 49.7 62.6c-62.8 53.6-66.1 149.8-9.9 207.9l193.5 199.8c12.5 12.9 32.8 12.9 45.3 0l193.5-199.8c56.3-58.1 53-154.3-9.8-207.9z\"; -fx-background-color:  #EEEEEE; -fx-border-color: #EDB458");
                    favIsPressed = false;
                } else {
                    ((Button)component).setStyle("-fx-shape:  \"M462.3 62.6C407.5 15.9 326 24.3 275.7 76.2L256 96.5l-19.7-20.3C186.1 24.3 104.5 15.9 49.7 62.6c-62.8 53.6-66.1 149.8-9.9 207.9l193.5 199.8c12.5 12.9 32.8 12.9 45.3 0l193.5-199.8c56.3-58.1 53-154.3-9.8-207.9z\"; -fx-background-color:  #EDB458");
                    favIsPressed = true;
                }
                ((Button)component).setOnMouseClicked((e) -> {
                    Map<String, String> request = new HashMap<>();
                    request.put("action", Action.ADD_FAVOURITE);
                    request.put("userId", this.ID);
                    request.put("groupId", groupMap.get("groupId"));
                    if (!groupMap.get("is_fav").equals("1"))
                        request.put("isFav", "1");
                    else
                        request.put("isFav", "0");
                    try {
                        serverConnector.getObjOut().writeObject(request);
                    } catch (IOException err) {
                        err.printStackTrace();
                    }
                });
            }
        }
    }

    /**
     * Sorts the groupList such that favorites will be placed in the beginning of the list
     * Set priorityGrpId to -1 if there is no group to prioritize
     * TODO: algorithm is trash pls optimize lmao
     * Algorithm:
     * 1. add all favorites
     * 2. add all non favorites
     * 3. place priority group on top
     * 4. return
     */
    private List<Map<String, String>> sortGroupList(List<Map<String, String>> groupsList, String priorityGrpId) {
        System.out.println("Sort Groups List: " + groupsList.size());
        ArrayList<Map<String, String>> sortedList = new ArrayList<>();
        System.out.println(groupsList.size());
        int favorites = 0;
        for (Map<String, String> group : groupsList) { // put all favorites in sortedList
            if (group.get("is_fav").equals("1")) {
                sortedList.add(group);
                favorites++;
            }
        }
        for (Map<String, String> group : groupsList) { // put all not favorites in sortedList
            if (group.get("is_fav").equals("0"))
                sortedList.add(group);
        }
        if (priorityGrpId.equals("-1"))
            return sortedList;
        for (Map<String, String> group : groupsList) { // put priority on top
            if (group.get("groupId").equals(priorityGrpId)) {
                int origIdxOfGrp = sortedList.indexOf(group);
                if (group.get("is_fav").equals("1"))
                    sortedList.add(0, group);
                else
                    sortedList.add(favorites, group);
                sortedList.remove(origIdxOfGrp+1);
            }
        }
        // TODO: Find more elegant way of doing this - randomly updating global variable in method bad - bad practice
        this.groupsList = sortedList;
        return sortedList; // set groupList = sortedList
    }

    private void handleGroupCreation(Map<String, String> groupMap, String status) {
        if (!status.equals("true")) {
            System.out.println("group creation unsuccessful");
            return;
        }
        for (Map<String, String> gpMap : groupsList) {
            if (groupMap.get("groupId").equals(gpMap.get("groupId")))
                return;
        }
        groupsList.add(groupMap);
        renderGroupsList(sortGroupList(groupsList, groupMap.get("groupId")));
    }

    private void handleUserInformationSend(Map<String, String> userInformation) {
        this.ID = userInformation.get("userId");
        this.fName = userInformation.get("firstName");
        this.lName = userInformation.get("lastName");
        this.email = userInformation.get("email");
        this.userColor = userInformation.get("color");
        email_label.setText(this.email);
    }

    /**
     * Remodel the header_pane into just a text box where user can input names for new conversation
     */
    @FXML
    private void onNewConversationClick(MouseEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../resources/view/NewConversationStage.fxml"));
            Stage stage = (Stage)fxmlLoader.load();
            stage.initModality(Modality.APPLICATION_MODAL);
            NewConversationStageController controller = fxmlLoader.getController();
            controller.setEmail(this.email);
            stage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void mouseEnter(MouseEvent event) {
        if (event.getSource().equals(send_button)) {
            send_button.setStyle("-fx-shape:  \"M256 8c137 0 248 111 248 248S393 504 256 504 8 393 8 256 119 8 256 8zm113.9 231L234.4 103.5c-9.4-9.4-24.6-9.4-33.9 0l-17 17c-9.4 9.4-9.4 24.6 0 33.9L285.1 256 183.5 357.6c-9.4 9.4-9.4 24.6 0 33.9l17 17c9.4 9.4 24.6 9.4 33.9 0L369.9 273c9.4-9.4 9.4-24.6 0-34z\"; -fx-background-color: #333333");
        } else if (event.getSource().equals(newConversations_Pane)) {
            newConversations_Pane.setStyle("-fx-background-color: #DDDDDD; -fx-background-radius: 5");
        }
    }

    @FXML
    void mouseExit(MouseEvent event) {
        if (event.getSource().equals(send_button)) {
            send_button.setStyle("-fx-shape:  \"M256 8c137 0 248 111 248 248S393 504 256 504 8 393 8 256 119 8 256 8zm113.9 231L234.4 103.5c-9.4-9.4-24.6-9.4-33.9 0l-17 17c-9.4 9.4-9.4 24.6 0 33.9L285.1 256 183.5 357.6c-9.4 9.4-9.4 24.6 0 33.9l17 17c9.4 9.4 24.6 9.4 33.9 0L369.9 273c9.4-9.4 9.4-24.6 0-34z\"; -fx-background-color: #999999");
        } else if (event.getSource().equals(newConversations_Pane)) {
            newConversations_Pane.setStyle("-fx-background-color: #EEEEEE");

        }
    }

    private void setConversationHeader(String alias, Boolean isBroadcast) {
        header_pane.getChildren().clear();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../resources/view/ConversationHeader.fxml"));
        Node node = null;
        try {
            node = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert node != null;
        for (Node component : ((Pane)node).getChildren()){
            if (component.getId().equals("header_label"))
                ((Label) component).setText(alias);
            if (component.getId().equals("info_button")) {
                setAddingMembersControl((Button) component, false);
                if (isBroadcast)
                    component.setVisible(false);
            }
        }
        header_pane.getChildren().add(node);
    }

    private void setAddingMembersControl(Button component, Boolean isAdmin){
        component.setOnAction(event -> {
            try {
                FXMLLoader loader= new FXMLLoader(getClass().getResource("../resources/view/ConvoInfoScreen.fxml"));
                Stage tempStage =(Stage)loader.load();
                tempStage.initModality(Modality.APPLICATION_MODAL);
                convoController = loader.getController();
                convoController.setCurrentlySelectedGroupID(currentlySelectedGroupID);
                convoController.setUserIsAdmin(isAdmin);
                convoController.setCurrentUserEmail(email);
                convoController.setGroupAlias(getCurrentGroupAlias());
                convoController.setGroupAdmin(getCurrentGroupAdmin());
                convoController.requestMembers();
                convoController.setUserId(this.ID);
                tempStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void handleGroupInfo(List<HashMap<String, String>> groupMembers){
        convoController.setGroupMembers(groupMembers);
    }

    private void handleMemberRemoval(String email, String groupId){
        if (!email.equals(this.email))
            return;
        for (Map<String, String> groupMap : groupsList) {
            if (groupMap.get("groupId").equals(groupId)) {
                groupsList.remove(groupMap);
                break;
            }
        }
        renderGroupsList(sortGroupList(groupsList, "-1"));
    }

    private void handleMemberAddition(Boolean areAdded){
        convoController.requestMembers();
        System.out.println("REQUESTING FOR MEMBERS AFTER ADDING");
    }


    private String getCurrentGroupAlias(){
        for (Map<String, String> groups: groupsList){
            if (groups.get("groupId").equals(currentlySelectedGroupID)){
                return groups.get("alias");
            }
        }
        return null;
    }
    private String getCurrentGroupAdmin(){
        for (Map<String, String> groups: groupsList){
            if (groups.get("groupId").equals(currentlySelectedGroupID)){
                return groups.get("uidAdmin");
            }
        }
        return null;
    }

    private void initializeBroadcastButton() {
        broadCastMessages = new ArrayList<>();
        broadcast_button.setOnMouseClicked((e) -> {
            broadcastNotif_circle.setVisible(false);
            messages = new ArrayList<>();
            Platform.runLater(() -> messages_vBox.getChildren().clear());
            // TODO: clear notifs for broadcast as well
            unreadBroadcastMessages = 0;
            setConversationHeader("Broadcast To All", true);
            currentlySelectedGroupID = "-1";
            message_area.clear();
            message_area.setPromptText("Send a message to all online users");
            message_area.requestFocus();
            message_area.setDisable(false);
            try {
                System.out.println(broadCastMessages.size());
                Message[] toPreview = new Message[broadCastMessages.size()];
                for (int i = 0; i < broadCastMessages.size(); i++)
                    toPreview[i] = broadCastMessages.get(i);
                previewMessage(toPreview);
            } catch (NullPointerException err) {
                err.printStackTrace();
            }
        });
    }

    /**
     *  Set values of header, text placeholder, listOfPeople, messages
     *  TODO: Clean this code
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        clientThread = new ClientThread();
        clientThread.setDaemon(true);
        clientThread.start();
        currentlySelectedGroupID = "null";
        messages = new ArrayList<>();
        groupsList = new ArrayList<>();

        messages_scrollPane.vvalueProperty().bind(messages_vBox.heightProperty());

        HashMap<String, Object> request = new HashMap<>();
        request.put("action", Action.GET_USER_INFORMATION);
        request.put("email", this.email);

        try {
            serverConnector.getObjOut().writeObject(request);
            System.out.println("USER INFORMATION REQUEST SENT");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // TODO: Clean up code by turning this block into a function that takes in varargs as argument
        HashMap<String, Object> request1 = new HashMap<>();
        request1.put("action", Action.GET_GROUP_LIST);
        try {
            serverConnector.getObjOut().writeObject(request1);
            System.out.println("REQUEST FOR GROUPS SENT");
        } catch (IOException e) {
            e.printStackTrace();
        }

        logout_button.setOnAction(e -> {
            ScreenController sC = new ScreenController((Stage) logout_button.getScene().getWindow());
            sC.activateUsingPath("../resources/view/SignInScreen.fxml");
            serverConnector.reset();
        });

        initializeBroadcastButton();
        message_area.setDisable(true);
        broadcastNotif_circle.setVisible(false);
    }

}
