package preproject.frontend.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import preproject.backend.models.User;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminSignUpsController implements Initializable {

    @FXML
    private Label userName;

    @FXML
    private Button declineButton;

    @FXML
    private Button acceptButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void setUserHbox(User user){
        userName.setText(user.getFirstName()+" "+user.getLastName());
    }


}
