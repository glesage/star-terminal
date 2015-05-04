import Main.Network.ListenSocket;

public class Server {
    public static void main(String[] args) {
        startServer(5555);
    }

    private static void startServer(int port) {
        System.out.print("Started server on port: " + String.valueOf(port));
        ListenSocket listen = new ListenSocket(port);
    }
}
