package preproject.frontend.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import preproject.backend.models.User;
import preproject.frontend.Action;
import preproject.frontend.Main;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import static preproject.frontend.State.SUCCESS_GET_UNVERIFIED_USERS;
import static preproject.frontend.State.SUCCESS_GET_VERIFIED_USERS;

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
    TableView<Map<String, String>> studentTable;

    @FXML
    TableColumn<User, String> status;
    @FXML
    TableColumn<User, String> email;
    @FXML
    TableColumn<User, String> fName;
    @FXML
    TableColumn<User, String> lName;


    ObservableList<Map<String, String>> userRegisteredRepo = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadSignUpsToScrollPane();
        importUserRepo();
        loadDataToTable();
    }

    //TODO: Find a better way to implement this
    //Load user info to vbox in the scrollpane
    public void loadSignUpsToScrollPane() {
        //Declare nodes which will be assigned with HBox nodes which will contain 3 children nodes which are label, declineButton and acceptButton

        Node node = null;
        for (int i = 0; i < userRegisteredRepo.size(); i++) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../resources/view/UserSignUpHbox.fxml"));

            try {
                node = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            assert node != null;
            for (Node component : ((HBox) node).getChildren()) {
                Node finalNode = node;
                Map<String, String> userRepo = userRegisteredRepo.get(i);

                if (component instanceof Label) {
                    ((Label) component).setText(userRepo.get("email"));
                }

                else if (component instanceof Button) {
                    if (component.getId().equals("declineButton")) {
                        ((Button) component).setOnAction(e -> {
                            ((VBox) component.getParent().getParent()).getChildren().remove(finalNode);
                        });

                        // TODO: request update to denied
                    }

                    else if (component.getId().equals("acceptButton")) {
                        ((Button) component).setOnAction(e -> {
                            ((VBox) component.getParent().getParent()).getChildren().remove(finalNode);
                        });

                        // TODO: request update to verified
                    }
                }
            }
            signUpVbox.getChildren().add(node);
        }
    }

    public void loadDataToTable() {
//        status.setCellValueFactory(new PropertyValueFactory<>("status"));
        email.setCellValueFactory(new PropertyValueFactory<>("email"));
        fName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        studentTable.setItems(userRegisteredRepo);
    }

    public void importUserRepo() {
        try {
            Map<String, String> actionMap = new HashMap<>();
            actionMap.put("action", Action.GET_UNVERIFIED_USERS);
            Main.serverConnector.getObjOut().writeObject(actionMap);

            Map<String, List<Map<String, String>>> mapRead = (Map<String, List<Map<String, String>>>) Main.serverConnector.getObjIn().readObject();

            List<Map<String, String>> mapRepo = mapRead.get(SUCCESS_GET_UNVERIFIED_USERS);
            if (userRegisteredRepo == null) {
                System.out.println("error");
            }
            System.out.println("SIZEEEEEE " + mapRepo.size());
            userRegisteredRepo.addAll(mapRepo);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


}