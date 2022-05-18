package canosa.view;

import canosa.Model;
import java.awt.CardLayout;
import java.awt.Toolkit;
import javax.swing.JFrame;

/**
 *
 * @author aaron.mitchell
 */
public class View {
    private static final String MAINMENU = "mainmenu";
    private static final String GAME = "game";

    private Model model;
    private JFrame frame;

    private MainMenu mainmenu;
    private GamePanel gamePanel;

    public View(Model model){
        this.model = model;
        frame = new JFrame();
        frame.setSize(620, 660);
        frame.setLocationRelativeTo(null);
        frame.setTitle("Canosa");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        mainmenu = new MainMenu(model, this);
        gamePanel = new GamePanel(model, this);

        frame.getContentPane().setLayout(new CardLayout());
        frame.getContentPane().add(mainmenu, MAINMENU);
        frame.getContentPane().add(gamePanel, GAME);
    }

    public void showMainMenu(){
        show(MAINMENU);
    }

    public void showGame(){
        show(GAME);
        gamePanel.refresh();
    }

    private void show(String t){
        ((CardLayout) frame.getContentPane().getLayout()).show(frame.getContentPane(), t);
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public JFrame getFrame() {
        return frame;
    }

    public void setFrame(JFrame frame) {
        this.frame = frame;
    }

    public MainMenu getMainmenu() {
        return mainmenu;
    }

    public GamePanel getGamePanel() {
        return gamePanel;
    }
}
