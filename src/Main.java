import javax.swing.*;

public class Main {
    final static String GAME_NAME = "Rocket Racer";

    MainMenuPanel menuPanel = new MainMenuPanel();

    public static void main(String[] args) {
        JFrame frame = new JFrame(GAME_NAME);

        // TODO: level loading
        Game game = new Game("fillerString");

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
