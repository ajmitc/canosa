package canosa.ai;

import canosa.game.Action;
import canosa.Model;
import canosa.game.PieceType;
import canosa.game.board.Cell;
import canosa.view.View;

/**
 *
 * @author aaron.mitchell
 */
public abstract class ComputerPlayer {
    protected Model model;
    protected View view;
    protected PieceType siren;  // The siren this computer player controls
    protected Cell islandCell;

    public ComputerPlayer(Model model, View view){
        this.model = model;
        this.view = view;
    }

    /**
     * This method is called after setup and before the game begins
     */
    public void getReady(){
        islandCell = model.getGame().getBoard().getIslandCell(siren);
    }

    public abstract Action chooseAction();

    /**
     * Find a controlled sailor that is adjacent (orthogonal) to the controlling siren's island
     * @return 
     */
    protected Cell findScoringSailor(){
        Cell island = model.getGame().getBoard().getIslandCell(siren);
        for (Cell neighbor: model.getGame().getBoard().getNeighboringCells(island, false)){
            if (neighbor.getPiece() != null && neighbor.getPiece().getType() == PieceType.SAILOR && neighbor.getPiece().peekTopRing() == siren){
                return neighbor;
            }
        }
        return null;
    }

    public PieceType getSiren() {
        return siren;
    }

    public void setSiren(PieceType siren) {
        this.siren = siren;
    }
}
