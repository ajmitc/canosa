package canosa;

import canosa.game.Game;
import canosa.game.board.Cell;

/**
 *
 * @author aaron.mitchell
 */
public class Model {
    private Game game;
    private Cell selectedCell;

    public Model(){
        game = new Game();
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Cell getSelectedCell() {
        return selectedCell;
    }

    public void setSelectedCell(Cell selectedCell) {
        this.selectedCell = selectedCell;
    }
}
