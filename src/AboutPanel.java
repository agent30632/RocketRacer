import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class AboutPanel extends JPanel {
    // Map of all tracks by trackID

    // Necessary JComponents
    JPanel contentPanel;
    JButton backButton;

    public AboutPanel() {
        super(true);

        setBorder(BorderFactory.createEmptyBorder(50, 100, 50, 100));
        setPreferredSize(new Dimension(1920, 1080));
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setBackground(new Color(50, 50, 50));

        // Initializing the back button
        backButton = new JButton("< Back");
        backButton.setFont(Main.uiTextBig);
        backButton.setForeground(Color.WHITE);
        backButton.setBackground(Color.BLACK);
        backButton.setBorder(Main.buttonBorder);
        backButton.setFocusable(false);
        backButton.setAlignmentX(0f);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                Main.showMainMenu();
            }
        });

        // This is where all the text stuffs go
        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.PAGE_AXIS));
        contentPanel.setBackground(null);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 25, 0));
        contentPanel.setFocusable(false);
        contentPanel.setAlignmentX(0f);
        contentPanel.setPreferredSize(new Dimension(1720, 980 -backButton.getHeight()));

        // Content for the content panel
        // This is a terrible solution but also a solution that works so ¯\_(ツ)_/¯
        JLabel gameTitleLabel = new JLabel("Rocket Racer");
        gameTitleLabel.setFont(Main.uiTextBig);
        gameTitleLabel.setForeground(Color.WHITE);
        gameTitleLabel.setBackground(null);

        JLabel creatorLabel = new JLabel("Created by: Zhenyang Cai");
        creatorLabel.setFont(Main.uiTextSmall);
        creatorLabel.setForeground(Color.WHITE);
        creatorLabel.setBackground(null);

        JLabel forLabel = new JLabel("For Ms. Wong's CompSci Class, 2021");
        forLabel.setFont(Main.uiTextSmall);
        forLabel.setForeground(Color.WHITE);
        forLabel.setBackground(null);

        JLabel musicHeaderLabel = new JLabel("Music Credits");
        musicHeaderLabel.setFont(Main.uiTextBig);
        musicHeaderLabel.setForeground(Color.WHITE);
        musicHeaderLabel.setBackground(null);

        JLabel artist1 = new JLabel("Professor Kliq (Trackmania² Stadium and Valley OSTs)");
        artist1.setFont(Main.uiTextSmall);
        artist1.setForeground(Color.WHITE);
        artist1.setBackground(null);

        JLabel artist2 = new JLabel("Ramova (Trackmania 2020 OST)");
        artist2.setFont(Main.uiTextSmall);
        artist2.setForeground(Color.WHITE);
        artist2.setBackground(null);

        JLabel artist3 = new JLabel("Doo (Trackmania Wii OST)");
        artist3.setFont(Main.uiTextSmall);
        artist3.setForeground(Color.WHITE);
        artist3.setBackground(null);
        
        JLabel artist4 = new JLabel("Patricia Taxxon (Wavetable)");
        artist4.setFont(Main.uiTextSmall);
        artist4.setForeground(Color.WHITE);
        artist4.setBackground(null);

        // Adding everything to the content panel
        contentPanel.add(gameTitleLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPanel.add(creatorLabel);
        contentPanel.add(forLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 50)));
        contentPanel.add(musicHeaderLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPanel.add(artist1);
        contentPanel.add(artist2);
        contentPanel.add(artist3);
        contentPanel.add(artist4);

        // Adding the UI components
        add(contentPanel);
        add(backButton);
    }
}
