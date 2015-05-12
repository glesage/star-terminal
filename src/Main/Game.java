package Main;

import Main.Exceptions.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Created by jeff on 5/11/15.
 */
public class Game {
    public volatile List<String> map;
    public int mapWidth;
    private List<User> users = new ArrayList<User>();

    public Game(int mapWidth) {
        this.mapWidth = mapWidth;
    }

    public void joinGame(User user) throws UserAlreadyInGameException {
        if (users.contains(user)) throw new UserAlreadyInGameException();
        users.add(user);
    }

    public String updateMap(User user, String mvmt) throws UserNotInGameException {
        if (!users.contains(user)) throw new UserNotInGameException();

        switch (mvmt) {
            case "LEFT":
                if (user.pos <= 0) user.pos = 0;
                else user.pos--;
                break;
            case "RIGHT":
                if (user.pos >= this.mapWidth) user.pos = this.mapWidth;
                else user.pos++;
                break;
            case "START":
                user.pos = (int)(this.mapWidth/2);
                break;
            default:
                System.out.println("User sent invalid mvmt: " + mvmt);
        }

        return "test";
    }
}
