package preproject.frontend.controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import preproject.frontend.*;

import java.io.IOException;
import java.net.PasswordAuthentication;
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
    @SuppressWarnings("unchecked")
    void loginPressed(ActionEvent event) {
        try {
            String email = email_field.getText();
            String password = pass_field.getText();

            Map<String, String> userRepo = new HashMap<>();

            userRepo.put("action", Action.LOGIN_USER);
            userRepo.put("email", email);
            userRepo.put("password", password);

            List<Map<String, String>> mapRepo = new ArrayList<>();
            mapRepo.add(userRepo);

            Main.serverConnector.getObjOut().writeObject(userRepo);
            Map<String, String> loginRepo = (Map<String, String>) Main.serverConnector.getObjIn().readObject();

            System.out.println("userid: " + loginRepo.get("userId"));
            System.out.println("email: " + loginRepo.get("email"));
            System.out.println("first name: " + loginRepo.get("firstName"));
            System.out.println("last name: " + loginRepo.get("lastName"));
            System.out.println("is verified: " + loginRepo.get("isVerified"));
            System.out.println("is admin: " + loginRepo.get("isAdmin"));

            if (Boolean.parseBoolean(loginRepo.get("isVerified")) && Boolean.parseBoolean(loginRepo.get("isAdmin"))) {
                sController = new ScreenController((Stage) (signIn_btn.getScene().getWindow()));
                sController.activateUsingPath("../resources/view/AdminScreen.fxml");
            }

            if (Boolean.parseBoolean(loginRepo.get("isVerified")) && !Boolean.parseBoolean(loginRepo.get("isAdmin"))) {
                sController = new ScreenController((Stage) (signIn_btn.getScene().getWindow()));
                System.out.println("USER VERIFIED");
//                sController.activateUsingPath("src/preproject/frontend/resources/view/AdminScreen.fxml");
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
