package preproject.frontend;

import java.io.*;
import java.net.Socket;

public class ServerConnector extends Thread {
    private Socket socket;
    private ReadThread readThread;
    private WriteThread writeThread;

    public ServerConnector() {
    }

    @Override
    public void run() {
        try {

            this.socket = new Socket("localhost", 2000);;

            writeThread = new WriteThread(socket);
            readThread = new ReadThread(socket);

            writeThread.start();
            readThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Socket getSocket() {
        return socket;
    }

    public ObjectInputStream getObjIn() {
        return readThread.getObjIn();
    }

    public ObjectOutputStream getObjOut() {
        return writeThread.getObjOut();
    }

}
