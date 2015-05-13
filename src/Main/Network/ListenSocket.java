package Main.Network;

import Main.Game;
import Main.UserThreads.UserThread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ListenSocket {
    private int port;
    private static final int timeout = 20000; //20 seconds, while developing

    public ListenSocket(int port, Game game){
        this.port = port;
        String line;

        try {
            System.out.println("Starting server listening at localhost:" + String.valueOf(port));
            ServerSocket serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(5000);
            Socket clientSocket = serverSocket.accept();

            new Thread( new UserThread( clientSocket, game) ).start();
        }
        catch (java.net.SocketTimeoutException e){
            String timeoutMsg;
            if (timeout > 3600000) timeoutMsg = (timeout/3600000) + " hrs";
            else if (timeout > 60000) timeoutMsg = (timeout/60000) + " min";
            else if (timeout > 1000) timeoutMsg = (timeout/1000) + " sec";
            else timeoutMsg = timeout + " ms";

            System.out.println("\n\nServer shut down (no one wanted to play after " + timeoutMsg + " /:)");
            System.exit(0);
        }
        catch (IOException e){
            System.out.println("Couldn't start server");
            e.printStackTrace();
            System.exit(1);
        }
    }
}
