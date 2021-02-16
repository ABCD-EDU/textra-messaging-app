package preproject.frontend.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import preproject.backend.Connector;
import preproject.backend.handlers.DataImportHandler;
import preproject.backend.models.User;

import java.awt.event.ActionEvent;
import java.net.URL;
import java.sql.Array;
import java.sql.ResultSet;
import java.util.*;

public class AdminController implements Initializable {


    @FXML
    private Button declineAllButton;

    @FXML
    private Button acceptAllButton;


    @FXML
    private Button previous;

    @FXML
    private Button next;

    @FXML
    private VBox signUpVbox;

    @FXML
    TableView<User> studentTable;

    @FXML
    TableColumn<User, String> status;
    @FXML
    TableColumn<User, String> email;
    @FXML
    TableColumn<User, String> fName;
    @FXML
    TableColumn<User, String> lName;


    ObservableList<User> list = FXCollections.observableArrayList(getDataFromDB());

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        loadSignUpsToScrollPane();
        loadDataToTable();
    }

    //Load user info to vbox in the scrollpane
    public void loadSignUpsToScrollPane(){
        Node[] nodes = new Node[list.size()];
        for (int i=0;i<nodes.length;i++){
            try {
                //Assigns formatted fxml file to each Hbox
                FXMLLoader loader = new FXMLLoader(getClass().getResource("../resources/view/UserSignUpHbox.fxml"));
                AdminSignUpsController controller = new AdminSignUpsController();
                loader.setController(controller);
                nodes[i] = loader.load();
                //Add an Hbox node to the vbox node
                signUpVbox.getChildren().add(nodes[i]);
                controller.setUserHbox(list.get(i));
            }catch (Exception e){
                e.printStackTrace();
            }
         }
    }

    public void loadDataToTable(){
        email.setCellValueFactory(new PropertyValueFactory<User, String>("email"));
        fName.setCellValueFactory(new PropertyValueFactory<User, String>("firstName"));
        lName.setCellValueFactory(new PropertyValueFactory<User, String>("lastName"));
        studentTable.setItems(list);
    }

    public List<User> getDataFromDB() {
        final String DATABASE = "messenger.user_acc";
        Connector.createConnection();
        Connector connector = new Connector();
        ResultSet table = connector.readDatabase(DATABASE);
        return DataImportHandler.parseUserTable(table);
    }
}
