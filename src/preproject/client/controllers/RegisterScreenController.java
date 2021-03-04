package preproject.client.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import preproject.client.Action;
import preproject.client.ClientExecutable;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RegisterScreenController {

    @FXML
    private VBox vBox;

    @FXML
    private Label warning_field;

    @FXML
    private TextField email_field;

    @FXML
    private TextField fName_field;

    @FXML
    private TextField lName_field;

    @FXML
    private PasswordField pass_field;

    @FXML
    private PasswordField cPass_field;

    @FXML
    private Label signIn_label;

    @FXML
    void fieldClicked(MouseEvent mouseEvent) {
        if (mouseEvent.getSource().equals(email_field)) {
            resetFieldFocus();
            email_field.setStyle("-fx-background-color: #FAFAFA; -fx-border-radius: 3; -fx-border-width: 1; -fx-border-color: #333333;");
        } else if (mouseEvent.getSource().equals(fName_field)) {
            resetFieldFocus();
            fName_field.setStyle("-fx-background-color: #FAFAFA; -fx-border-radius: 3; -fx-border-width: 1; -fx-border-color: #333333;");
        } else if (mouseEvent.getSource().equals(lName_field)) {
            resetFieldFocus();
            lName_field.setStyle("-fx-background-color: #FAFAFA; -fx-border-radius: 3; -fx-border-width: 1; -fx-border-color: #333333;");
        } else if (mouseEvent.getSource().equals(pass_field)) {
            resetFieldFocus();
            pass_field.setStyle("-fx-background-color: #FAFAFA; -fx-border-radius: 3; -fx-border-width: 1; -fx-border-color: #333333;");
        } else if (mouseEvent.getSource().equals(cPass_field)) {
            resetFieldFocus();
            cPass_field.setStyle("-fx-background-color: #FAFAFA; -fx-border-radius: 3; -fx-border-width: 1; -fx-border-color: #333333;");
        }
    }

    @FXML
    void keyPressed(KeyEvent keyEvent) {
        if (keyEvent.getSource().equals(email_field) && keyEvent.getCode() == KeyCode.TAB) {
            resetFieldFocus();
            fName_field.setStyle("-fx-background-color: #FAFAFA; -fx-border-radius: 3; -fx-border-width: 1; -fx-border-color: #333333;");
        } else if (keyEvent.getSource().equals(fName_field) && keyEvent.getCode() == KeyCode.TAB) {
            resetFieldFocus();
            lName_field.setStyle("-fx-background-color: #FAFAFA; -fx-border-radius: 3; -fx-border-width: 1; -fx-border-color: #333333;");
        } else if (keyEvent.getSource().equals(lName_field) && keyEvent.getCode() == KeyCode.TAB) {
            resetFieldFocus();
            pass_field.setStyle("-fx-background-color: #FAFAFA; -fx-border-radius: 3; -fx-border-width: 1; -fx-border-color: #333333;");
        } else if (keyEvent.getSource().equals(pass_field) && keyEvent.getCode() == KeyCode.TAB) {
            resetFieldFocus();
            cPass_field.setStyle("-fx-background-color: #FAFAFA; -fx-border-radius: 3; -fx-border-width: 1; -fx-border-color: #333333;");
        } else if (keyEvent.getSource().equals(cPass_field) && keyEvent.getCode() == KeyCode.TAB) {
            resetFieldFocus();
            email_field.setStyle("-fx-background-color: #FAFAFA; -fx-border-radius: 3; -fx-border-width: 1; -fx-border-color: #333333;");
        }
    }

    void resetFieldFocus() {
        email_field.setStyle("-fx-background-color: #FAFAFA; -fx-border-radius: 3; -fx-border-width: 1; -fx-border-color: #AAAAAA;");
        fName_field.setStyle("-fx-background-color: #FAFAFA; -fx-border-radius: 3; -fx-border-width: 1; -fx-border-color: #AAAAAA;");
        lName_field.setStyle("-fx-background-color: #FAFAFA; -fx-border-radius: 3; -fx-border-width: 1; -fx-border-color: #AAAAAA;");
        pass_field.setStyle("-fx-background-color: #FAFAFA; -fx-border-radius: 3; -fx-border-width: 1; -fx-border-color: #AAAAAA;");
        cPass_field.setStyle("-fx-background-color: #FAFAFA; -fx-border-radius: 3; -fx-border-width: 1; -fx-border-color: #AAAAAA;");
    }

    @FXML
    void hoverEnter() {
        signIn_label.setStyle("-fx-text-fill: #3498DB; -fx-font-size: 12px");
    }

    @FXML
    void hoverExit() {
        signIn_label.setStyle("-fx-text-fill: #000000; -fx-font-size: 12px");
    }

    @FXML
    void goBackLabelPressed() {
        ScreenController sController = new ScreenController(vBox);
        sController.activateUsingPath("../resources/view/SignInScreen.fxml");
    }

    @FXML
    void registerButtonPressed() throws IOException, ClassNotFoundException {

        boolean fieldsAreValid = true;

        if (hasBlankFields()) {
            warning_field.setText("Please fill up the fields.");
            warning_field.setVisible(true);
            fieldsAreValid = false;
        } else {
            warning_field.setVisible(false);
        }

        String email = email_field.getText().trim();
        String fName = fName_field.getText().trim();
        String lName = lName_field.getText().trim();
        String password = pass_field.getText().trim();
        String cPassword = cPass_field.getText().trim();

        // validate email, fName, lastName here
        if (!password.equals(cPassword)) {
            warning_field.setText("Password does not match.");
            warning_field.setVisible(true);
            fieldsAreValid = false;
        }

        if (fieldsAreValid) {
            Map<String, String> userRepo = new HashMap<>();

            userRepo.put("action", Action.REGISTER_USER);
            userRepo.put("email", email);
            userRepo.put("password", password);
            userRepo.put("firstName", fName);
            userRepo.put("lastName", lName);

            ClientExecutable.serverConnector.getObjOut().writeObject(userRepo);
            boolean result = (Boolean) ClientExecutable.serverConnector.getObjIn().readObject();
            if (result) {
                System.out.println("successful registration");
            }else {
                warning_field.setText("Something came up, please try again.");
                warning_field.setVisible(true);
                System.out.println("UNSUCCESSFUL REGISTRATION");
            }
        }
    }

    private boolean hasBlankFields() {
        if (email_field.getText().trim().isBlank())
            email_field.setStyle("-fx-border-color: #DB5461 ; -fx-border-width: 1px ; -fx-border-radius: 3px");
        if (fName_field.getText().trim().isBlank())
            fName_field.setStyle("-fx-border-color: #DB5461 ; -fx-border-width: 1px ; -fx-border-radius: 3px");
        if (lName_field.getText().trim().isBlank())
            lName_field.setStyle("-fx-border-color: #DB5461 ; -fx-border-width: 1px ; -fx-border-radius: 3px");
        if (pass_field.getText().trim().isBlank())
            pass_field.setStyle("-fx-border-color: #DB5461 ; -fx-border-width: 1px ; -fx-border-radius: 3px");
        if (cPass_field.getText().trim().isBlank())
            cPass_field.setStyle("-fx-border-color: #DB5461 ; -fx-border-width: 1px ; -fx-border-radius: 3px");

        return email_field.getText().trim().isBlank() || fName_field.getText().trim().isBlank() ||
                lName_field.getText().trim().isBlank() || pass_field.getText().trim().isBlank() ||
                cPass_field.getText().trim().isBlank();
    }

}
