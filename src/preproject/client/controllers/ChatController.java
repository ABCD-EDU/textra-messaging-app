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
import preproject.client.Action;
import preproject.client.ClientExecutable;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.*;

public class ChatController implements Initializable {

    private ClientThread clientThread;
    private String name;
    private ArrayList<Message> messages;
    private String ID;
    private String email;
    private List<Map<String, String>> groupsList;
    private String currentlySelectedGroupID;

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
    private ScrollPane messages_scrollPane;

    @FXML
    private VBox people_vBox;

    @FXML
    private Button broadcast_button;

    @FXML
    private TextArea message_area;

    @FXML
    private VBox messages_vBox;

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
                        renderGroupsList(sortGroupList(groupsList));
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
            renderGroupsList(sortGroupList(groupsList));
        List<Map<String, String>> results = new ArrayList<>();
        for (Map<String, String> group : groupsList) {
            if (group.get("alias").startsWith(searchContacts_field.getText().trim())) {
                results.add(group);
            }
        }
        renderGroupsList(sortGroupList(results));
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
        renderGroupsList(sortGroupList(groupsList));
    }

    private void handleMessageSend(String message) throws IOException {
        Message[] messages = processMessage(message);
        sendMessage(messages);
        previewMessage(messages);
    }

    private Message[] processMessage(String message) {
        System.out.println(message);
        // Include additional parsing - specify the max amount of characters per line
        // so that there will be no problems with wrapping
        String[] msgData = message.split("\n");
        Message[] messages = new Message[msgData.length];

        for (int i = 0; i < msgData.length; i++) {
            Timestamp time = new Timestamp(new Date().getTime());
            messages[i] = new Message(this.ID,time,this.name,msgData[i]);
        }

        return messages;
    }

    /**
     * Send message to database here
     */
    private void sendMessage(Message[] msgData) throws IOException {
        Map<String, Object> request = new HashMap<>();
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
            System.out.println("== previewing message: ");
            System.out.println(msgData[i]);
            boolean primary = true;
            if (messages.size() != 0)
                primary = !messages.get(messages.size()-1).getSenderID().equals(msgData[i].getSenderID());
            System.out.println(messages.toString());
            FXMLLoader loader = null;
//            System.out.println(loader.toString());
            if (primary){
                loader = new FXMLLoader(getClass().getResource("../resources/view/MessageBoxPrimary.fxml"));
                System.out.println("Loaded Primary");
            } else {
                loader = new FXMLLoader(getClass().getResource("../resources/view/MessageBoxSecondary.fxml"));
                System.out.println("Loaded Secondary");
            }
//            System.out.println(loader.toString());

            try { node = loader.load(); }
            catch (IOException e) { e.printStackTrace(); }

            assert node != null;

            for (Node component : ((Pane) node).getChildren()) {
                System.out.println(component.getId());
                if (component.getId().equals("message_label"))
                    ((Label)component).setText(msgData[i].getMessage());
                else if (component.getId().equals("name_label"))
                    ((Label)component).setText(msgData[i].getSenderName());
            }

            System.out.println("NOde added " + primary);
            messages.addAll(Arrays.asList(msgData));
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
        if (!message.get("address").equals(this.currentlySelectedGroupID)) return;
        Message[] msgData = {new Message(
                message.get("sender"),
                Timestamp.valueOf(message.get("timeSent")),
                message.get("sender"),
                message.get("message")
        )};
        previewMessage(msgData);
    }

    private void renderGroupsList(List<Map<String, String>> groupsList) {
        System.out.println("Render Groups List: " + groupsList.size());
        try {
            System.out.println("GROUP REQUEST ACCEPTED NUMBER OF GROUPS: " + groupsList.size());
            Platform.runLater(() -> people_vBox.getChildren().clear());
//            sortGroupList();
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
                    setConversationHeader(groupMap.get("alias"), String.valueOf(this.ID).equals(groupMap.get("uidAdmin")));
                    currentlySelectedGroupID = groupMap.get("groupId");
                });

                for (Node component : ((Pane)node).getChildren()) {
                    if (component.getId().equals("groupAlias_label")) {
                        ((Label)component).setText(alias);
                    }
                    if (component.getId().equals("favorite_button")) {
                        ((RadioButton)component).setSelected(groupMap.get("is_fav").equals("1"));
                        ((RadioButton)component).selectedProperty().addListener((obs, wasPreviouslySelected, isNowSelected) -> {
                            Map<String, String> request = new HashMap<>();
                            request.put("action", Action.ADD_FAVOURITE);
                            request.put("userId", this.ID);
                            request.put("groupId", groupMap.get("groupId"));
                            if (isNowSelected) { // CHECK IF IT EVEN ENTERS IF AND ELSE
                                System.out.println("radio box sele3cted");
                                request.put("isFav", "1");
                            } else {
                                request.put("isFav", "0");
                                System.out.println("radio box not selected");
                            }
                            System.out.println("toggle request: ");
                            System.out.println(request.get("action"));
                            System.out.println(request.get("userId"));
                            System.out.println(request.get("isFav"));
                            try {
                                ClientExecutable.serverConnector.getObjOut().writeObject(request);
                            } catch (IOException e) {
                                e.printStackTrace();
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
     * TODO: algorithm is trash pls optimize lmao
     * Algorithm:
     * 1. add all favorites
     * 2. add all non favorites
     * 3. return
     */
    private List<Map<String, String>> sortGroupList(List<Map<String, String>> groupsList) {

        System.out.println("Sort Groups List: " + groupsList.size());
        List<Map<String, String>> sortedList = new ArrayList<>();
        System.out.println(groupsList.size());
        for (int i = 0; i < groupsList.size(); i++) { // put all favorites in sortedList
            Map<String, String> group = groupsList.get(i);
            if (group.get("is_fav").equals("1"))
                sortedList.add(group);
        }
        for (int i = 0; i < groupsList.size(); i++) { // put all not favorites in sortedList
            Map<String, String> group = groupsList.get(i);
            if (group.get("is_fav").equals("0"))
                sortedList.add(group);
        }
        System.out.println("size after sorting: " + this.groupsList.size());
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
        this.name = userInformation.get("firstName") + userInformation.get("lastName");
        this.email = userInformation.get("email");
        email_label.setText(this.email);
    }

    /**
     * Remodel the header_pane into just a text box where user can input names for new conversation
     */
    @FXML
    private void onNewConversationClick(MouseEvent event) {
        header_pane.getChildren().removeIf(component -> component.getId().equals("header_label"));
        header_pane.getChildren().removeIf(component -> component.getId().equals("header_button"));
        TextField aliasField = new TextField();
        aliasField.setPromptText("conference name");
        header_pane.getChildren().add(aliasField);
        TextField membersField = new TextField();
        membersField.setPromptText("members");
        header_pane.getChildren().add(membersField);
        Button newConvoButton = new Button("add");
        header_pane.getChildren().add(newConvoButton);
        newConvoButton.setLayoutX(100);
        aliasField.setLayoutX(300);
        newConvoButton.setOnAction(keyEvent -> {
            System.out.println("new conversation button pressed");
            System.out.println(aliasField.getText().trim());
            System.out.println(membersField.getText().trim());
            try {
                createNewConversation(aliasField.getText().trim(),
                        membersField.getText().trim());
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        });

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

    private void createNewConversation(String alias, String members) throws IOException, ClassNotFoundException {
        Map<String, Object> userRepo = new HashMap<>();
        userRepo.put("action", Action.ADD_GROUP);

        userRepo.put("alias", alias);
        userRepo.put("creator", this.email); // valid
        userRepo.put("members", new ArrayList<>(Arrays.asList(members.split(","))));
        System.out.println(userRepo.get("members"));

        ClientExecutable.serverConnector.getObjOut().writeObject(userRepo);
        System.out.println("REQUESTED NEW CONVERSATION");
//        Main.serverConnector.getObjIn().readObject();
    }

    /**
     *  Set values of header, text placeholder, listOfPeople, messages
     *  TODO: Clean this code
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        clientThread = new ClientThread();
        clientThread.start();
        currentlySelectedGroupID = null;
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

    }

}