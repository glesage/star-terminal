import Main.Network.ListenSocket;

import Main.Game;

public class Server {
    private static volatile Game game;
    private static int port = 5555;


    public static void main(String[] args) {
        game = new Game(50);

        if(args.length > 0){
            try{
                port = Integer.parseInt(args[0]);
            }catch (NumberFormatException e){
                System.out.println("Bad port number. Exiting program.");
                System.exit(1);
            }
        }

        startServer();
    }

    private static void startServer() {
        System.out.println("Starting on port: " + port);
        new ListenSocket(port, game);
    }
}
