import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class TutorialPanel extends JPanel {

    JPanel contentPanel;
    JPanel imagesPanel;

    JButton backButton;

    public TutorialPanel() {
        super(true);

        setBorder(BorderFactory.createEmptyBorder(50, 100, 50, 100));
        setPreferredSize(new Dimension(1920, 1080));
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setBackground(new Color(50, 50, 50));

        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.PAGE_AXIS));
        contentPanel.setBackground(null);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 25, 0));
        contentPanel.setFocusable(false);
        contentPanel.setAlignmentX(0f);
        contentPanel.setPreferredSize(new Dimension(1720, 980 -backButton.getHeight()));

        // Initializing the button
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

        // Content for the content panel
        

        // Adding the UI components
        add(backButton, BorderLayout.PAGE_END);
        add(contentPanel, BorderLayout.CENTER);
    }
}
