package Main.Network;

import Main.Game;
import Main.ServerCommandWatcher;
import Main.UserThreads.UserThread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ListenSocket {
    private static final int timeout = 2000000; //2000 seconds, while developing

    public ListenSocket(int port, Game game){
        Socket clientSocket;

        //watch for input to stop server
        new Thread(new ServerCommandWatcher()).start();

        try {
            ServerSocket serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(timeout);

            //keeps going until interrupted by socket timeout exception or server shutting down
            while(true){
                clientSocket = serverSocket.accept();

                new Thread( new UserThread( clientSocket, game) ).start();
            }


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
        /*catch (InterruptedException e){
            System.out.println("Interrupted by: " + e);
        }*/
    }
}
