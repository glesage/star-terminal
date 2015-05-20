package Main.Network.Network;

import Main.Network.Console.Console;

// Network stuff
import java.net.Socket;
import java.net.InetAddress;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Timer;
import java.util.TimerTask;


public class SendSocket {
    private static final int kREFRESH = 200; // Milisecond

    private int port;
    private String host;

    private Console console;
    private Boolean dead = false;

    private Timer timer;
    public Boolean communicated = false;

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

            // Refresh the screen regardless of movements sent
            timer = new Timer();
            timer.schedule(new Refresh(this), 0, kREFRESH);

            // Get movement from keyboard
            String movement= null;
            while (!dead && !(movement = this.getNextMovement()).equals("EXIT"))
            {
                this.communicate(movement);
            }

            timer.cancel();
            if (client_socket.isConnected()) close();
            this.console.reset();
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

            // receive the game map
            String response = null;
            int mapHeight = 14;
            while (mapHeight > 0) {
                response = input.readLine();
                System.out.println(response);
                mapHeight--;
                if (response.toLowerCase().contains("GAME OVER".toLowerCase())) this.dead = true;
            }
            System.out.println("\f");
            if (this.dead) close();
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
        this.communicated = false;

        int key = this.console.getLiveASCII();

        if ( key == 0x1B ) return "EXIT"; // pressed 'esc'
        if ( key == 97 ) return "LEFT"; // pressed 'a'
        if ( key == 100 ) return "RIGHT"; // presses 'd'

        this.communicated = true;

        // If an unknown or invalid key was pressed, wait for a valid one
        return this.getNextMovement();
    }

    class Refresh extends TimerTask {
        SendSocket parent = null;

        Refresh (SendSocket parent)
        {
            this.parent = parent;
        }

        public void run() {
            if (dead || parent.communicated) return;
            parent.communicate("");
        }
    }
}