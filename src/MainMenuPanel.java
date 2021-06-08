import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.Border;

public class MainMenuPanel extends JPanel {
    static ImageIcon gameLogo = new ImageIcon("assets/img/game_logo.png");
    static JLabel logoLabel;

    static JButton playButton;
    static JButton tutorialButton;
    static JButton optionsButton;
    static JButton exitButton;

    static Font uiTextBig = new Font(Font.SANS_SERIF, Font.BOLD, 64);
    static Font uiTextSmall = new Font(Font.SANS_SERIF, Font.PLAIN, 40);
    static Font uiTextSmallItalics = new Font(Font.SANS_SERIF, Font.ITALIC, 40);
    static Font uiTextMediumHighlight = new Font(Font.SANS_SERIF, Font.ITALIC|Font.BOLD, 48);
    static Font uiTextMedium = new Font(Font.SANS_SERIF, Font.BOLD, 48);

    public MainMenuPanel() {
        super(true);

        setBorder(BorderFactory.createEmptyBorder(50, 100, 50, 100));
        setPreferredSize(new Dimension(1920, 1080));
        setLayout(new BorderLayout());
        setBackground(new Color(50, 50, 50));

        JPanel leftSidePanel = new JPanel();
        leftSidePanel.setBackground(null);
        leftSidePanel.setLayout(new BoxLayout(leftSidePanel, BoxLayout.PAGE_AXIS));

        Border buttonBorder = BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.WHITE, 5), BorderFactory.createEmptyBorder(2, 15, 2, 15));

        logoLabel = new JLabel(gameLogo);
        
        playButton = new JButton("Play");
        playButton.setFont(uiTextBig);
        playButton.setForeground(Color.WHITE);
        playButton.setBackground(Color.BLACK);
        playButton.setBorder(buttonBorder);
        playButton.setFocusable(false);
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                Main.showGame("tracks/testTrack.track");   
            }
        });

        optionsButton = new JButton("Options");
        optionsButton.setFont(uiTextBig);
        optionsButton.setForeground(Color.WHITE);
        optionsButton.setBackground(Color.BLACK);
        optionsButton.setBorder(buttonBorder);
        optionsButton.setFocusable(false);

        exitButton = new JButton("Exit");
        exitButton.setFont(uiTextBig);
        exitButton.setForeground(Color.WHITE);
        exitButton.setBackground(Color.BLACK);
        exitButton.setBorder(buttonBorder);
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
        leftSidePanel.add(Box.createRigidArea(new Dimension(0, 30)));
        leftSidePanel.add(optionsButton);
        leftSidePanel.add(Box.createRigidArea(new Dimension(0, 30)));
        leftSidePanel.add(exitButton);

        add(leftSidePanel, BorderLayout.LINE_START);

        // TODO: buttons
        
    }
}
