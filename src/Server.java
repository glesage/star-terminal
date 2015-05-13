import Main.Network.ListenSocket;

import Main.Game;

public class Server {
    public static void main(String[] args) {
        Game game = new Game(41);
        startServer(5555, game);
    }

    private static void startServer(int port, Game game) {
        ListenSocket listen = new ListenSocket(port, game);
    }
}
