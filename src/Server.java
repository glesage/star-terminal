import Main.Network.ListenSocket;
import java.util.Timer;
import java.util.TimerTask;
import java.io.*;

public class Server {
    public static void main(String[] args) {
        startServer(5555);
    }

    private static void startServer(int port) {
        ListenSocket listen = new ListenSocket(port);
    }
}
