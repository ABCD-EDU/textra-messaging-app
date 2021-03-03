package preproject.client.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import preproject.server.models.User;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminSignUpsController implements Initializable {

    @FXML
    private HBox userSigningUpHbox;

    @FXML
    private Label userName;

    @FXML
    void mouseEnterBox(javafx.scene.input.MouseEvent mouseEvent) {
        userSigningUpHbox.setStyle("-fx-background-color: #DDDDDD");
    }

    @FXML
    void mouseExitBox(javafx.scene.input.MouseEvent mouseEvent) {
        userSigningUpHbox.setStyle("-fx-background-color: #EEEEEE");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void setUserHbox(User user){
        userName.setText(user.getFirstName()+" "+user.getLastName());
    }
}
