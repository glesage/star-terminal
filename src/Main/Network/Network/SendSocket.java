package Main.Network.Network;

import Main.Network.Console.Console;

// Network stuff
import java.net.Socket;
import java.net.InetAddress;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class SendSocket {
    private int port;
    private String host;

    private Console console;

    String line;
    InetAddress loopback;
    Socket client_socket;
    PrintWriter output;
    BufferedReader input;

    public SendSocket(String host, int port) {
        this.port = port;
        this.host = host;
        this.console = new Console();

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

            // Join the game
            this.communicate("START");

            // Get movement from keyboard
            String movement= null;
            while (!(movement = this.getNextMovement()).equals("EXIT"))
            {
                this.communicate(movement);
            }

            if (client_socket.isConnected()) close();
            System.exit(1);
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

    /**
     * Movement input manager
     */
    private String getNextMovement() {
        int key = this.console.getLiveASCII();

        if ( key == 0x1B ) return "EXIT"; // pressed 'esc'
        if ( key == 97 ) return "LEFT"; // pressed 'a'
        if ( key == 100 ) return "RIGHT"; // presses 'd'

        // If an unkown or invalid key was pressed, wait for a valid one
        return this.getNextMovement();
    }
}