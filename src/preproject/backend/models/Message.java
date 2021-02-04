package preproject.backend.models;

import java.sql.Timestamp;

/**
 * The goal for this class is to create an object of Message where it contains the information of the messages sent,
 * if the message is sent or received/seen or not seen, timestamp, and the id for the message for validation.
 */
public class Message {
    private String convoId; // validation key for which group chat it was sent to
    private String message;
    private java.sql.Timestamp timeSent;

    public Message(String convoId, String message, java.sql.Timestamp timeSent) {
        this.convoId = convoId;
        this.message = message;
        this.timeSent = timeSent;
    }

    public String getConvoId() {
        return convoId;
    }

    public void setConvoId(String convoId) {
        this.convoId = convoId;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setTimeSent(Timestamp timeSent) {
        this.timeSent = timeSent;
    }

    public Timestamp getTimeSent() {
        return timeSent;
    }
}
