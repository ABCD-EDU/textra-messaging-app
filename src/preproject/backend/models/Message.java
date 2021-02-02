package preproject.backend.models;

import java.sql.Timestamp;

// TODO: fit to database
// placeholder data for now
public class Message {
    private String status;
    private String message;
    private java.sql.Timestamp timeSent;

    public Message(String message, java.sql.Timestamp timeSent) {
        this.message = message;
        this.timeSent = timeSent;
    }

    public Message(String message, String status, java.sql.Timestamp timeSent) {
        this.message = message;
        this.timeSent = timeSent;
        this.status = status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
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
