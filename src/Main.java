import java.awt.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

public class Main {
    final static String GAME_NAME = "Rocket Racer";

    static ImageIcon gameIcon = new ImageIcon("assets/img/game_icon.png");
    static Image gameIconImage = gameIcon.getImage();

    static HashMap<String, Time> personalBests = new HashMap<>();
    static MainMenuPanel menuPanel;
    static OptionsPanel optionsPanel;
    static PlayPanel playPanel;
    static Game gamePanel;
    static HelpPanel helpPanel;

    static JFrame frame;
    
    public static void loadPersonalData() {
        try {
            BufferedReader bufIn = new BufferedReader(new FileReader("save/data.save"));
            String line = bufIn.readLine();
            
            while (line != null) {
                StringTokenizer strTokenizer = new StringTokenizer(line);
                String trackID = strTokenizer.nextToken();
                Time time = new Time(strTokenizer.nextToken());

                personalBests.put(trackID, time);

                line = bufIn.readLine();
            }

            bufIn.close();
        } catch (FileNotFoundException e) {
            File newSave = new File("save/data.save");
            try {
                newSave.createNewFile();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addPersonalData(String trackID, Time time) {
        personalBests.put(trackID, time);
    }

    public static void saveDataToFile() {
        try {
            PrintWriter saveWrite = new PrintWriter("save/data.save");
            for (String key : personalBests.keySet()) {
                saveWrite.println(key + " " + personalBests.get(key));
            }

            saveWrite.close();
        } catch (IOException e) {
            //TODO: handle exception
        }
    }

    public static Time getPersonalBest(String trackID) {
        return personalBests.get(trackID);
    }

    public static void showMainMenu() {
        frame.dispose();
        frame = new JFrame(GAME_NAME);
        frame.setUndecorated(true);
        frame.setIconImage(gameIconImage);

        frame.add(menuPanel);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setFocusTraversalPolicy(null);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    public static void showOptions() {
        // TODO: show options
    }

    public static void showGame(String trackFilePath) {
        // TODO: new game time bois
        frame.dispose();
        frame = new JFrame(GAME_NAME);
        frame.setUndecorated(true);
        frame.setIconImage(gameIconImage);

        gamePanel = new Game("tracks/testTrack.track");
        frame.add(gamePanel);
        frame.addKeyListener(gamePanel);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setFocusTraversalPolicy(null);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    public static void showTutorial() {
        frame.dispose();
        frame = new JFrame(GAME_NAME);
        frame.setUndecorated(true);
        frame.setIconImage(gameIconImage);
    }

    public static void main(String[] args) {
        frame = new JFrame(GAME_NAME);

        loadPersonalData();

        menuPanel = new MainMenuPanel();

        frame.setUndecorated(true);
        frame.setIconImage(gameIconImage);
        frame.add(menuPanel);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setFocusTraversalPolicy(null);
        frame.setResizable(false);
        frame.setVisible(true);
    }
}
