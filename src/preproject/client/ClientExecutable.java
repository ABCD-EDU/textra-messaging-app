package preproject.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ClientExecutable extends Application {

    private Object AdminController;
    public static ServerConnector serverConnector;

    // TODO: MAKE SURE THAT USER DISCONNECTS WHEN WINDOW IS CLOSED
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setController(AdminController);
        Parent root = FXMLLoader.load(getClass().getResource("resources/view/SignInScreen.fxml"));
        Scene scene = new Scene(root);
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
