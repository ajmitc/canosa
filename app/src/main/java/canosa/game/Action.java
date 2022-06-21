package canosa.game;

import canosa.game.board.Cell;

/**
 *
 * @author aaron.mitchell
 */
public class Action {
    private ActionType type;
    private Cell sourceCell;  // Cell originating the action
    private Cell targetCell;  // Cell targeted
    private Cell bumpTargetCell; // only used when pushing opponent siren to new cell

    public Action(){

    }

    public Action(ActionType type){
        this(type, null, null, null);
    }

    public Action(ActionType type, Cell sourceCell, Cell targetCell){
        this(type, sourceCell, targetCell, null);
    }

    public Action(ActionType type, Cell sourceCell, Cell targetCell, Cell bumpTargetCell){
        this.type = type;
        this.sourceCell = sourceCell;
        this.targetCell = targetCell;
        this.bumpTargetCell = bumpTargetCell;
    }

    public ActionType getType() {
        return type;
    }

    public void setType(ActionType type) {
        this.type = type;
    }

    public Cell getSourceCell() {
        return sourceCell;
    }

    public void setSourceCell(Cell sourceCell) {
        this.sourceCell = sourceCell;
    }

    public Cell getTargetCell() {
        return targetCell;
    }

    public void setTargetCell(Cell targetCell) {
        this.targetCell = targetCell;
    }

    public Cell getBumpTargetCell() {
        return bumpTargetCell;
    }

    public void setBumpTargetCell(Cell bumpTargetCell) {
        this.bumpTargetCell = bumpTargetCell;
    }
}
