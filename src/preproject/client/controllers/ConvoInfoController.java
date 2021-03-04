package preproject.client.controllers;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.AccessibleAction;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import preproject.client.Action;
import preproject.client.ClientExecutable;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class ConvoInfoController {


    private String currentlySelectedGroupID;
    private List<HashMap<String,String>> groupMembers;
    private Boolean userIsAdmin;
    private List<HashMap<String, String>> searchedUsers;
    private String currentUserEmail;
    private String groupAlias;

    //StackPane that contains the MemberInfo GridPane and Add Members GridPane
    @FXML
    public StackPane stackPane;

    @FXML
    public GridPane convoInfoGridPane;

    @FXML
    public GridPane addMemberGridPane;


    //Show Group Members GridPane
    @FXML
    public  Button convoInfoPaneExitButton;

    @FXML
    public Label convoInfoPaneConvoAliasLabel;

    @FXML
    public Button convoInfoPaneAddMemberButton;

    //Add Members GridPane
    @FXML
    public Button addMemberPaneExitButton;

    @FXML
    public VBox convoInfoPaneVbox;

    @FXML
    public VBox addMemberPaneVbox;

    @FXML
    public TextField addMemberPaneSearchTextField;

    @FXML
    public Button addMemberPaneDoneButton;

    public void setGroupMembers(List<HashMap<String, String>> groupMembers) {
        this.groupMembers = groupMembers;
        convoInfoPaneVbox.getChildren().clear();
        renderMembers();
    }

    public void setUserIsAdmin(Boolean userIsAdmin) {
        this.userIsAdmin = userIsAdmin;
    }

    public void setCurrentlySelectedGroupID(String currentlySelectedGroupID) {
        this.currentlySelectedGroupID = currentlySelectedGroupID;
    }

    public void setCurrentUserEmail(String currentUserEmail) {
        this.currentUserEmail = currentUserEmail;
    }

    public void setGroupAlias(String groupAlias) {
        this.groupAlias = groupAlias;
    }

    public String getGroupAlias() {
        return groupAlias;
    }

    public void requestMembers() {
        HashMap<String, String> request = new HashMap<>();
        request.put("action", Action.GET_GROUP_MEMBERS);
        request.put("groupId", currentlySelectedGroupID);
        try {
            ClientExecutable.serverConnector.getObjOut().writeObject(request);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void renderMembers(){
        Node node = null;
        for (Map<String,String> member: groupMembers){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../resources/view/GroupMembersHbox.fxml"));
            try {
                node = loader.load();
            }catch (Exception e){
                e.printStackTrace();
            }
            assert node!= null;
            convoInfoPaneConvoAliasLabel.setText(groupAlias);

            for (Node component: ((HBox)node).getChildren()){
                Node finalNode = node;
                if (component.getId().equals("email_label")){
                    ((Label) component).setText(member.get("email"));

                }
                if (component.getId().equals("member_name")){
                    String memberName = member.get("firstName") +" "+member.get("lastName");
                    ((Label) component).setText(memberName);
                }

                if (component.getId().equals("remove_button")){
                    //Hide remove button if the user is not the admin of the group
                    if (currentUserEmail.equals(member.get("email"))){
                        component.setVisible(false);
                    }else {
                        ((Button) component).setOnAction(event -> {
                            ((VBox) component.getParent().getParent()).getChildren().remove(finalNode);
                            removeMemberFromBackEnd(member.get("email"));
                        });
                    }
                }
            }

            Node finalNode = node;
            Platform.runLater(()->convoInfoPaneVbox.getChildren().add(finalNode));
        }
    }

    public void removeMemberFromBackEnd(String memberToRemoveEmail){
        try {
            Map<String, String> request = new HashMap<>();
            request.put("action", Action.REMOVE_A_MEMBER);
            request.put("email", memberToRemoveEmail);
            request.put("groupId", currentlySelectedGroupID);
            ClientExecutable.serverConnector.getObjOut().writeObject(request);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void removeMemberFromList(String memberToRemoveEmail){
            List<Map<String, String>> toRemove = groupMembers.stream()
                    .filter(e->e.get("email").equals(memberToRemoveEmail))
                    .collect(Collectors.toList());
            groupMembers.removeAll(toRemove);
            Platform.runLater(()->convoInfoPaneVbox.getChildren().clear());
            renderMembers();
    }

    @FXML
    public void onConvoInfoExit(){
    Stage stage = (Stage)stackPane.getScene().getWindow();
    Platform.runLater(()->stage.close());
    }

    @FXML
    public void onAddMember(){
        convoInfoGridPane.toBack();
        convoInfoGridPane.setVisible(false);
    }
    @FXML
    public void onAddMemberExit(){
        convoInfoGridPane.toFront();
        convoInfoGridPane.setVisible(true);
    }

    @FXML
    public void onSearchInputField(KeyEvent event) {
        if (addMemberPaneSearchTextField.getText().isBlank()){
            addMemberPaneVbox.getChildren().clear();
        }else {
            try {
                Map<String, Object> request = new HashMap<>();
                request.put("action", Action.GET_SEARCHED_USERS);
                request.put("searchQuery", addMemberPaneSearchTextField.getText());
                ClientExecutable.serverConnector.getObjOut().writeObject(request);

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

}
