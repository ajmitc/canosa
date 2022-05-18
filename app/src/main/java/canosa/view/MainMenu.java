package canosa.view;

import canosa.Model;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 *
 * @author aaron.mitchell
 */
public class MainMenu extends JPanel{
    private Model model;
    private View view;

    private JButton btnExit;
    private JButton btnPlay;

    public MainMenu(Model model, View view){
        super();
        this.model = model;
        this.view = view;

        btnExit = new JButton("Exit");
        btnPlay = new JButton("Play");

        new GridBagLayoutHelper(this, true)
                .add(btnPlay)
                .nextRow()
                .add(btnExit)
                ;
    }

    public JButton getBtnExit() {
        return btnExit;
    }

    public JButton getBtnPlay() {
        return btnPlay;
    }
}
