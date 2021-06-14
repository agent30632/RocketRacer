import java.awt.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;

public class Main {
    final static String GAME_NAME = "Rocket Racer";

    static ImageIcon gameIcon = new ImageIcon("assets/img/game_icon.png");
    static Image gameIconImage = gameIcon.getImage();

    static HashMap<String, Time> personalBests = new HashMap<>();

    // Menus
    static MainMenuPanel menuPanel;
    static PlayPanel playPanel;
    static Game gamePanel;
    static TutorialPanel tutorialPanel;
    static AboutPanel aboutPanel;

    // Main frame
    static JFrame frame;

    // Standardized UI elements
    // Fonts
    static Font uiTextBig = new Font(Font.SANS_SERIF, Font.BOLD, 64);
    static Font uiTextBigItalics = new Font(Font.SANS_SERIF, Font.BOLD|Font.ITALIC, 64);
    static Font uiTextMediumHighlight = new Font(Font.SANS_SERIF, Font.ITALIC|Font.BOLD, 48);
    static Font uiTextMedium = new Font(Font.SANS_SERIF, Font.BOLD, 48);
    static Font uiTextSmall = new Font(Font.SANS_SERIF, Font.PLAIN, 40);
    static Font uiTextSmallItalics = new Font(Font.SANS_SERIF, Font.ITALIC, 40);
    static Font uiTextSmallBold = new Font(Font.SANS_SERIF, Font.BOLD, 40);
    static Font uiTextTiny = new Font(Font.SANS_SERIF, Font.PLAIN, 24);
    static Font uiTextTinyItalics = new Font(Font.SANS_SERIF, Font.ITALIC, 24);

    // Button border
    static Border buttonBorder = BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.WHITE, 5), BorderFactory.createEmptyBorder(2, 15, 2, 15));
    
    /**
     * Loads the user's personal best times from the pb.save file in the save folder, storing it into a HashMap called "personalBests"
     */
    public static void loadPersonalData() {
        try {
            BufferedReader bufIn = new BufferedReader(new FileReader("save/pb.save"));
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
            File newSave = new File("save/pb.save");
            try {
                newSave.createNewFile();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds a personal best time to the personal bests HashMap
     * @param trackID The ID of the track to add a personal best for
     * @param time the time achieved
     */
    public static void addPersonalData(String trackID, Time time) {
        personalBests.put(trackID, time);
    }

    /**
     * Saves the personalBests HashMap to pb.save
     */
    public static void saveDataToFile() {
        try {
            PrintWriter saveWrite = new PrintWriter("save/pb.save");
            for (String key : personalBests.keySet()) {
                saveWrite.println(key + " " + personalBests.get(key));
            }

            saveWrite.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the specified personal best time for the given track
     * @param trackID ID of the track
     * @return Time object representing the player's personal best on this track
     */
    public static Time getPersonalBest(String trackID) {
        return personalBests.get(trackID);
    }

    /**
     * Shows the main menu
     */
    public static void showMainMenu() {
        frame.remove(playPanel);
        frame.remove(tutorialPanel);
        frame.remove(aboutPanel);

        frame.add(menuPanel);
        frame.revalidate();
        frame.pack();
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setFocusTraversalPolicy(null);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    /**
     * Shows a new instance of the game using the given track file
     * @param trackFilepath path of the track file
     */
    public static void showGame(String trackFilepath) {
        frame.dispose();
        frame = new JFrame(GAME_NAME);
        frame.setUndecorated(true);
        frame.setIconImage(gameIconImage);

        gamePanel = new Game(trackFilepath);
        frame.add(gamePanel);
        frame.addKeyListener(gamePanel);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setFocusTraversalPolicy(null);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    /**
     * Shows the "play" menu (i.e. the level selection screen)
     */
    public static void showPlay() {
        frame.remove(menuPanel);
        frame.remove(tutorialPanel);
        frame.remove(aboutPanel);

        // I had to reconstruct it cause there was a bug where medals wouldn't update after finishing a level
        // This is a quick and dirty fix for that
        playPanel = new PlayPanel();

        frame.add(playPanel);
        frame.revalidate();
        frame.pack();
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setFocusTraversalPolicy(null);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    /**
     * Shows the tutorial menu
     */
    public static void showTutorial() {
        frame.remove(playPanel);
        frame.remove(menuPanel);
        frame.remove(aboutPanel);

        frame.add(tutorialPanel);
        frame.revalidate();
        frame.pack();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setFocusTraversalPolicy(null);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    /**
     * Shows the "about" menu
     */
    public static void showAbout() {
        frame.remove(playPanel);
        frame.remove(menuPanel);
        frame.remove(tutorialPanel);

        frame.add(aboutPanel);
        frame.revalidate();
        frame.pack();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setFocusTraversalPolicy(null);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    /**
     * Returns the player to the level selection screen when they have decided to exit the level
     */
    public static void exitFromGame() {
        frame.dispose();
        frame = new JFrame(GAME_NAME);
        frame.setUndecorated(true);
        frame.setIconImage(gameIconImage);

        showPlay();
    }

    public static boolean getOpenGLSetting() {
        try {
            Scanner in = new Scanner(new FileReader("save/settings.save"));
            String line = in.nextLine();
            StringTokenizer lineST = new StringTokenizer(line, " \t\n\r\f=");
            String key = lineST.nextToken();
            String value = lineST.nextToken();

            if (key.compareTo("opengl") == 0) {
                if (value.compareTo("true") == 0) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } catch (FileNotFoundException e) {
            return false;
        }
    }
    
    public static void main(String[] args) {
        // I had huge problems with lag before
        // Forcing OpenGL fixed most of that
        // Maybe if you have the same issue it will also work too
        boolean openGL = getOpenGLSetting();
        System.out.println(openGL);
        if (openGL) {
            System.setProperty("sun.java2d.opengl", "true");
        } else {
            System.setProperty("sun.java2d.opengl", "false");
        }

        frame = new JFrame(GAME_NAME);

        loadPersonalData();

        // Initializing panels
        menuPanel = new MainMenuPanel();
        playPanel = new PlayPanel();
        tutorialPanel = new TutorialPanel();
        aboutPanel = new AboutPanel();

        // Packing the panel together
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
