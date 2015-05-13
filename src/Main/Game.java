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
    private static final int MSG_PADDING = 5;
    private static final char OBSTACLE = 'X';
    private static final char WALL = '*';
    private static final char MSG_CHAR = '#';

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
        messages.add(this.buildMessage(" WELCOME TO STAR TERMINAL "));
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
                messages.add(" INVALID DIRECTION ");
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

        User theDead = null;
        for (User user : this.users) {
            if (originalLine.charAt(user.pos) != OBSTACLE) continue;
            theDead = user;
        }

        if (theDead == null) return null;

        // If you got so far then there is a collision
        // so build & send out the game over message
        List<String> messages = new ArrayList<String>();
        messages.add(this.buildMessage(" GAME OVER! "));
        messages.add(this.buildMessage(" " + theDead.name + " is dead! "));
        return this.mapWithMessages(messages);
    }

    private String createNextLine() {
        String line = "*";
        while (line.length() < this.mapWidth-1)
        {
            if (ran.nextDouble() > 0.9) line += OBSTACLE;
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
        String nextLine = null;
        int sizeMinusMessages = (mapHeight/2)-(messages.size()/2)-1;

        // Add the first set of lines back into the map
        while (newMap.size() < sizeMinusMessages) newMap.add(this.map.get(newMap.size()));

        // Add separating line of characters to make messages clearer
        nextLine = map.get(newMap.size());
        newMap.add(this.buildMsgLine(nextLine, this.buildMessage("")));
        //

        // Embed the actual messages into the map
        for (String message : messages) {
            nextLine = map.get(newMap.size());
            newMap.add(this.buildMsgLine(nextLine, message));
        }

        // Add separating line of characters to make messages clearer
        nextLine = map.get(newMap.size());
        newMap.add(this.buildMsgLine(nextLine, this.buildMessage("")));
        //

        // Add the last set of lines back into the map
        while (newMap.size() < mapHeight) newMap.add(this.map.get(newMap.size()));

        // return the map as a string
        return Game.getMapAsString(newMap);
    }

    // Build a message line using an existing map line using
    // the original map line's data to blend in nicely
    private String buildMsgLine(String original, String message) {
        StringBuilder line = new StringBuilder(original);

        int begin = (line.length()/2)-(message.length()/2);
        for (int i = 0; i < message.length(); i++) {
            line.setCharAt(begin+i, message.charAt(i));
        }
        return line.toString();
    }

    // Build a message string using the appropriate size, padding, and decoration
    private String buildMessage(String message) {
        while (message.length() < this.mapWidth-MSG_PADDING) {
            message = MSG_CHAR + message + MSG_CHAR;
        }
        message = " " + message + " ";
        return message;
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
