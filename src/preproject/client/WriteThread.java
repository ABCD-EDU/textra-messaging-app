package preproject.client;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class WriteThread extends Thread {
    private ObjectOutputStream objOut;

    public WriteThread(Socket socket) {
        try {
            objOut = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ObjectOutputStream getObjOut() {
        return objOut;
    }
}
