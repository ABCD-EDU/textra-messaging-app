package preproject.frontend.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import preproject.backend.models.User;
import preproject.frontend.Action;
import preproject.frontend.Main;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import static preproject.frontend.State.*;

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
    TableColumn<String, String> email;
    @FXML
    TableColumn<String, String> fName;
    @FXML
    TableColumn<String, String> lName;


    ObservableList<Map<String, String>> registeredUserRepo = FXCollections.observableArrayList();
    ObservableList<Map<String, String>> unRegisteredUserRepo = FXCollections.observableArrayList();

    /**
     * Load all the functions for each component in the admin panel
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeAcceptDeclineAllButton();
        importRegisteredUserRepo();
        importUnRegisteredUserRepo();
        loadUnRegisteredUsersToScrollPane();
        loadRegisteredUsersToTable();
    }

    /**
     * Add all the unregistered users to the scroll pane
     */
    public void loadUnRegisteredUsersToScrollPane() {
        //Declare nodes which will be assigned with HBox nodes where HBox node contains 3 children nodes which are label, declineButton and acceptButton

        Node node = null;
        for (int i = 0; i < unRegisteredUserRepo.size(); i++) {
            int userIndex = i;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../resources/view/UserSignUpHbox.fxml"));
            try {
                node = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            assert node != null;
            for (Node component : ((HBox) node).getChildren()) {
                Node finalNode = node;
                Map<String, String> userRepo = unRegisteredUserRepo.get(i);
                if (component instanceof Label) {
                    ((Label) component).setText(userRepo.get("email"));
                }
                else if (component instanceof Button) {
                    if (component.getId().equals("declineButton")) {
                        ((Button) component).setOnAction(e -> {
                            ((VBox) component.getParent().getParent()).getChildren().remove(finalNode);
                            String email =((Label)((HBox)finalNode).getChildren().get(0)).getText();
                            verifyRegistration(email, 0);
                        });
                    }
                    else if (component.getId().equals("acceptButton")) {
                        ((Button) component).setOnAction(e -> {
                            ((VBox) component.getParent().getParent()).getChildren().remove(finalNode);
                            String email =((Label)((HBox)finalNode).getChildren().get(0)).getText();
                            verifyRegistration(email,1);
                        });
                    }
                }
            }
            signUpVbox.getChildren().add(node);
        }
    }

    public void initializeAcceptDeclineAllButton(){
        acceptAllButton.setOnAction(e-> {
            try {
                Map<String, String> actionMap = new HashMap<>();
                actionMap.put("action", Action.ACCEPT_ALL_USERS);
                updateAllRegistration(actionMap);
                System.out.println("Accepted all");
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        declineAllButton.setOnAction(e-> {
            try {
                Map<String, String> actionMap = new HashMap<>();
                actionMap.put("action", Action.DECLINE_ALL_USERS);
                updateAllRegistration(actionMap);
                System.out.println("Declined all");
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
    }

    private void updateAllRegistration(Map<String, String> actionMap) throws IOException, ClassNotFoundException {
        Main.serverConnector.getObjOut().writeObject(actionMap);
        Map<String, List<Map<String,String>>> mapRead = (Map<String, List<Map<String,String>>>) Main.serverConnector.getObjIn().readObject();
        System.out.println(mapRead.containsKey(SUCCESS_ACCEPT_ALL_USERS));
        System.out.println(mapRead.containsKey(SUCCESS_DECLINE_ALL_USERS));
        if (mapRead.containsKey(SUCCESS_ACCEPT_ALL_USERS)){
            List<Map<String,String>> mapRepo = mapRead.get(SUCCESS_ACCEPT_ALL_USERS);
            unRegisteredUserRepo.clear();
            signUpVbox.getChildren().clear();
            registeredUserRepo = FXCollections.observableArrayList(mapRepo);
            studentTable.getItems().clear();
            loadRegisteredUsersToTable();

        }else { //If SUCCESS DECLINE ALL USERS
            unRegisteredUserRepo.clear();
            signUpVbox.getChildren().clear();
        }
    }

    public void verifyRegistration(String email, int isVerified){
        try {
            Map<String, String> actionMap = new HashMap<>();
            actionMap.put("action", Action.POST_VERIFIED_USERS);
            actionMap.put("email",email);
            actionMap.put("isVerified", Integer.toString(isVerified));
            Main.serverConnector.getObjOut().writeObject(actionMap);
            Map<String, Boolean> mapRead = (Map<String,Boolean>) Main.serverConnector.getObjIn().readObject();
            boolean isAccepted= mapRead.get(SUCCESS_POST_VERIFIED_USERS);

            int index = 0;
            //Find the index of the user that has been accepted or declined
            while (!unRegisteredUserRepo.get(index).get("email").equals(email)) {
                index++;
            }
            if (isAccepted){
                System.out.println("Updated");
                Map<String, String> newlyRegistered = unRegisteredUserRepo.get(index);
                unRegisteredUserRepo.remove(index);//remove the registered user to unregistered ones
                newlyRegistered.replace("verified", "1");//Set the verification
                registeredUserRepo.add(newlyRegistered);
                studentTable.getItems().add(newlyRegistered);
            }else {
                unRegisteredUserRepo.remove(index);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void loadRegisteredUsersToTable() {
        for (Map<String,String> map: registeredUserRepo){
            studentTable.getItems().add(map);
        }
        email.setCellValueFactory(new MapValueFactory("email"));
        fName.setCellValueFactory(new MapValueFactory("firstName"));
        lName.setCellValueFactory(new MapValueFactory("lastName"));
    }

    public void importUnRegisteredUserRepo() {
        try {
            Map<String, String> actionMap = new HashMap<>();
            actionMap.put("action", Action.GET_UNVERIFIED_USERS);
            Main.serverConnector.getObjOut().writeObject(actionMap);
            Map<String, List<Map<String, String>>> mapRead = (Map<String, List<Map<String, String>>>) Main.serverConnector.getObjIn().readObject();
            List<Map<String, String>> mapRepo = mapRead.get(SUCCESS_GET_UNVERIFIED_USERS);
            if (unRegisteredUserRepo == null) {
                System.out.println("error");
            }
            unRegisteredUserRepo =  FXCollections.observableArrayList(mapRepo);

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void importRegisteredUserRepo(){
        try {
            Map<String, String> actionMap = new HashMap<>();
            actionMap.put("action", Action.GET_VERIFIED_USERS);
            Main.serverConnector.getObjOut().writeObject(actionMap);
            Map<String, List<Map<String, String>>> mapRead = (Map<String, List<Map<String, String>>>) Main.serverConnector.getObjIn().readObject();
            List<Map<String, String>> mapRepo = mapRead.get(SUCCESS_GET_VERIFIED_USERS);
            if (mapRepo == null) {
                System.out.println("error");
            }
            registeredUserRepo =  FXCollections.observableArrayList(mapRepo);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}