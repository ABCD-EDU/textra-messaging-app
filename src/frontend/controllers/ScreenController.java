package frontend.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;

public class ScreenController {

    HashMap<String, Scene> scenes;
    private Stage window;

    public ScreenController(ActionEvent event) {
        this.window = (Stage)((Node)event.getSource()).getScene().getWindow();
        scenes = new HashMap<>();
    }

    public ScreenController(Node node) {
        this.window = (Stage)node.getScene().getWindow();
        scenes = new HashMap<>();
    }

    public ScreenController(Stage window) {
        this.window = window;
        scenes = new HashMap<>();
    }

    public ScreenController() {
        this.window = null;
        scenes = new HashMap<>();
    }

    public void setWindow(Stage window) {
        this.window = window;
    }

    public void setWindow(ActionEvent event) {
        this.window = (Stage)((Node)event.getSource()).getScene().getWindow();
    }

    public void addScreen(String name, String pathToScene) {
        try{
            scenes.put(name, new Scene(FXMLLoader.load(getClass().getResource(pathToScene))));
        }catch (IOException e) {
            System.out.println("Error in putting '" + name + "'screen in scenes hashmap");
            e.printStackTrace();
        }
    }

    public void removeScreen(String name){
        try{
            if (!scenes.containsKey(name)) throw new IllegalArgumentException();
            scenes.remove(name);
        }catch (IllegalArgumentException e) {
            System.out.println("\"" + name + "does not exist within the hashmap");
            e.printStackTrace();
        }
    }

    public void activate(String name){
        try{
            if (!scenes.containsKey(name)) throw new IllegalArgumentException();
            window.setScene(scenes.get(name));
            window.show();
        }catch (IllegalArgumentException e) {
            System.out.println("\"" + name + "does not exist within the hashmap");
            e.printStackTrace();
        }
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