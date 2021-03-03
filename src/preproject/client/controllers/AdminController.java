package preproject.client.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import preproject.client.Action;
import preproject.client.ClientExecutable;
import preproject.server.models.User;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class AdminController implements Initializable {


    @FXML
    private Button declineAllButton;

    @FXML
    private Button acceptAllButton;

    @FXML
    private Button logoutButton;

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

    private ObservableList<Map<String, String>> registeredUserRepo = FXCollections.observableArrayList();
    private List<Map<String, String>> unverifiedList;

    /**
     * Load all the functions for each component in the admin panel
     *
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeAcceptDeclineAllButton();
        importRegisteredUsers();
        loadUnRegisteredUsersToScrollPane();
        loadRegisteredUsersToTable();
    }

    /**
     * Add all the unregistered users to the scroll pane
     */
    public void loadUnRegisteredUsersToScrollPane() {
        //Declare nodes which will be assigned with HBox nodes where HBox node contains 3 children nodes which are label, declineButton and acceptButton
        signUpVbox.getChildren().clear();
        Node node = null;
        for (Map<String, String> unverifiedStudent : unverifiedList) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../resources/view/UserSignUpHbox.fxml"));

            try {
                node = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            assert node != null;
            for (Node component : ((HBox) node).getChildren()) {
                Node finalNode = node;

                if (component instanceof Label) {
                    ((Label) component).setText(unverifiedStudent.get("email"));
                } else if (component instanceof Button) {
                    if (component.getId().equals("declineButton")) {
                        ((Button) component).setOnAction(e -> {
                            ((VBox) component.getParent().getParent()).getChildren().remove(finalNode);
                            verifyRegistration(unverifiedStudent.get("email"), false);
                        });
                    } else if (component.getId().equals("acceptButton")) {
                        ((Button) component).setOnAction(e -> {
                            ((VBox) component.getParent().getParent()).getChildren().remove(finalNode);
                            verifyRegistration(unverifiedStudent.get("email"), true);
                        });
                    }
                }
            }
            signUpVbox.getChildren().add(node);
        }
    }

    public void initializeAcceptDeclineAllButton() {
        acceptAllButton.setOnAction(e -> {
            System.out.println("clicked accept");
            try {
                Map<String, String> actionMap = new HashMap<>();
                actionMap.put("action", Action.ACCEPT_ALL_USERS);
                ClientExecutable.serverConnector.getObjOut().writeObject(actionMap);

                boolean successPost = (boolean) ClientExecutable.serverConnector.getObjIn().readObject();
                if (successPost) {
                    signUpVbox.getChildren().clear(); // removes all the items in vbox
                    System.out.println("Declined all");

                    // maps all the items in list from unverified to verified
                    registeredUserRepo.stream()
                            .filter(student -> !Boolean.parseBoolean(student.get("isVerified")))
                            .forEach(student -> student.put("isVerified", "true"));
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            loadRegisteredUsersToTable();
        });

        declineAllButton.setOnAction(e -> {
            System.out.println("clicked decline");
            try {
                Map<String, String> actionMap = new HashMap<>();
                actionMap.put("action", Action.DECLINE_ALL_USERS);
                ClientExecutable.serverConnector.getObjOut().writeObject(actionMap);

                boolean successPost = (boolean) ClientExecutable.serverConnector.getObjIn().readObject();
                if (successPost) {
                    signUpVbox.getChildren().clear(); // removes all the items in vbox
                    registeredUserRepo.removeAll(unverifiedList);
                    unverifiedList.clear();
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            loadRegisteredUsersToTable();
        });
    }

    @SuppressWarnings("unchecked")
    public void verifyRegistration(String email, boolean isVerified) {
        try {
            Map<String, String> actionMap = new HashMap<>();
            actionMap.put("action", Action.POST_VERIFIED_USERS);
            actionMap.put("email", email);
            actionMap.put("isVerified", String.valueOf(isVerified));

            ClientExecutable.serverConnector.getObjOut().writeObject(actionMap);

            Map<String, Boolean> response = (Map<String, Boolean>) ClientExecutable.serverConnector.getObjIn().readObject();

            if (isVerified) {
                registeredUserRepo.stream()
                        .filter(e -> e.get("email").equals(email))
                        .forEach(e -> e.put("isVerified", "true"));
            }

            if (response.get("response")) {
                List<Map<String, String>> toRemove = unverifiedList.stream()
                        .filter(e -> e.get("email").equals(email))
                        .collect(Collectors.toList());
                unverifiedList.removeAll(toRemove);
            }

            loadUnRegisteredUsersToScrollPane();
            loadRegisteredUsersToTable();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadRegisteredUsersToTable() {
        studentTable.getItems().clear();
        for (Map<String, String> map : registeredUserRepo) {
            studentTable.getItems().add(map);
        }

        status.setCellValueFactory(new MapValueFactory("isVerified"));
        email.setCellValueFactory(new MapValueFactory("email"));
        fName.setCellValueFactory(new MapValueFactory("firstName"));
        lName.setCellValueFactory(new MapValueFactory("lastName"));
    }

    public void importRegisteredUsers() {
        try {
            Map<String, String> verifiedRequest = new HashMap<>();
            verifiedRequest.put("action", Action.GET_VERIFIED_USERS);

            Map<String, String> unverifiedRequest = new HashMap<>();
            unverifiedRequest.put("action", Action.GET_UNVERIFIED_USERS);

            List<Map<String, String>> verifiedList = getRegisteredUsers(verifiedRequest);
            unverifiedList = getRegisteredUsers(unverifiedRequest);

            List<Map<String, String>> mapRepo = new ArrayList<>();
            mapRepo.addAll(verifiedList);
            mapRepo.addAll(unverifiedList);

            registeredUserRepo = FXCollections.observableArrayList(mapRepo);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void logout(ActionEvent action) {
        //add method here
    }

    @SuppressWarnings("unchecked")
    private List<Map<String, String>> getRegisteredUsers(Map<String, String> request) throws IOException, ClassNotFoundException {
        ClientExecutable.serverConnector.getObjOut().writeObject(request);
        return (List<Map<String, String>>) ClientExecutable.serverConnector.getObjIn().readObject();
    }
}