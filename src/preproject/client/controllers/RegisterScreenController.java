package preproject.client.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import preproject.client.Action;
import preproject.client.ClientExecutable;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RegisterScreenController {

    private ScreenController sController;

    @FXML
    private VBox vBox;

    @FXML
    private Label emptyField_warning;

    @FXML
    private Label email_warning;

    @FXML
    private Label password_warning;

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
    private Button register_button;

    @FXML
    private Label goBack_label;

    @FXML
    void goBackLabelPressed(MouseEvent event) {
        sController = new ScreenController(vBox);
        sController.activateUsingPath("../resources/view/SignInScreen.fxml");
    }

    @FXML
    void registerButtonPressed(ActionEvent event) throws IOException, ClassNotFoundException {

        boolean fieldsAreValid = true;

        if (hasBlankFields()) {
            emptyField_warning.setVisible(true);
            fieldsAreValid = false;
        }
        emptyField_warning.setVisible(false);

        String email = email_field.getText().trim();
        String fName = fName_field.getText().trim();
        String lName = lName_field.getText().trim();
        String passWord = pass_field.getText().trim();
        String cPassWord = cPass_field.getText().trim();

        // validate email, fName, lastName here

        if (!passWord.equals(cPassWord)) {
            password_warning.setVisible(true);
            fieldsAreValid = false;
        }else password_warning.setVisible(false);

        if (fieldsAreValid) {
            Map<String, String> userRepo = new HashMap<>();

            userRepo.put("action", Action.REGISTER_USER);
            userRepo.put("email", email);
            userRepo.put("password", passWord);
            userRepo.put("firstName", fName);
            userRepo.put("lastName", lName);

            ClientExecutable.serverConnector.getObjOut().writeObject(userRepo);
            boolean result = (Boolean) ClientExecutable.serverConnector.getObjIn().readObject();
            if (result) {
                System.out.println("successful registration");
            }else {
                System.out.println("UNSUCCESSFUL REGISTRATION");
            }
        }

    }

    private boolean hasBlankFields() {
        if (email_field.getText().trim().isBlank())
            email_field.setStyle("-fx-border-color: red ; -fx-border-width: 2px ; -fx-border-radius: 2px");
        else email_field.setStyle("-fx-border-width: 0px");
        if (fName_field.getText().trim().isBlank())
            fName_field.setStyle("-fx-border-color: red ; -fx-border-width: 2px ; -fx-border-radius: 2px");
        else fName_field.setStyle("-fx-border-width: 0px");
        if (lName_field.getText().trim().isBlank())
            lName_field.setStyle("-fx-border-color: red ; -fx-border-width: 2px ; -fx-border-radius: 2px");
        else lName_field.setStyle("-fx-border-width: 0px");
        if (pass_field.getText().trim().isBlank())
            pass_field.setStyle("-fx-border-color: red ; -fx-border-width: 2px ; -fx-border-radius: 2px");
        else pass_field.setStyle("-fx-border-width: 0px");
        if (cPass_field.getText().trim().isBlank())
            cPass_field.setStyle("-fx-border-color: red ; -fx-border-width: 2px ; -fx-border-radius: 2px");
        else cPass_field.setStyle("-fx-border-width: 0px");

        return email_field.getText().trim().isBlank() || fName_field.getText().trim().isBlank() ||
                lName_field.getText().trim().isBlank() || pass_field.getText().trim().isBlank() ||
                cPass_field.getText().trim().isBlank();
    }

}
