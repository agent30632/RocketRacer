import java.util.*;
import javax.swing.*;

public class Main {
    final static String GAME_NAME = "Rocket Racer";

    MainMenuPanel menuPanel = new MainMenuPanel();
    HashMap<String, Time> personalBests = new HashMap<>();
    
    public void loadPersonalData() {

    }

    public void savePersonalData() {

    }

    public void saveDataToFile() {

    }

    public Time getPersonalBest(String trackID) {
        return personalBests.getOrDefault(trackID, new Time(0, 0, 0));
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame(GAME_NAME);

        // TODO: level loading
        Game game = new Game("tracks/testTrack.track");

        frame.setUndecorated(true);
        frame.add(game);
        frame.addKeyListener(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setResizable(false);
        frame.setVisible(true);
    }
}
