package preproject.client.controllers;

import java.sql.Timestamp;

public class Message {

    private String senderID;
    private Timestamp timeStamp;
    private String senderName;
    private String message;

    public Message(String senderID, Timestamp timeStamp, String senderName, String message) {
        this.senderID = senderID;
        this.timeStamp = timeStamp;
        this.senderName = senderName;
        this.message = message;
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

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Message{" +
                "senderID='" + senderID + '\'' +
                ", timeStamp='" + timeStamp + '\'' +
                ", senderName='" + senderName + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
