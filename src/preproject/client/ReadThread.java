package preproject.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ReadThread extends Thread {
    private ObjectInputStream objIn;

    public ReadThread(Socket socket) {
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
