package preproject.frontend.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import preproject.backend.Action;
import preproject.backend.Connector;
import preproject.backend.handlers.DataImportHandler;
import preproject.backend.models.User;

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

    //TODO: Find a better way to implement this
    //Load user info to vbox in the scrollpane
    public void loadSignUpsToScrollPane(){

        //Declare nodes which will be assigned with HBox nodes which will contain 3 children nodes which are label, declineButton and acceptButton
        Node[] nodes = new Node[list.size()];
        for (int i=0;i<nodes.length;i++){
            try {
                //Assigns formatted fxml file to each Hbox
                FXMLLoader loader = new FXMLLoader(getClass().getResource("../resources/view/UserSignUpHbox.fxml"));
                nodes[i] = loader.load();
                //Get the 3 nodes from the HBox
                for (Node subNode: ((HBox) nodes[i]).getChildren()){
                    //Check if the node provided is label type then assign the user's first and last names to that node label in the Hbox
                    int currentIndex= i;
                    if (subNode instanceof Label){
                        ((Label) subNode).setText(list.get(i).getFirstName()+" "+ list.get(i).getLastName());
                        //Check if the node has an ID of decline button if true then assign an action even to the button
                    }else if (subNode.getId().equals("declineButton")){
                        ((Button) subNode).setOnAction(action->{
                            System.out.println("No");
                            ((VBox) subNode.getParent().getParent()).getChildren().remove((HBox) nodes[currentIndex]);
//                            //If the action of the admin is decline then he removes that user to the list
//                            list.remove(currentIndex);
                        });
                    }else {
                        ((Button) subNode).setOnAction(action -> {
                            System.out.println("Yes");
                            ((VBox) subNode.getParent().getParent()).getChildren().remove((HBox) nodes[currentIndex]);
//                            //If the action of the admin is decline then he removes that user to the list
//                            list.remove(currentIndex);
                        });
                    }}

                //Add an Hbox node to the vbox node
                signUpVbox.getChildren().add(nodes[i]);
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
