package preproject.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ClientExecutable extends Application {
    public static ServerConnector serverConnector;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("resources/view/SignInScreen.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setTitle("Messaging App");
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setOnCloseRequest(e -> serverConnector.close());
        primaryStage.setResizable(false);
    }

    public static void main(String[] args) {
        serverConnector = new ServerConnector();
        serverConnector.start();
        launch(args);
    }

    @Override
    public void stop() {
        serverConnector.close();
    }

}
