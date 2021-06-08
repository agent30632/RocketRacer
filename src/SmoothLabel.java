// Adapted from an answer by Andrew Thompson on StackOverflow concerning anti-aliased JLabels
// Essentially the same as a normal JLabel, but anti-aliasing is always turned on
import java.awt.*;
import javax.swing.*;

public class SmoothLabel extends JLabel {
    public SmoothLabel(String text) {
        super(text);
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;
        RenderingHints textAntiAliasing = new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
        g2D.setRenderingHints(textAntiAliasing);

        super.paintComponent(g2D);
    }
}
