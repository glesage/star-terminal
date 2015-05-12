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
        System.out.println("Running user");

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            OutputStream output = clientSocket.getOutputStream();

            String clientMsg = reader.readLine();

            this.user = new User("Geoff", 'G');
            if (clientMsg.equals("START")) {
                try {
                    game.joinGame(this.user);
                    output.write(("Welcome to Star Terminal").getBytes());
                } catch (UserAlreadyInGameException e) {
                    System.out.println("Server attempted to add user to game more than once...");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            try {
                output.write((game.updateMap(this.user, clientMsg)).getBytes());
            } catch (UserNotInGameException e) {
                output.write(("Server could not add you to game...").getBytes());
            }

            output.close();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}