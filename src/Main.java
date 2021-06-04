import java.io.*;
import java.util.*;
import javax.swing.*;

public class Main {
    final static String GAME_NAME = "Rocket Racer";

    MainMenuPanel menuPanel = new MainMenuPanel();
    static HashMap<String, Time> personalBests = new HashMap<>();
    
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
            // TODO Auto-generated catch block
            File newSave = new File("save/data.save");
            try {
                newSave.createNewFile();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
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
        // TODO: main menu show
    }

    public static void showOptions() {
        // TODO: show options
    }

    public static void showGame(String trackFilePath) {
        // TODO: new game time bois
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame(GAME_NAME);

        loadPersonalData();

        // TODO: level loading
        Game game = new Game("tracks/A01.track");

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
