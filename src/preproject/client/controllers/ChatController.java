package preproject.client.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import preproject.client.Action;
import preproject.client.ClientExecutable;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.*;

public class ChatController implements Initializable {

    private ClientThread clientThread;
    private String fName;
    private String lName;
    private ArrayList<Message> messages;
    private String ID;
    private String email;
    private List<Map<String, String>> groupsList;
    private String currentlySelectedGroupID;

    private int unreadBroadcastMessages;
    private ArrayList<Message> broadCastMessages;

    @FXML
    private Pane header_pane;

    @FXML
    private Label header_label;

    @FXML
    private Label email_label;

    @FXML
    private TextField searchContacts_field;

    @FXML
    private Pane newConversations_Pane;

    @FXML
    private Label groupAlias_label;

    @FXML
    private ScrollPane conversation_scrollPane;

    @FXML
    private ScrollPane messages_scrollPane;

    @FXML
    private VBox people_vBox;

    @FXML
    private Button broadcast_button;

    @FXML
    private Button logout_button;

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

    private class ClientThread extends Thread {

        public void run(){
            System.out.println("CLIENT THREAD STARTED");
            while (true) {
                Map<String, Object> readData = null;
                try{
                    readData = (Map<String, Object>) ClientExecutable.serverConnector.getObjIn().readObject();
                }catch (Exception e) {
                    e.printStackTrace();
                    continue;
                }
                System.out.println("==== NEW ACTION RECEIVED ==== " + readData.get("action"));
                switch(String.valueOf(readData.get("action"))){
                    case Action.ON_GROUP_LIST_SEND:
                        System.out.println("GROUP LIST RECEIVED");
                        groupsList = (List<Map<String, String>>)readData.get("data");
                        System.out.println("Received groups list size: " + groupsList.size());
                        renderGroupsList(sortGroupList(groupsList, "-1"));
                        break;
                    case Action.ON_USER_INFO_SEND:
                        System.out.println("USER INFO RECEIVED");
                        handleUserInformationSend((Map<String, String>)readData.get("data"));
                        break;
                    case Action.ON_MESSAGE_RECEIVE:
                        System.out.println("MESSAGE RECEIVED");
                        handleMessagesReceived((Map<String, String>) readData.get("messages"));
                        break;
                    case Action.ON_GROUP_CREATION:
                        handleGroupCreation((String)readData.get("status"));
                        break;
                    case Action.ON_INITIAL_MESSAGES_RECEIVED:
                        System.out.println("INITIAL MESSAGES RECEIVED");
                        handleInitialMessagesReceived((List<Map<String, String>>) readData.get("messages"));
                        break;
                    case Action.ON_FAVORITE_TOGGLED:
                        System.out.println("FAVORITE TOGGLED");
                        handleFavoriteToggle((String)readData.get("groupId"), (String)readData.get("isFav"));
                        break;

                }
            }
        }

    }

