package preproject.client.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.ResourceBundle;

public class ConversationBoxController implements Initializable {

    @FXML
    private Pane chatBoxPane;

    @FXML
    private Label groupAlias_label;

    @FXML
    private Label unreadMsgs_label;

    @FXML
    private Circle group_picture;

    @FXML
    private Button favorite_button;
    boolean isPressed = false;

    public void toggleFavoriteButton(ActionEvent e) {
        if (!isPressed) {
            favorite_button.setStyle("-fx-shape:  \"M462.3 62.6C407.5 15.9 326 24.3 275.7 76.2L256 96.5l-19.7-20.3C186.1 24.3 104.5 15.9 49.7 62.6c-62.8 53.6-66.1 149.8-9.9 207.9l193.5 199.8c12.5 12.9 32.8 12.9 45.3 0l193.5-199.8c56.3-58.1 53-154.3-9.8-207.9z\"; -fx-background-color:  #EDB458");
            isPressed = true;
        } else {
            favorite_button.setStyle("-fx-shape:  \"M462.3 62.6C407.5 15.9 326 24.3 275.7 76.2L256 96.5l-19.7-20.3C186.1 24.3 104.5 15.9 49.7 62.6c-62.8 53.6-66.1 149.8-9.9 207.9l193.5 199.8c12.5 12.9 32.8 12.9 45.3 0l193.5-199.8c56.3-58.1 53-154.3-9.8-207.9z\"; -fx-background-color:  #DDDDDD; -fx-border-color: #EDB458");
            isPressed = false;
        }
    }

    public void mouseEnterFavButton(javafx.scene.input.MouseEvent mouseEvent) {
        if (!isPressed) {
            favorite_button.setStyle("-fx-shape:  \"M462.3 62.6C407.5 15.9 326 24.3 275.7 76.2L256 96.5l-19.7-20.3C186.1 24.3 104.5 15.9 49.7 62.6c-62.8 53.6-66.1 149.8-9.9 207.9l193.5 199.8c12.5 12.9 32.8 12.9 45.3 0l193.5-199.8c56.3-58.1 53-154.3-9.8-207.9z\"; -fx-background-color:  #DDDDDD; -fx-border-color: #EDB458");
        }
    }

    public void mouseExitFavButton(javafx.scene.input.MouseEvent mouseEvent) {
        if (!isPressed) {
            favorite_button.setStyle("-fx-shape:  \"M462.3 62.6C407.5 15.9 326 24.3 275.7 76.2L256 96.5l-19.7-20.3C186.1 24.3 104.5 15.9 49.7 62.6c-62.8 53.6-66.1 149.8-9.9 207.9l193.5 199.8c12.5 12.9 32.8 12.9 45.3 0l193.5-199.8c56.3-58.1 53-154.3-9.8-207.9z\"; -fx-background-color:  #DDDDDD; -fx-border-color: #DDDDDD");
        }
    }


    @FXML
    void mouseEnterChatBox() {
        chatBoxPane.setStyle("-fx-background-color: #DDDDDD; -fx-background-radius: 5");
//        if (!isPressed) {
//            favorite_button.setStyle("-fx-shape:  \"M462.3 62.6C407.5 15.9 326 24.3 275.7 76.2L256 96.5l-19.7-20.3C186.1 24.3 104.5 15.9 49.7 62.6c-62.8 53.6-66.1 149.8-9.9 207.9l193.5 199.8c12.5 12.9 32.8 12.9 45.3 0l193.5-199.8c56.3-58.1 53-154.3-9.8-207.9z\"; -fx-background-color:  #DDDDDD");
//        }
    }

    @FXML
    void mouseExitChatBox() {
        chatBoxPane.setStyle("-fx-background-color: #EEEEEE");
//        if (!isPressed) {
//            favorite_button.setStyle("-fx-shape:  \"M462.3 62.6C407.5 15.9 326 24.3 275.7 76.2L256 96.5l-19.7-20.3C186.1 24.3 104.5 15.9 49.7 62.6c-62.8 53.6-66.1 149.8-9.9 207.9l193.5 199.8c12.5 12.9 32.8 12.9 45.3 0l193.5-199.8c56.3-58.1 53-154.3-9.8-207.9z\"; -fx-background-color:  #EEEEEE");
//        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {    }

}
