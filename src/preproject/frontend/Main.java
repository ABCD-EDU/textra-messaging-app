package preproject.frontend;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import preproject.frontend.controllers.AdminController;

public class Main extends Application {

    private Object AdminController;
    public static ServerConnector serverConnector;

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setController(AdminController);
        Parent root = FXMLLoader.load(getClass().getResource("resources/view/AdminScreen.fxml"));
        Scene scene = new Scene(root);
//        scene.getStylesheets().add(getClass().getResource("resources/css/AdminScreenStyle.css").toExternalForm());
        primaryStage.setTitle("Messaging App");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        serverConnector = new ServerConnector();
        serverConnector.start();
        launch(args);
    }
}
