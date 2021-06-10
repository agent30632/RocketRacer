import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

import javax.swing.*;

public class PlayPanel extends JPanel {
    // Map of all tracks by trackID
    public static TreeMap<String, String> trackNameFilepathMap = new TreeMap<>();

    public static ImageIcon bronzeMedal = new ImageIcon("assets/img/medal_bronze.png");
    public static ImageIcon bronzeMedalImageScaled = new ImageIcon(bronzeMedal.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH));
    public static ImageIcon silverMedal = new ImageIcon("assets/img/medal_silver.png");
    public static ImageIcon silverMedalImageScaled = new ImageIcon(silverMedal.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH));
    public static ImageIcon goldMedal = new ImageIcon("assets/img/medal_gold.png");
    public static ImageIcon goldMedalImageScaled = new ImageIcon(goldMedal.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH));
    public static ImageIcon authorMedal = new ImageIcon("assets/img/medal_author.png");
    public static ImageIcon authorMedalImageScaled = new ImageIcon(authorMedal.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH));
    public static ImageIcon noMedal = new ImageIcon("assets/img/medal_blank.png");
    public static ImageIcon noMedalImageScaled = new ImageIcon(noMedal.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH));

    // Necessary JComponents
    JPanel selectionPanel;
    JButton backButton;

    public static void loadAllTracks() {
        File trackFolder = new File("tracks");
        File[] trackFolderArray = trackFolder.listFiles();

        for(File file : trackFolderArray) {
            String fileExt = "";
            int dot = file.getName().lastIndexOf(".");
            if (dot > 0) {
                fileExt = file.getName().substring(dot + 1);
            }

            if (fileExt.compareToIgnoreCase("track") == 0) {
                try {
                    Track track = new Track(file.getAbsolutePath());
                    trackNameFilepathMap.put(track.getTrackID(), file.getAbsolutePath());
                } catch (IOException e) {
                    //TODO: handle exception
                    // do nothing I guess
                }
            }
        }
    }

    public PlayPanel() {
        super(true);

        setBorder(BorderFactory.createEmptyBorder(50, 100, 50, 100));
        setPreferredSize(new Dimension(1920, 1080));
        setLayout(new BorderLayout());
        setBackground(new Color(50, 50, 50));

        // Initializing the button
        backButton = new JButton("< Back");
        backButton.setFont(Main.uiTextBig);
        backButton.setForeground(Color.WHITE);
        backButton.setBackground(Color.BLACK);
        backButton.setBorder(Main.buttonBorder);
        backButton.setFocusable(false);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                Main.showMainMenu();
            }
        });

        // Level selection list
        selectionPanel = new JPanel();
        selectionPanel.setLayout(new BoxLayout(selectionPanel, BoxLayout.PAGE_AXIS));
        selectionPanel.setBackground(null);

        // Box testBox = Box.createHorizontalBox();
        JButton testLabel = new JButton("Test 1");
        testLabel.setIcon(authorMedalImageScaled);
        testLabel.setForeground(Color.WHITE);
        testLabel.setBackground(null);
        testLabel.setBorder(Main.levelButtonBorder);
        testLabel.setFont(Main.uiTextMedium);
        testLabel.setFocusable(false);
        testLabel.setAlignmentY(0f);
        testLabel.setAlignmentX(0f);

        selectionPanel.add(testLabel);

        JButton testLabel2 = new JButton("Test 2");
        testLabel2.setIcon(noMedalImageScaled);
        testLabel2.setForeground(Color.WHITE);
        testLabel2.setBackground(null);
        testLabel2.setBorder(Main.levelButtonBorder);
        testLabel2.setFont(Main.uiTextMedium);
        testLabel2.setFocusable(false);
        testLabel2.setAlignmentY(0f);
        testLabel2.setAlignmentX(0f);

        selectionPanel.add(testLabel2);
        
        // Adding the UI components
        add(backButton, BorderLayout.PAGE_END);
        add(selectionPanel, BorderLayout.CENTER);
    }
}
