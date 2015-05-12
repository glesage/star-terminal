package Main;

import Main.Exceptions.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by jeff on 5/11/15.
 */
public class Game {
    private static final int mapHeight = 10;
    Random ran = new Random();

    private List<User> users = new ArrayList<User>();
    public volatile List<String> map = new ArrayList<String>();
    public int mapWidth;

    public Game(int mapWidth) {
        this.mapWidth = mapWidth;
    }

    public void joinGame(User user) throws UserAlreadyInGameException {
        if (users.contains(user)) throw new UserAlreadyInGameException();
        user.pos = (int)(this.mapWidth/2);
        users.add(user);
    }

    public String updateMap(User user, String mvmt) throws UserNotInGameException {
        if (!users.contains(user)) throw new UserNotInGameException();

        switch (mvmt) {
            case "LEFT":
                if (user.pos <= 1) user.pos = 1;
                else user.pos--;
                break;
            case "RIGHT":
                if (user.pos >= this.mapWidth-1) user.pos = this.mapWidth-1;
                else user.pos++;
                break;
            default:
                System.out.println("User sent invalid mvmt: " + mvmt);
        }

        return this.rebuildMap();
    }

    private String rebuildMap() {

        // Make sure the map has the right number of lines
        while (this.map.size() < mapHeight) this.map.add(this.createNextLine());

        // Shift the map up one line
        this.map.remove(0);
        this.map.add(this.createNextLine());

        // Update user positions for the first line
        String originalFirstLine = this.map.get(0);
        String updatedFirstLine = this.updateUserPositions(originalFirstLine);

        // Check for collisions
        String death = this.checkForDeath(originalFirstLine, updatedFirstLine);
        if (death != null) return death;

        // If no death has been found, update the first line
        this.map.set(0, updatedFirstLine);

        // Build a string out of the map List
        String outputMap = "";
        for (String line : this.map) {
            outputMap += (line + "\n");
        }

        return outputMap;
    }

    private String checkForDeath(String originalLine, String newLine) {
        for (int i = 0; i < this.mapWidth; i++){
            char original = originalLine.charAt(i);
            char updated = newLine.charAt(i);
            if (original == 'O' && updated != 'O' && updated != ' ')
            {
                String gameOver = "GAME OVER";
                while (gameOver.length() < this.mapWidth) {
                    gameOver = "#" + gameOver + "#";
                }
                gameOver = "\n" + gameOver + "\n";

                String whoLost = "";
                for (User user : this.users) {
                    if (user.symbol != updated) continue;
                    whoLost = user.name + "has lost!";
                }
                while (whoLost.length() < this.mapWidth) {
                    whoLost = "#" + whoLost + "#";
                }
                whoLost = "\n" + whoLost + "\n";

                return gameOver + whoLost;
            }
        }
        return null;
    }

    private String createNextLine() {
        String nextLine = "*";
        while (nextLine.length() < this.mapWidth-1)
        {
            if (ran.nextDouble() > 0.7) nextLine += "O";
            else nextLine += " ";
        }

        return nextLine + "*";
    }

    private String updateUserPositions(String line) {
        StringBuilder updatedLine = new StringBuilder(line);
        for (User user : this.users) {
            updatedLine.setCharAt(user.pos, user.symbol);
        }
        return updatedLine.toString();
    }
}
