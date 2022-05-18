/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package canosa;

import canosa.view.View;
import javax.swing.SwingUtilities;

public class App {
    public static void main(String[] args) {
        Model model = new Model();
        View view = new View(model);
        new Controller(model, view);

        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run() {
                view.getFrame().setVisible(true);
            }
        });
    }
}