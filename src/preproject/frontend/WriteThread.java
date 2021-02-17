package preproject.frontend;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map;

public class WriteThread extends Thread {
    private ObjectOutputStream objOut;
    private Socket socket;

    public WriteThread(Socket socket) {
        this.socket = socket;
        try {
            objOut = new ObjectOutputStream(this.socket.getOutputStream());
//            while (true) {
//                writeObject(objOut);
//            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeObject(ObjectOutputStream objOut) throws IOException {
        while (true) {
//            objOut.writeObject();
        }
    }

    public void sendDataToServer(Map<String, Object> mapRepo) {

    }

    public ObjectOutputStream getObjOut() {
        return objOut;
    }
}
