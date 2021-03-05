package preproject.client.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;

public class ScreenController {

    private Stage window;

    public ScreenController(Node node) {
        this.window = (Stage)node.getScene().getWindow();
    }

    public ScreenController(Stage window) {
        this.window = window;
    }

    public void activateUsingPath(String pathToScene) {
        try{
            window.setScene(new Scene(FXMLLoader.load(getClass().getResource(pathToScene))));
            window.show();
        }catch (IOException e) {
            System.out.println("Error in activating '" + pathToScene);
            e.printStackTrace();
        }
    }

}