package preproject.backend.models;

import java.util.*;

/**
 * This class will be used to create an object of user which holds all of the information of the account and also the
 * history of their messages.
 *
 * Each message will be stored in a {@link List} of the {@link Message} class. Then, this information will be used to
 * store all of the unique group chat that the user is in. Each {@link List} of {@link Message} will be identified by
 * a unique ID.
 */
public class User {
    private int userId;
    private String email;
    private String firstName;
    private String lastName;
    private Map<String, List<Message>> messageList; // where String = id, Message = messages for that group chat's id
    private Set<GroupChat> groups;

    public User(int id, String email, String fN, String lN) {
        this.userId = id;
        this.email = email;
        this.firstName = fN;
        this.lastName = lN;
        this.messageList = new HashMap<>();
        this.groups = new HashSet<>();
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
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

    public Map<String, List<Message>> getMessageList() {
        return messageList;
    }

    public void setMessageList(Map<String, List<Message>> messageList) {
        this.messageList = messageList;
    }
}
