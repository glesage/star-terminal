package Main.Network;

import java.net.Socket;
import java.net.InetAddress;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

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
            this.communicate("START");
            this.communicate("LEFT");;
            this.communicate("RIGHT");;
            this.communicate("DOWN");
            close();
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

            // receive the game map
            String response = input.readLine();
            System.out.println(response);
            if (response.toLowerCase().contains("GAME OVER".toLowerCase())) close();
        }
        catch(IOException e) {
            System.out.println("Error communicating with server.");
            e.printStackTrace();
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