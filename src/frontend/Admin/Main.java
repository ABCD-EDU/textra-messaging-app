package frontend.Admin;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;

public class Main extends Application {


    public void start(Stage primaryStage) throws Exception{
//        FXMLLoader loader = new FXMLLoader(new File("resources/view/structure.fxml").toURI().toURL());
        Parent root = FXMLLoader.load(getClass().getResource("resources/view/structure.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("resources/css/style.css").toExternalForm());
        primaryStage.setTitle("Admin Panel");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
