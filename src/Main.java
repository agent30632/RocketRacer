import javax.swing.*;

public class Main {

    final static String gameName = "Rocket Racer";
    public static void main(String[] args) {
        JFrame frame = new JFrame(gameName);

        Game game = new Game();

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
