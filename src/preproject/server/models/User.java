package preproject.server.models;

import java.util.*;

/**
 * This class will be used to create an object of user which holds all of the information of the account and also the
 * history of their messages.
 */
public class User {
    private int userId;
    private Optional<String> email;
    private String firstName;
    private String lastName;
    private boolean isLoggedIn;

    public User(int id, String email, String fN, String lN) {
        this.userId = id;

        if (email.equals(""))
            this.email = Optional.empty();
        else
            this.email = Optional.of(email);

        this.firstName = fN;
        this.lastName = lN;
        this.isLoggedIn = true;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getEmail() {
        if (this.email.isPresent()) {
            return email.get();
        } else {
            return " unknown user ";
        }
    }

    public void setEmail(String email) {
        this.email = Optional.ofNullable(email);
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

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }
}
