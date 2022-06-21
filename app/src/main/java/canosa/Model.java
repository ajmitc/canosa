package canosa;

import canosa.ai.ComputerPlayer;
import canosa.game.Game;
import canosa.game.board.Cell;

/**
 *
 * @author aaron.mitchell
 */
public class Model {
    private Game game;
    private Cell selectedCell;
    private ComputerPlayer computerPlayer;

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

    public ComputerPlayer getComputerPlayer() {
        return computerPlayer;
    }

    public void setComputerPlayer(ComputerPlayer computerPlayer) {
        this.computerPlayer = computerPlayer;
    }
}
