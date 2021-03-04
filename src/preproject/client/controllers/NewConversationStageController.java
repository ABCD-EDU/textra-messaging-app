package preproject.client.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import preproject.client.Action;
import preproject.client.ClientExecutable;

import java.io.IOException;
import java.util.*;

public class NewConversationStageController {

    @FXML
    private Stage newConvoStage;

    @FXML
    private TextField groupAlias_field;

    @FXML
    private ScrollPane members_sPane;

    @FXML
    private VBox members_vBox;

    @FXML
    private Button add_button;

    @FXML
    private Button save_button;

    @FXML
    private Button cancel_button;

    private String email;

    @FXML
    void onAdd(ActionEvent event) {
        TextField newMember = new TextField();
        members_vBox.getChildren().add(newMember);
    }

    @FXML
    void onCancel(ActionEvent event) {
        newConvoStage.close();
    }

    @FXML
    void onSave(ActionEvent event) {
        List<String> members = new ArrayList<>();
        for (Node component : members_vBox.getChildren()) {
            members.add(((TextField)component).getText().trim());
        }
        boolean isSuccessful = createNewConversation(groupAlias_field.getText(), members);
        previewResult(isSuccessful);
    }

    private void previewResult(boolean success) {
        Stage stage = new Stage();
        Label label = new Label();
        label.setStyle("-fx-background-color: #EEEEEE; -fx-font-size: 14px; -fx-font-weight: BOLD");

        VBox layout = new VBox();
        layout.setStyle("-fx-background-color: #EEEEEE");
        layout.setAlignment(Pos.CENTER);
        layout.setSpacing(12);

        Button button = new Button("Continue");
        button.setStyle("-fx-background-color: #333333; -fx-text-fill: #FFFFFF; ");
        button.setPrefSize(80, 25);
        layout.getChildren().add(label);
        layout.getChildren().add(button);
        Scene scene = new Scene(layout, 230,100);
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        if (success) {
            label.setText("Group creation successful.");
            button.setOnMouseClicked((e) -> {
                this.newConvoStage.close();
                stage.close();
            });
        }else {
            label.setText("UNSUCCESSFUL group creation!");
            button.setOnMouseClicked((e) -> stage.close());
        }
        stage.show();
    }

    private boolean createNewConversation(String alias, List<String> members) {
        Map<String, Object> userRepo = new HashMap<>();
        userRepo.put("action", Action.ADD_GROUP);

        userRepo.put("alias", alias);
        userRepo.put("creator", this.email); // valid
        userRepo.put("members", members);

        System.out.println("REQUESTED NEW CONVERSATION");
        try {
            ClientExecutable.serverConnector.getObjOut().writeObject(userRepo);
            return true;
        }catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    void setEmail(String email) {
        this.email = email;
    }

}
