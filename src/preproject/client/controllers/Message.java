package preproject.client.controllers;

import java.sql.Timestamp;

public class Message {

    private String senderID;
    private Timestamp timeStamp;
    private String firstName;
    private String lastName;
    private String message;
    private String color;

    public Message(String senderID, Timestamp timeStamp, String firstName, String lastName, String message, String color) {
        this.senderID = senderID;
        this.timeStamp = timeStamp;
        this.firstName = firstName;
        this.lastName = lastName;
        this.message = message;
        this.color = color;
    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public Timestamp getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Timestamp timeStamp) {
        this.timeStamp = timeStamp;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "Message{" +
                "senderID='" + senderID + '\'' +
                ", timeStamp=" + timeStamp +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", message='" + message + '\'' +
                ", color='" + color + '\'' +
                '}';
    }

}
