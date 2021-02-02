package preproject.backend.models;

import java.util.ArrayList;
import java.util.List;

// TODO: fit to database
// placeholder data for now
public class User {
    private String userId;
    private String email;
    private String firstName;
    private String lastName;
    private String userName;
    private List<Message> messageList;

    public User(String id, String email, String fN, String lN, String uN) {
        this.userId = id;
        this.email = email;
        this.firstName = fN;
        this.lastName = lN;
        this.userName = uN;
        this.messageList = new ArrayList<>();
    }

    public User(String id, String email, String fN, String lN, String uN, List<Message> mL) {
        this.userId = id;
        this.email = email;
        this.firstName = fN;
        this.lastName = lN;
        this.userName = uN;
        this.messageList = mL;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<Message> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<Message> messageList) {
        this.messageList = messageList;
    }
}
