package preproject.client;

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
            this.socket = new Socket("localhost", 2000);

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

    protected void close() {
        try {
            if (socket != null) {
                socket.close();
            }

            if (writeThread.getObjOut() != null) {
                writeThread.getObjOut().close();
            }

            if (readThread.getObjIn() != null) {
                readThread.getObjIn().close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
