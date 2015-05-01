import Main.Network.ListenSocket;

public class Server {
    private static final int port = 5555;

    public static void main(String[] args){
        ListenSocket listen = new ListenSocket(port);
    }
}
