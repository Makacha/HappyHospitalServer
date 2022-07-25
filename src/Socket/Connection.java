package Socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Connection {
    private ServerSocket serverSocket;
    private Socket socket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;

    public Connection() {
        try {
            serverSocket = new ServerSocket(8888);
            socket = serverSocket.accept();
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.getStackTrace();
        }
        System.out.println("Connect success!");
    }

    public void sendData(String data) {
        try {
            dataOutputStream.writeUTF(data);
            dataOutputStream.flush();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.getStackTrace();
        }
    }

    public String getRequest() {
        String receiveData = null;
        try {
            receiveData = dataInputStream.readUTF();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.getStackTrace();
            receiveData = "finish";
        }
        return receiveData;
    }

    public void disconect() {
        try {
            dataInputStream.close();
            dataOutputStream.close();
            socket.close();
            serverSocket.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.getStackTrace();
        }
    }

    public static Command getCommand(String request) {
        String command = request.split(" ")[0];
        if (command.equals("press")) {
            return Command.KEY_PRESSED;
        } else if (command.equals("release")) {
            return Command.KEY_RELEASED;
        } else if (command.equals("draw")) {
            return Command.DRAW;
        } else if (command.equals("add")) {
            return Command.ADD;
        } else if (command.equals("update")){
            return Command.UPDATE;
        } else if (command.equals("finish")) {
            return Command.FINISH;
        } else if (command.equals("load")) {
            return Command.LOAD_MAP;
        } else if (command.equals("save")) {
            return Command.SAVE_MAP;
        } else {
            return Command.END;
        }
    }

    public static String getData(String request) {
        int index = request.indexOf(" ");
        if (index == -1 || index == request.length() - 1)
            return null;
        return request.substring(index + 1);
    }
}
