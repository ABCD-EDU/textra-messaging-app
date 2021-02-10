package frontend.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

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
    void loginPressed(ActionEvent event) {

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
