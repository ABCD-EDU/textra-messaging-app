package preproject.client.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import preproject.client.*;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class SignInController implements Initializable{

    private ScreenController sController;

    @FXML
    private GridPane grid_pane;

    @FXML
    private Button signIn_btn;

    @FXML
    private TextField pass_field;

    @FXML
    private TextField email_field;

    @FXML
    private Label register_label;

    @FXML
    void fieldClick(javafx.scene.input.MouseEvent mouseEvent) {
        if (mouseEvent.getSource().equals(email_field)) {
            email_field.setStyle("-fx-background-color: #DDDDDD; -fx-border-radius: 3; -fx-border-width: 1; -fx-border-color: #333333;");
            pass_field.setStyle("-fx-background-color: #FAFAFA; -fx-border-radius: 3; -fx-border-width: 1; -fx-border-color: #333333;");
        } else if (mouseEvent.getSource().equals(pass_field)) {
            pass_field.setStyle("-fx-background-color: #DDDDDD; -fx-border-radius: 3; -fx-border-width: 1; -fx-border-color: #333333;");
            email_field.setStyle("-fx-background-color: #FAFAFA; -fx-border-radius: 3; -fx-border-width: 1; -fx-border-color: #333333;");
        }
    }

    @FXML
    void tabPress(javafx.scene.input.KeyEvent keyEvent) {
        if (keyEvent.getSource().equals(email_field)) {
            email_field.setStyle("-fx-background-color: #FAFAFA; -fx-border-radius: 3; -fx-border-width: 1; -fx-border-color: #333333;");
            pass_field.setStyle("-fx-background-color: #DDDDDD; -fx-border-radius: 3; -fx-border-width: 1; -fx-border-color: #333333;");
        } else if (keyEvent.getSource().equals(pass_field)) {
            pass_field.setStyle("-fx-background-color: #FAFAFA; -fx-border-radius: 3; -fx-border-width: 1; -fx-border-color: #333333;");
            email_field.setStyle("-fx-background-color: #DDDDDD; -fx-border-radius: 3; -fx-border-width: 1; -fx-border-color: #333333;");
        }
    }

    @FXML
    @SuppressWarnings("unchecked")
    void loginPressed(ActionEvent event) {
        try {
            String email = email_field.getText();
            String password = pass_field.getText();

            Map<String, String> userRepo = new HashMap<>();

            userRepo.put("action", Action.LOGIN_USER);
            userRepo.put("email", email);
            userRepo.put("password", password);

            ClientExecutable.serverConnector.getObjOut().writeObject(userRepo);
            boolean isSuccessful = (boolean) ClientExecutable.serverConnector.getObjIn().readObject();
            if (!isSuccessful)
                return;
            System.out.println("log in successful");
            Map<String, String> loginRepo = (Map<String, String>) ClientExecutable.serverConnector.getObjIn().readObject();

            boolean isVerified = loginRepo.get("isVerified").equals("1");
            boolean isAdmin = loginRepo.get("isAdmin").equals("1");
//            System.out.println(loginRepo.get("isVerified"));
//            System.out.println(loginRepo.get("isAdmin"));
            System.out.println("is verified: " + isVerified);
            System.out.println("is admin: " + isAdmin);

            if (!isVerified) {
                System.out.println("User is not verified");
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

    @FXML
    void registerLabelPressed() {
        sController = new ScreenController((Stage)(register_label.getScene().getWindow()));
        sController.activateUsingPath("../resources/view/RegisterScreen.fxml");
    }

    /**
     *  Populate sController with potential scenes that the user may go to.
     *  This block executes after calling Constructor and populating @FXML components.
     *  Implementing this will
     *
     *  known problem:
     *  FXMLoader causes an infinite loop.
     *  Reason:
     *  The same line gets ran everytime this class is initialized
     *
     *  TODO: Find an alternative to this implementation. Need to find a way to run code
     *      as soon as FXML is loaded.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        this.signIn_btn.setOnAction(this::loginPressed);
//        try{
//            System.out.println("initializing");
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("../resources/view/SignInScreen.fxml"));
//            Parent root = (Parent)loader.load();
//            Stage stage = (Stage)(register_label.getScene().getWindow());
//            sController = new ScreenController(stage);
//            sController.addScreen("SignIn1", "../resources/view/RegisterScreenOne.fxml");
//        }catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}
