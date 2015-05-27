import Main.Network.ListenSocket;

import Main.Game;

public class Server {
    static volatile Game game;

    public static void main(String[] args) {
        game = new Game(50);
        startServer(5555, game);
    }

    private static void startServer(int port, Game game) {
        new ListenSocket(port, game);
    }
}
