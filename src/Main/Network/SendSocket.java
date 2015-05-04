package Main.Network;

import java.net.Socket;
import java.net.InetAddress;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class SendSocket {
    private int port;

    String line;
    InetAddress loopback;
    Socket client_socket;
    PrintWriter output;
    BufferedReader input;

    public SendSocket( int port ) throws IOException {
        this.port = port;

        loopback= InetAddress.getLoopbackAddress();

        // try to connect to the server
        client_socket= new Socket( loopback, port );

        // grab the output and input streams
        output= new PrintWriter( client_socket.getOutputStream(), true );
        input= new BufferedReader( new InputStreamReader( client_socket.getInputStream() ) );
    }

    public void communicate(String move) throws IOException {
        // send a message
        output.println(move);

        // receive the game map
        String response= input.readLine();
        if ( response.isEmpty() )
            System.out.println( "(server did not reply with a message)" );
        else if ( response.equalsIgnoreCase("Death") ) {
            System.out.println("GAME OVER");
            close();
        } else {
            System.out.println( response );
        }
    }

    public void close() {
        try {
            client_socket.close();
        }
        catch( Exception e ) {
            // ignore
        }
    }
}