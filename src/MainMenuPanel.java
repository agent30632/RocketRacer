// main menu panel

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class MainMenuPanel extends JPanel {
    static ImageIcon gameLogo = new ImageIcon("assets/img/game_logo.png");
    static JLabel logoLabel;

    static JButton playButton;
    static JButton tutorialButton;
    static JButton aboutButton;
    static JButton exitButton;

    /**
     * Creates a new instance of MainMenuPanel
     */
    public MainMenuPanel() {
        super(true);

        setBorder(BorderFactory.createEmptyBorder(50, 100, 50, 100));
        setPreferredSize(new Dimension(1920, 1080));
        setLayout(new BorderLayout());
        setBackground(new Color(50, 50, 50));

        // Where the stuff on the left side goes
        // Even though there is nothing on the right side
        JPanel leftSidePanel = new JPanel();
        leftSidePanel.setBackground(null);
        leftSidePanel.setLayout(new BoxLayout(leftSidePanel, BoxLayout.PAGE_AXIS));

        logoLabel = new JLabel(gameLogo);
        
        // Play button (level select)
        playButton = new JButton("Play");
        playButton.setFont(Main.uiTextBig);
        playButton.setForeground(Color.WHITE);
        playButton.setBackground(Color.BLACK);
        playButton.setBorder(Main.buttonBorder);
        playButton.setFocusable(false);
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.showPlay();
            }
        });

        // Tutorial button
        tutorialButton = new JButton("Tutorial");
        tutorialButton.setFont(Main.uiTextBig);
        tutorialButton.setForeground(Color.WHITE);
        tutorialButton.setBackground(Color.BLACK);
        tutorialButton.setBorder(Main.buttonBorder);
        tutorialButton.setFocusable(false);
        tutorialButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.showTutorial();
            }
        });

        // About game button
        aboutButton = new JButton("About");
        aboutButton.setFont(Main.uiTextBig);
        aboutButton.setForeground(Color.WHITE);
        aboutButton.setBackground(Color.BLACK);
        aboutButton.setBorder(Main.buttonBorder);
        aboutButton.setFocusable(false);
        aboutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Main.showGame("tracks/testTrack.track");
                Main.showAbout();
            }
        });

        // Exit game button
        exitButton = new JButton("Exit");
        exitButton.setFont(Main.uiTextBig);
        exitButton.setForeground(Color.WHITE);
        exitButton.setBackground(Color.BLACK);
        exitButton.setBorder(Main.buttonBorder);
        exitButton.setFocusable(false);
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Man I hope you didn't accidentally press this button
                System.exit(0);
            }
        });

        // Adding everything
        leftSidePanel.add(logoLabel);
        leftSidePanel.add(Box.createRigidArea(new Dimension(0, 100)));
        leftSidePanel.add(playButton);
        leftSidePanel.add(Box.createRigidArea(new Dimension(0, 25)));
        leftSidePanel.add(tutorialButton);
        leftSidePanel.add(Box.createRigidArea(new Dimension(0, 25)));
        leftSidePanel.add(aboutButton);
        leftSidePanel.add(Box.createRigidArea(new Dimension(0, 25)));
        leftSidePanel.add(exitButton);

        add(leftSidePanel, BorderLayout.LINE_START);
    }
}
