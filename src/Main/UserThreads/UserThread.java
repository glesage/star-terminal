package Main.UserThreads;

import Main.Exceptions.UserAlreadyInGameException;
import Main.Exceptions.UserNotInGameException;
import Main.Game;
import Main.User;

import java.io.*;
import java.net.Socket;

public class UserThread implements Runnable{

    protected Socket clientSocket = null;
    private Game game;
    private User user;

    public UserThread(Socket clientSocket, Game game) {
        this.clientSocket = clientSocket;
        this.game = game;
    }

    public void run() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            OutputStream output = clientSocket.getOutputStream();

            this.user = new User("Geoff", 'G');

            String clientMsg = null;
            while ((clientMsg = reader.readLine()) != null) {

                System.out.println(user.name + " sent " + clientMsg);

                // If the client is asking to join the game, welcome him in
                if (clientMsg.equals("START")) {
                    try {
                        game.joinGame(this.user);
                        String welcome = " Welcome to Star Terminal ";
                        while (welcome.length() < game.mapWidth) welcome = "#" + welcome + "#";
                        welcome = welcome + "\n";
                        output.write(welcome.getBytes());
                    } catch (UserAlreadyInGameException e) {
                        System.out.println("Server attempted to add user to game more than once...");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        output.write((game.updateMap(this.user, clientMsg)).getBytes());
                    } catch (UserNotInGameException e) {
                        output.write(("Server could not add you to game...").getBytes());
                    }
                }
            }

            output.close();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}