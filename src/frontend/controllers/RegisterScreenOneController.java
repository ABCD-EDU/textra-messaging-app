package frontend.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class RegisterScreenOneController {

    private ScreenController sController;

    @FXML
    private GridPane grid_pane;

    @FXML
    private TextField firstName_field;

    @FXML
    private TextField lastName_field;

    @FXML
    private Button next_button;

    @FXML
    private Label goBack_label;

    @FXML
    void goBackLabelPressed() {
        sController = new ScreenController((Stage)(grid_pane.getScene().getWindow()));
        sController.activateUsingPath("../resources/view/SignInScreen.fxml");
    }

    @FXML
    void nextButtonPressed(ActionEvent event) {

    }

}
