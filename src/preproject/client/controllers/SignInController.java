package preproject.client.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import preproject.client.*;
import javafx.scene.input.KeyEvent;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class SignInController implements Initializable{

    private ScreenController sController;

    @FXML
    private Label error_message;

    @FXML
    private TextField email_field;

    @FXML
    private PasswordField pass_field;

    @FXML
    private Label register_label;

    @FXML
    private Button signIn_btn;

    @FXML
    void fieldClicked(MouseEvent mouseEvent) {
        error_message.setVisible(false);
        if (mouseEvent.getSource().equals(email_field)) {
            email_field.setStyle("-fx-background-color: #FAFAFA; -fx-border-radius: 3; -fx-border-width: 1; -fx-border-color: #333333;");
            pass_field.setStyle("-fx-background-color: #FAFAFA; -fx-border-radius: 3; -fx-border-width: 1; -fx-border-color: #AAAAAA;");
        } else if (mouseEvent.getSource().equals(pass_field)) {
            pass_field.setStyle("-fx-background-color: #FAFAFA; -fx-border-radius: 3; -fx-border-width: 1; -fx-border-color: #333333;");
            email_field.setStyle("-fx-background-color: #FAFAFA; -fx-border-radius: 3; -fx-border-width: 1; -fx-border-color: #AAAAAA;");
        }
    }

    @FXML
    void keyPressed(KeyEvent keyEvent) {
        error_message.setVisible(false);
        if (keyEvent.getSource().equals(email_field) && keyEvent.getCode() == KeyCode.TAB) {
            email_field.setStyle("-fx-background-color: #FAFAFA; -fx-border-radius: 3; -fx-border-width: 1; -fx-border-color: #AAAAAA;");
            pass_field.setStyle("-fx-background-color: #FAFAFA; -fx-border-radius: 3; -fx-border-width: 1; -fx-border-color: #333333;");
        } else if (keyEvent.getSource().equals(pass_field) && keyEvent.getCode() == KeyCode.TAB) {
            pass_field.setStyle("-fx-background-color: #FAFAFA; -fx-border-radius: 3; -fx-border-width: 1; -fx-border-color: #AAAAAA;");
            email_field.setStyle("-fx-background-color: #FAFAFA; -fx-border-radius: 3; -fx-border-width: 1; -fx-border-color: #333333;");
        } else if (keyEvent.getSource().equals(pass_field) && keyEvent.getCode() == KeyCode.ENTER) {
            handleLogin();
        }
    }

    @FXML
    void hoverEnter() {
        register_label.setStyle("-fx-text-fill: #3498DB; -fx-font-size: 12px");
    }

    @FXML
    void hoverExit() {
        register_label.setStyle("-fx-text-fill: #000000; -fx-font-size: 12px");
    }

    @FXML
    void loginPressed() {
      handleLogin();
    }

    void handleLogin() {
        try {
            String email = email_field.getText();
            String password = pass_field.getText();

            Map<String, String> userRepo = new HashMap<>();

            userRepo.put("action", Action.LOGIN_USER);
            userRepo.put("email", email);
            userRepo.put("password", password);

            ClientExecutable.serverConnector.getObjOut().writeObject(userRepo);
            boolean isSuccessful = (boolean) ClientExecutable.serverConnector.getObjIn().readObject();
            if (!isSuccessful) {
                displayErrorMsg("unsuccessful");
                return;
            }
            System.out.println("log in successful");
            Map<String, String> loginRepo = (Map<String, String>) ClientExecutable.serverConnector.getObjIn().readObject();

            boolean isVerified = Boolean.parseBoolean(loginRepo.get("isVerified"));
            boolean isAdmin = Boolean.parseBoolean(loginRepo.get("isAdmin"));
            System.out.println("is verified: " + isVerified);
            System.out.println("is admin: " + isAdmin);

            if (!isVerified) {
                System.out.println("User is not verified");
                displayErrorMsg("pending");
            }else if (isAdmin) {
                System.out.println("User is admin");
                sController = new ScreenController((Stage) (signIn_btn.getScene().getWindow()));
                sController.activateUsingPath("../resources/view/AdminScreen.fxml");
            }else {
                System.out.println("User is verified");
                sController = new ScreenController((Stage) (signIn_btn.getScene().getWindow()));
                sController.activateUsingPath("../resources/view/ChatScreen.fxml");
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    void displayErrorMsg(String type) {
        if (type.equalsIgnoreCase("unsuccessful")) {
            error_message.setStyle("-fx-text-fill:  #DB5461; -fx-font-size: 12px; -fx-font-weight: bold");
            error_message.setText("Incorrect email or password");
            error_message.setVisible(true);
            email_field.setStyle("-fx-background-color: #FAFAFA; -fx-border-radius: 3; -fx-border-width: 1; -fx-border-color: #DB5461;");
            pass_field.setStyle("-fx-background-color: #FAFAFA; -fx-border-radius: 3; -fx-border-width: 1; -fx-border-color: #DB5461;");
        } else if (type.equalsIgnoreCase("pending")) {
            error_message.setStyle("-fx-text-fill:  #00A676; -fx-font-size: 12px; -fx-font-weight: bold");
            error_message.setText("User is not verified");
            error_message.setVisible(true);
        }
    }


    @FXML
    void registerLabelPressed() {
        sController = new ScreenController((Stage)(register_label.getScene().getWindow()));
        sController.activateUsingPath("../resources/view/RegisterScreen.fxml");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