    /**
     * TODO: take into consideration if entered key is shift + enter
     */
    @FXML
    void onKeyPress(KeyEvent key) throws IOException {
        if (key.getCode().equals(KeyCode.ENTER)) {
            handleMessageSend(message_area.getText().trim());
            message_area.clear();
        }
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
            System.out.println(group);
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
            messages[i] = new Message(this.ID,time,this.fName + " " + this.lName, msgData[i]);
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
        ClientExecutable.serverConnector.getObjOut().writeObject(request);
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

            /*
             TODO: Separate the processing for primary and secondary message boxes to improve efficiency
                secondary message box only has message_label component
             */
            for (Node component : ((Pane) node).getChildren()) {
                if (component.getId().equals("message_label"))
                    ((Label)component).setText(msgData[i].getMessage());
                else if (component.getId().equals("name_label"))
                    ((Label)component).setText(msgData[i].getSenderName());
//                String senderName = msgData[i].getSenderName().split(" ")
                /*
                 TODO: Parsing sender name in client side is not efficient. Make server send first name and last name
                    instead of sending whole name.
                 */
                if (component.getId().equals("fInitial_label")) {
                    if (fName.length() > 0)
                        ((Label)component).setText(String.valueOf(msgData[i].getSenderName().charAt(0)));
                }
                if (component.getId().equals("lInitial_label")) {
                    if (lName.length() > 0)
                        ((Label)component).setText(String.valueOf(msgData[i].getSenderName().charAt(msgData[i].getSenderName().indexOf(" ")+1)));
                }
                if (component.getId().equals("timeStamp_label"))
                    ((Label)component).setText(msgData[i].getTimeStamp().toString());
            }

            messages.add(msgData[i]);
            Node finalNode = node;
            Platform.runLater(() -> messages_vBox.getChildren().add(finalNode));
        }

    }


    private void handleInitialMessagesReceived(List<Map<String, String>> initialMessages) {
        Platform.runLater(() -> messages_vBox.getChildren().clear());
        if (initialMessages.size() == 0)
            return;
        for (Map<String, String> message : initialMessages){
            Message[] msgData = {new Message(
                    message.get("senderId"),
                    Timestamp.valueOf(message.get("timeSent")),
                    message.get("senderName"),
                    message.get("message")
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
                message.get("sender"),
                message.get("message")
        )};
        if (message.get("address").equals("-1")) { // if message is from broadcast to all
            broadCastMessages.addAll(Arrays.asList(msgData));
            if (currentlySelectedGroupID.equals("-1"))
                previewMessage(msgData);
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
                String alias = groupMap.get("alias");
                FXMLLoader loader = new FXMLLoader(getClass().getResource("../resources/view/ConversationBox.fxml"));
                Node node = loader.load();
                node.setOnMouseClicked((event) -> {
                    messages = new ArrayList<>();
                    HashMap<String, Object> request = new HashMap<>();
                    request.put("action", Action.GET_GROUP_MESSAGES);
                    request.put("groupId", groupMap.get("groupId"));
                    try {
                        ClientExecutable.serverConnector.getObjOut().writeObject(request);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // Remove unread messages circle
                    for (Node component : ((Pane)node).getChildren()) {
                        if (component.getId().equals("notif_circle"))
                            component.setVisible(false);
                        if (component.getId().equals("unreadMsgs_label"))
                            component.setVisible(false);
                        for (Map<String, String> globalGrpMap : groupsList) {
                            if (globalGrpMap.equals(groupMap)) {
                                globalGrpMap.replace("unreadMessages", "0");
                            }
                        }
                    }
                    setConversationHeader(groupMap.get("alias"), String.valueOf(this.ID).equals(groupMap.get("uidAdmin")));
                    currentlySelectedGroupID = groupMap.get("groupId");
                });
//                System.out.println(groupMap.get());
                for (Node component : ((Pane)node).getChildren()) {
                    if (Integer.parseInt(groupMap.get("unreadMessages")) > 0) {
                        if (component.getId().equals("notif_circle"))
                            component.setVisible(true);
                        if (component.getId().equals("unreadMsgs_label")) {
                            component.setVisible(true);
                            ((Label)component).setText(groupMap.get("unreadMessages"));
                        }
                    }else {
                        if (component.getId().equals("notif_circle"))
                            component.setVisible(false);
                        if (component.getId().equals("unreadMsgs_label"))
                            component.setVisible(false);
                    }
                    if (component.getId().equals("groupAlias_label"))
                        ((Label)component).setText(alias);
                    if (component.getId().equals("favorite_button")) {
                        if (!groupMap.get("is_fav").equals("1")) {
                            ((Button)component).setStyle("-fx-shape:  \"M462.3 62.6C407.5 15.9 326 24.3 275.7 76.2L256 96.5l-19.7-20.3C186.1 24.3 104.5 15.9 49.7 62.6c-62.8 53.6-66.1 149.8-9.9 207.9l193.5 199.8c12.5 12.9 32.8 12.9 45.3 0l193.5-199.8c56.3-58.1 53-154.3-9.8-207.9z\"; -fx-background-color:  #DDDDDD; -fx-border-color: #EDB458");
                        }else {
                            ((Button)component).setStyle("-fx-shape:  \"M462.3 62.6C407.5 15.9 326 24.3 275.7 76.2L256 96.5l-19.7-20.3C186.1 24.3 104.5 15.9 49.7 62.6c-62.8 53.6-66.1 149.8-9.9 207.9l193.5 199.8c12.5 12.9 32.8 12.9 45.3 0l193.5-199.8c56.3-58.1 53-154.3-9.8-207.9z\"; -fx-background-color:  #EDB458");
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
                                ClientExecutable.serverConnector.getObjOut().writeObject(request);
                            } catch (IOException err) {
                                err.printStackTrace();
                            }
                        });
                    }
                }
                Platform.runLater(() -> people_vBox.getChildren().add(node));
            }
        }catch (IOException e) {
            e.printStackTrace();
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

    private void handleGroupCreation(String creationStatus) {
        boolean successfulCreation = creationStatus.equals("true");

        if (successfulCreation) {
            HashMap<String, Object> request1 = new HashMap<>();
            request1.put("action", Action.GET_GROUP_LIST);
            try {
                ClientExecutable.serverConnector.getObjOut().writeObject(request1);
                System.out.println("REQUEST FOR GROUPS SENT");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            System.out.println("CREATION OF GROUP UNSUCCESSFUL");
        }
    }

    private void handleUserInformationSend(Map<String, String> userInformation) {
        this.ID = userInformation.get("userId");
        this.fName = userInformation.get("firstName");
        this.lName = userInformation.get("lastName");
        this.email = userInformation.get("email");
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

    private void setConversationHeader(String alias, Boolean isAdmin) {
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
            if (component.getId().equals("header_button"))
                /*
                    TODO: Add functionality to this button.
                     Its functionality should be different if user is admin of said group opened.
                     If user is NOT admin: show list of users and allow adding of other members
                     If user is admin: show list of users and allow removal and addition of other members
                     Suggestion:
                     Open a new window where the user can perform these actions upon press of button
                 */
                if (!isAdmin)
                    component.setVisible(false); // for debug purposes only
        }
        header_pane.getChildren().add(node);
    }

    private void initializeBroadcastButton() {
        broadCastMessages = new ArrayList<>();
        broadcast_button.setOnMouseClicked((e) -> {
            messages = new ArrayList<>();
            Platform.runLater(() -> messages_vBox.getChildren().clear());
            // TODO: clear notifs for broadcast as well
            unreadBroadcastMessages = 0;
            setConversationHeader("Broadcast To All", false);
            currentlySelectedGroupID = "-1";
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
        clientThread.start();
        currentlySelectedGroupID = "null";
        messages = new ArrayList<>();
        groupsList = new ArrayList<>();

        messages_scrollPane.vvalueProperty().bind(messages_vBox.heightProperty());

        HashMap<String, Object> request = new HashMap<>();
        request.put("action", Action.GET_USER_INFORMATION);
        request.put("email", this.email);

        try {
            ClientExecutable.serverConnector.getObjOut().writeObject(request);
            System.out.println("USER INFORMATION REQUEST SENT");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // TODO: Clean up code by turning this block into a function that takes in varargs as argument
        HashMap<String, Object> request1 = new HashMap<>();
        request1.put("action", Action.GET_GROUP_LIST);
        try {
            ClientExecutable.serverConnector.getObjOut().writeObject(request1);
            System.out.println("REQUEST FOR GROUPS SENT");
        } catch (IOException e) {
            e.printStackTrace();
        }

        initializeBroadcastButton();
    }

}
