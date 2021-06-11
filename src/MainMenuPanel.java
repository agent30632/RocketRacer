import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.Border;

public class MainMenuPanel extends JPanel {
    static ImageIcon gameLogo = new ImageIcon("assets/img/game_logo.png");
    static JLabel logoLabel;

    static JButton playButton;
    static JButton tutorialButton;
    static JButton aboutButton;
    static JButton exitButton;

    public MainMenuPanel() {
        super(true);

        setBorder(BorderFactory.createEmptyBorder(50, 100, 50, 100));
        setPreferredSize(new Dimension(1920, 1080));
        setLayout(new BorderLayout());
        setBackground(new Color(50, 50, 50));

        JPanel leftSidePanel = new JPanel();
        leftSidePanel.setBackground(null);
        leftSidePanel.setLayout(new BoxLayout(leftSidePanel, BoxLayout.PAGE_AXIS));

        logoLabel = new JLabel(gameLogo);
        
        playButton = new JButton("Play");
        playButton.setFont(Main.uiTextBig);
        playButton.setForeground(Color.WHITE);
        playButton.setBackground(Color.BLACK);
        playButton.setBorder(Main.buttonBorder);
        playButton.setFocusable(false);
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                // Main.showGame("tracks/testTrack.track");
                Main.showPlay();
            }
        });

        tutorialButton = new JButton("Tutorial");
        tutorialButton.setFont(Main.uiTextBig);
        tutorialButton.setForeground(Color.WHITE);
        tutorialButton.setBackground(Color.BLACK);
        tutorialButton.setBorder(Main.buttonBorder);
        tutorialButton.setFocusable(false);
        tutorialButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                // Main.showGame("tracks/testTrack.track");
                Main.showTutorial();
            }
        });

        aboutButton = new JButton("About");
        aboutButton.setFont(Main.uiTextBig);
        aboutButton.setForeground(Color.WHITE);
        aboutButton.setBackground(Color.BLACK);
        aboutButton.setBorder(Main.buttonBorder);
        aboutButton.setFocusable(false);
        aboutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                // Main.showGame("tracks/testTrack.track");
                Main.showAbout();
            }
        });

        exitButton = new JButton("Exit");
        exitButton.setFont(Main.uiTextBig);
        exitButton.setForeground(Color.WHITE);
        exitButton.setBackground(Color.BLACK);
        exitButton.setBorder(Main.buttonBorder);
        exitButton.setFocusable(false);
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                System.exit(0);
            }
        });

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
