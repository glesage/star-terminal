package Main.Network;

import java.net.Socket;
import java.net.InetAddress;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class SendSocket {
    private int port;
    private String host;

    String line;
    InetAddress loopback;
    Socket client_socket;
    PrintWriter output;
    BufferedReader input;

    public SendSocket(String host, int port) {
        this.port = port;
        this.host = host;

        this.connect();
    }

    private void connect() {
        try {
            if (host.isEmpty())
                loopback = InetAddress.getLoopbackAddress();
            else
                loopback = InetAddress.getByName(host);

            // try to connect to the server
            client_socket = new Socket(loopback, port);

            // grab the output and input streams
            output = new PrintWriter(client_socket.getOutputStream(), true);
            input = new BufferedReader(new InputStreamReader(client_socket.getInputStream()));

            // TODO - get move from keyboard
            List<String> movements = new ArrayList<String>();
            movements.add("START");
            movements.add("LEFT");
            movements.add("LEFT");
            movements.add("LEFT");
            movements.add("LEFT");
            movements.add("LEFT");
            movements.add("RIGHT");
            movements.add("RIGHT");
            movements.add("LEFT");
            movements.add("LEFT");
            movements.add("RIGHT");
            movements.add("LEFT");
            movements.add("RIGHT");
            movements.add("RIGHT");
            movements.add("RIGHT");
            movements.add("RIGHT");
            movements.add("RIGHT");
            movements.add("RIGHT");
            movements.add("DOWN");

            while (!movements.isEmpty()) this.communicate(movements.remove(0));
            if (client_socket.isConnected()) close();
        }
        catch(IOException e) {
            System.out.println("Couldn't start client.");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void communicate(String move) {
        try {
            // send a message
            output.println(move);

            Boolean dead = false;
            // receive the game map
            String response = null;
            int mapHeight = 14;
            while (mapHeight > 0) {
                response = input.readLine();
                System.out.println(response);
                mapHeight--;
                if (response.toLowerCase().contains("GAME OVER".toLowerCase())) dead = true;
            }
            System.out.println("\f");
            if (dead) close();
        }
        catch(IOException e) {
            System.out.println("Server connection closed.");
            System.exit(1);
        }
    }

    public void close() {
        try {
            client_socket.close();
        }
        catch( Exception e ) {
            System.out.println("Had an error closing the socket.");
        }
    }
}