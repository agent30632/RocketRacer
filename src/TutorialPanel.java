import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class TutorialPanel extends JPanel {

    JPanel outerPanel;
    JPanel contentPanel;  
    JPanel imagesPanel;

    JButton backButton;

    /**
     * Creates a JLabel with the specified text and font, left-aligned and with white foreground and transparent background
     * @param text the text for the label
     * @param font the font to use for the label
     * @return the resultant JLabel
     */
    public static JLabel createLeftAlignedLabel(String text, Font font) {
        JLabel createdLabel = new JLabel(text);
        createdLabel.setFont(font);
        createdLabel.setForeground(Color.WHITE);
        createdLabel.setBackground(null);
        createdLabel.setFocusable(false);
        createdLabel.setAlignmentX(0f);

        return createdLabel;
    }

    /**
     * Creates a label with both an image and text, with the text below the image
     * @param text text to put under the image
     * @param image image to put above the text
     * @param font font to use for the text
     * @return the resultant JLabel
     */
    public static JLabel createTextImageLabel(String text, ImageIcon image, Font font) {
        JLabel createdImageLabel = new JLabel(text);
        createdImageLabel.setFont(font);
        createdImageLabel.setForeground(Color.WHITE);
        createdImageLabel.setBackground(null);
        createdImageLabel.setIcon(image);
        createdImageLabel.setHorizontalTextPosition(JLabel.CENTER);
        createdImageLabel.setVerticalTextPosition(JLabel.BOTTOM);
        createdImageLabel.setHorizontalAlignment(JLabel.CENTER);

        return createdImageLabel;
    }

    public TutorialPanel() {
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
                Main.showMainMenu();
            }
        });

        // Creating the outer panel (contains the content)
        outerPanel = new JPanel();
        outerPanel.setLayout(new BorderLayout());
        outerPanel.setBackground(null);
        outerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 25, 0));
        outerPanel.setFocusable(false);
        outerPanel.setAlignmentX(0f);

        // Creating the images panel (contains block images)
        GridLayout imageGrid = new GridLayout(4, 2);
        imagesPanel = new JPanel();
        imagesPanel.setLayout(imageGrid);
        imagesPanel.setBackground(null);
        imagesPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 25, 0));
        imagesPanel.setFocusable(false);
        imagesPanel.setAlignmentX(0.5f);

        // Images for the image panel
        JLabel startBlockImageLabel = createTextImageLabel("Start Block", TrackBlock.startBlock, Main.uiTextSmallItalics);
        JLabel checkpointBlockImageLabel = createTextImageLabel("Checkpoint Block", TrackBlock.checkpointBlock, Main.uiTextSmallItalics);
        JLabel finishBlockImageLabel = createTextImageLabel("Finish Block", TrackBlock.finishBlock, Main.uiTextSmallItalics);
        JLabel boostBlockImageLabel = createTextImageLabel("Boost Block", TrackBlock.boostBlock, Main.uiTextSmallItalics);
        JLabel noControlImageBlock = createTextImageLabel("No Control Block", TrackBlock.nocontrolBlock, Main.uiTextSmallItalics);
        JLabel resetBlockImageLabel = createTextImageLabel("Reset Block", TrackBlock.resetBlock, Main.uiTextSmallItalics);

        // Adding things to the images panel
        imagesPanel.add(startBlockImageLabel);
        imagesPanel.add(checkpointBlockImageLabel);
        imagesPanel.add(finishBlockImageLabel);
        imagesPanel.add(boostBlockImageLabel);
        imagesPanel.add(noControlImageBlock);
        imagesPanel.add(resetBlockImageLabel);

        // Creating the content panel (contains the text labels)
        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.PAGE_AXIS));
        contentPanel.setBackground(null);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 25, 0));
        contentPanel.setFocusable(false);
        contentPanel.setAlignmentX(0f);

        // Content for the content panel
        // Maybe instead of JLabels a better idea would've been a JTextArea or something
        // Controls
        JLabel controlsLabel = createLeftAlignedLabel("Controls", Main.uiTextMedium);
        JLabel forwardsLabel = createLeftAlignedLabel("W - Forwards", Main.uiTextTiny);
        JLabel leftLabel = createLeftAlignedLabel("A - Turn Left", Main.uiTextTiny);
        JLabel rightLabel = createLeftAlignedLabel("D - Turn Right", Main.uiTextTiny);
        JLabel brakeLabel = createLeftAlignedLabel("LShift - Brake", Main.uiTextTiny);
        JLabel resetLabel = createLeftAlignedLabel("Backspace - Reset to Start", Main.uiTextTiny);
        JLabel respawnLabel = createLeftAlignedLabel("Enter - Respawn at Last Checkpoint", Main.uiTextTiny);

        // How to play
        JLabel howToPlayLabel = createLeftAlignedLabel("How to Play", Main.uiTextMedium);
        JLabel descriptionLabel1 = createLeftAlignedLabel("Play as many tracks as you can, finish as fast as possible, and gather as many medals as possible!", Main.uiTextTiny);
        JLabel descriptionLabel2 = createLeftAlignedLabel("Each track has four medal times to beat: bronze, silver, gold, and author, each of which provide", Main.uiTextTiny);
        JLabel descriptionLabel3 = createLeftAlignedLabel("a new challenge and endless replayability!", Main.uiTextTiny);
        JLabel descriptionLabel4 = createLeftAlignedLabel("Time elapsed and checkpoints are shown in the bottom middle, with speed in the bottom right.", Main.uiTextTiny);

        // Block descriptions
        JLabel blockLabel = createLeftAlignedLabel("Track Blocks", Main.uiTextMedium);
        JLabel blockPrefaceLabel = createLeftAlignedLabel("Each track is composed of blocks, which all have different behaviour:", Main.uiTextTiny);
        JLabel startBlockDescriptionLabel = createLeftAlignedLabel("    - Start Block: Where your rocket starts the level", Main.uiTextTiny);
        JLabel checkpointBlockDescriptionLabel = createLeftAlignedLabel("    - Checkpoint block: Go through all of these to finish a level", Main.uiTextTiny);
        JLabel finishBlockDescriptionLabel = createLeftAlignedLabel("    - Finish block: End here after reaching all checkpoints", Main.uiTextTiny);
        JLabel boostBlockDescriptionLabel = createLeftAlignedLabel("    - Boost block: Increases the acceleration of your rocket until the next reset block", Main.uiTextTiny);
        JLabel nocontrolBlockDescriptionLabel = createLeftAlignedLabel("    - No control block: Disables control of your rocket until the next reset block", Main.uiTextTiny);
        JLabel resetBlockDescriptionLabel = createLeftAlignedLabel("    - Reset block: Resets you to your original state", Main.uiTextTiny);

        // Adding things to the content panel
        // Adding control labels
        contentPanel.add(controlsLabel);
        contentPanel.add(forwardsLabel);
        contentPanel.add(leftLabel);
        contentPanel.add(rightLabel);
        contentPanel.add(brakeLabel);
        contentPanel.add(resetLabel);
        contentPanel.add(respawnLabel);

        // Adding gameplay tutorial labels
        contentPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        contentPanel.add(howToPlayLabel);
        contentPanel.add(descriptionLabel1);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        contentPanel.add(descriptionLabel2);
        contentPanel.add(descriptionLabel3);
        contentPanel.add(descriptionLabel4);

        // Adding block explanation
        contentPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        contentPanel.add(blockLabel);
        contentPanel.add(blockPrefaceLabel);
        contentPanel.add(startBlockDescriptionLabel);
        contentPanel.add(checkpointBlockDescriptionLabel);
        contentPanel.add(finishBlockDescriptionLabel);
        contentPanel.add(boostBlockDescriptionLabel);
        contentPanel.add(nocontrolBlockDescriptionLabel);
        contentPanel.add(resetBlockDescriptionLabel);

        // Back button
        contentPanel.add(Box.createRigidArea(new Dimension(0, 40)));
        contentPanel.add(backButton);

        // Adding things to the outer panel
        outerPanel.add(imagesPanel, BorderLayout.LINE_END);
        outerPanel.add(contentPanel, BorderLayout.CENTER);

        // Adding the UI components
        add(outerPanel);
        // add(backButton);
    }
}
