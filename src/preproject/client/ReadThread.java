package preproject.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ReadThread extends Thread {
    private ObjectInputStream objIn;
    private Socket socket;

    public ReadThread(Socket socket) {
        this.socket = socket;

        try {
            objIn = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ObjectInputStream getObjIn() {
        return objIn;
    }
}
