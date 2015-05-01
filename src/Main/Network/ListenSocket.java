package Main.Network;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ListenSocket {
    private int port;

    public ListenSocket(int port){
        this.port = port;
        String line;

        try {
            ServerSocket serverSocket = new ServerSocket(port);
            Socket clientSocket = serverSocket.accept();BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            while((line = reader.readLine()) != null){
                System.out.println("Wrote line: " + line);
            }
        }catch (IOException e){
            System.out.println("Couldn't start server");
            e.printStackTrace();
            System.exit(1);
        }
    }
}
