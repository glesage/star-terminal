package Main;

import Main.Exceptions.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by jeff on 5/11/15.
 */
public class Game {
    private static final int mapHeight = 14;
    private static final char OBSTACLE = 'X';
    private static final char WALL = '*';

    Random ran = new Random();

    private List<User> users = new ArrayList<User>();
    public volatile List<String> map = new ArrayList<String>();
    public int mapWidth;

    public Game(int mapWidth) {
        this.mapWidth = mapWidth;
    }

    public String joinGame(User user) throws UserAlreadyInGameException {
        if (users.contains(user)) throw new UserAlreadyInGameException();
        user.pos = (int)(this.mapWidth/2);
        users.add(user);

        List<String> messages = new ArrayList<String>();
        messages.add(" # WELCOME TO STAR TERMINAL # ");
        return this.mapWithMessages(messages);
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
                List<String> messages = new ArrayList<String>();
                messages.add(" ##################### ");
                messages.add(" # INVALID DIRECTION # ");
                messages.add(" ##################### ");
                return this.mapWithMessages(messages);
        }

        return this.rebuildMap();
    }

    private String rebuildMap() {

        // Make sure the map has the right number of lines
        while (this.map.size() < mapHeight) this.map.add(this.createBlankLine());

        // Shift the map up one line
        this.map.remove(0);
        this.map.add(this.createNextLine());

        // Create a new line with updates user positions
        String updatedFirstLine = this.lineWithUpdatedPositions();

        // Check for collisions
        String death = this.checkForDeath(this.map.get(0), updatedFirstLine);
        if (death != null) return death;

        // If no death has been found, update the first line
        this.map.set(0, updatedFirstLine);

        return Game.getMapAsString(this.map);
    }

    private String checkForDeath(String originalLine, String newLine) {
        for (int i = 0; i < this.mapWidth; i++){
            char original = originalLine.charAt(i);
            char updated = newLine.charAt(i);

            // Skip if there are no obstacles at this position
            if (original != OBSTACLE) continue;

            // Skip if there are no users at this new position
            if (updated == OBSTACLE || updated == ' ') continue;

            // If you got so far then there is a collision
            List<String> messages = new ArrayList<String>();
            messages.add(" ################# ");
            messages.add(" ### GAME OVER ### ");

            for (User user : this.users) {
                if (user.symbol != updated) continue;
                String lostMessage = " " + user.name + " lost! ";
                while (lostMessage.length() < (messages.get(0).length()-2)) {
                    lostMessage = "#" + lostMessage + "#";
                }
                messages.add(" " + lostMessage + " ");
            }
            messages.add(" ################# ");

            return this.mapWithMessages(messages);
        }
        return null;
    }

    private String createNextLine() {
        String line = "*";
        while (line.length() < this.mapWidth-1)
        {
            if (ran.nextDouble() > 0.7) line += OBSTACLE;
            else line += " ";
        }

        return line + WALL;
    }

    private String createBlankLine() {
        String line = "*";
        while (line.length() < this.mapWidth-1)
        {
            line += " ";
        }

        return line + "*";
    }

    // Create a duplicate of the first line with updates user positions
    private String lineWithUpdatedPositions() {
        StringBuilder updatedLine = new StringBuilder(this.map.get(0));
        for (User user : this.users) {
            updatedLine.setCharAt(user.pos, user.symbol);
        }
        return updatedLine.toString();
    }

    // Rebuild the map with some custom messages
    public String mapWithMessages(List<String> messages) {

        // Make sure the map has the right number of lines
        while (this.map.size() < mapHeight) this.map.add(this.createBlankLine());

        // Build a new version of the map in memory, with the message lines
        List<String> newMap = new ArrayList<String>(mapHeight);
        int sizeMinusMessages = (mapHeight/2)-messages.size();
        while (newMap.size() < sizeMinusMessages) {
            newMap.add(this.map.get(newMap.size()));
        }
        for (String message : messages) {
            StringBuilder line = new StringBuilder(map.get(newMap.size()));

            int begin = (line.length()/2)-(message.length()/2);
            for (int i = 0; i < message.length(); i++) {
                line.setCharAt(begin+i, message.charAt(i));
            }
            newMap.add(line.toString());
        }
        while (newMap.size() < mapHeight) {
            newMap.add(this.map.get(newMap.size()));
        }

        return Game.getMapAsString(newMap);
    }

    // Build a string out of the map List
    private static String getMapAsString(List<String> aMap) {
        String outputMap = "";
        for (String line : aMap) {
            outputMap += (line + "\n");
        }
        return outputMap;
    }
}
