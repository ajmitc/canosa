package canosa;

import canosa.game.Phase;
import canosa.game.PhaseStep;
import canosa.game.Piece;
import canosa.game.PieceType;
import canosa.game.board.Cell;
import canosa.view.View;
import canosa.view.ViewUtil;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Winner: first player to score 4 Sailors, or trap opponent (opponent has no actions they can take)
 * 
 * Gold Siren goes first with 1 action, then silver takes 2 actions, then gold takes 2 actions, and so on
 * 
 * Must take 2 actions (can repeat same action twice)
 * 
 * Actions:
 * - Move Siren one space to any open adjacent space in any direction (cannot move into an island)
 *    - Cannot move into their island once they've left
 *    - Cannot move into opponent's island
 *    - Cannot jump over sailors
 *    - If move onto space of opponent Siren and you have more rings than opponent, move the opponent Siren to any unoccupied adjacent space (exc islands)
 * - Move Sailor that you control one space to any unoccupied orthogonally-adjacent space toward your island (cannot move backward or away from your island)
 *    - Can only move with a ring on it
 *    - Top ring determines which Siren can move it
 *    - Cannot jump another Sailor
 *    - Maximum of 3 rings at any given time
 *    - Can have multiple rings of same color
 *    - Cannot transfer rings to other sailors
 * - Transfer a ring from your Siren to a Sailor (Siren must be adjacent inc diagonal to Sailor)
 * - Transfer the top ring from an adjacent sailor to your siren (must be your ring)
 *   - Cannot transfer the opponent's ring
 *   - Siren can have a maximum of 3 rings at any given time
 * 
 * Score sailors by moving sailor onto island space
 * - immediately remove sailor from board
 * - Rings are distributed back to the sirens under these conditions:
 *   - a max of 1 ring is returned to each Siren (if siren already has 3 rings on it, the ring is removed from game)
 *   - all remaining rings are removed from game
 * 
 * @author aaron.mitchell
 */
public class Controller {
    private static final Logger logger = Logger.getLogger(Controller.class.getName());

    private Model model;
    private View view;

    private int actionsTaken = 0;

    private Cell defendingSirenCell = null;
    private boolean isSelectingSirenDestCell = false;

    public Controller(Model model, View view){
        this.model = model;
        this.view = view;

        view.getMainmenu().getBtnExit().addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                System.exit(0);
            }
        });

        view.getMainmenu().getBtnPlay().addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                view.showGame();
                run();
            }
        });

        // To move a siren, player click on their siren and click on adjacent space
        // TODO If attacking opponent siren, click on player siren, then on opponent siren, then select destination space
        // To move a sailor, player click on sailor they control and click on orthogonally-adjacent space
        // To transfer ring from siren to sailor, player click on their siren and then click on adjacent sailor
        // To transfer ring from sailor to siren, player click on sailor and then click on adjacent siren
        // To de-select siren or sailor, click on it again

        view.getGamePanel().addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                Cell newSelectedCell = view.getGamePanel().getSelectedCell(e.getX(), e.getY());
                logger.info("New selected cell: " + newSelectedCell);

                // If the user didn't select a cell, do nothing
                if (newSelectedCell == null)
                    return;

                PieceType currentPlayer = model.getGame().getCurrentPlayer();

                if (isSelectingSirenDestCell){
                    if (newSelectedCell.getPiece() != null || newSelectedCell.getIslandOwner() != null){
                        logger.info("Selected cell already has piece or is island");
                        return;
                    }
                    logger.info("   Bumping Siren at " + defendingSirenCell + " to " + newSelectedCell);
                    newSelectedCell.setPiece(defendingSirenCell.getPiece());
                    defendingSirenCell.setPiece(model.getSelectedCell().getPiece());
                    model.getSelectedCell().setPiece(null);
                    actionsTaken += 1;
                    model.setSelectedCell(null);
                    defendingSirenCell = null;
                    isSelectingSirenDestCell = false;
                    run();
                    return;
                }

                // If user selected same cell, deselect the selected cell
                if (model.getSelectedCell() == newSelectedCell){
                    model.setSelectedCell(null);
                    logger.info("Deselected cell");
                    view.getGamePanel().refresh();
                    return;
                }

                // If no cell was selected, select this new one
                if (model.getSelectedCell() == null && newSelectedCell.getPiece() != null){
                    model.setSelectedCell(newSelectedCell);
                    logger.info("Set new selected cell to " + model.getSelectedCell());
                    view.getGamePanel().refresh();
                    return;
                }

                if (model.getSelectedCell() != null && model.getSelectedCell().getPiece() != null){

                    // If player selected their own siren first
                    if (model.getSelectedCell().getPiece().getType() == currentPlayer){

                        // If the newly selected cell is not adjacent to the originally selected cell, set it as the newly selected cell
                        if (!model.getGame().getBoard().isAdjacent(model.getSelectedCell(), newSelectedCell)){
                            if (newSelectedCell.getPiece() != null){
                                model.setSelectedCell(newSelectedCell);
                                logger.info("Selected cell " + model.getSelectedCell());
                                view.getGamePanel().refresh();
                            }
                            return;
                        }

                        // If player selected empty adjacent space = Move siren
                        if (newSelectedCell.getPiece() == null && newSelectedCell.getIslandOwner() == null){
                            logger.info("Moving siren to " + newSelectedCell);
                            newSelectedCell.setPiece(model.getSelectedCell().getPiece());
                            model.getSelectedCell().setPiece(null);
                            actionsTaken += 1;
                            model.setSelectedCell(null);
                            run();
                            return;
                        }

                        // If player selected enemy siren and has more rings, bump it
                        if (newSelectedCell.getPiece() != null){
                            if (newSelectedCell.getPiece().getType().isSiren()){
                                if (model.getSelectedCell().getPiece().getRings().size() > newSelectedCell.getPiece().getRings().size()){
                                    // Let user select destination cell
                                    logger.info("Siren at " + model.getSelectedCell() + " attacking siren at " + newSelectedCell);
                                    isSelectingSirenDestCell = true;
                                    defendingSirenCell = newSelectedCell;
                                    view.getGamePanel().refresh();
                                    return;
                                }
                            }
                            else {
                                // Newly selected piece is sailor
                                if (model.getSelectedCell().getPiece().getRings().size() > 0 && newSelectedCell.getPiece().getRings().size() < 3){
                                    // Transfer a ring to the sailor
                                    logger.info("Transferring ring from siren to sailor at " + model.getSelectedCell());
                                    PieceType ring = model.getSelectedCell().getPiece().getRings().pop();
                                    newSelectedCell.getPiece().getRings().push(ring);
                                    actionsTaken += 1;
                                    model.setSelectedCell(null);
                                    run();
                                    return;
                                }
                            }
                        }
                    }
                    // If player selected a sailor
                    else if (!model.getSelectedCell().getPiece().getType().isSiren()){

                        // If player selected empty adjacent space and controlled by current player = Move sailor
                        // TODO Sailor can only move TOWARD the controlling siren's island
                        if (newSelectedCell.getPiece() == null && model.getSelectedCell().getPiece().peekTopRing() == currentPlayer){
                            // If the newly selected cell is not orthogonally adjacent to the originally selected cell, set it as the newly selected cell
                            if (model.getGame().getBoard().isOrthogonallyAdjacent(model.getSelectedCell(), newSelectedCell) && 
                                    model.getGame().getBoard().isCloserToIsland(model.getSelectedCell(), newSelectedCell, model.getSelectedCell().getPiece().peekTopRing())){
                                if (newSelectedCell.getIslandOwner() == null || newSelectedCell.getIslandOwner() == currentPlayer){
                                    logger.info("Moving sailor to " + newSelectedCell);
                                    newSelectedCell.setPiece(model.getSelectedCell().getPiece());
                                    model.getSelectedCell().setPiece(null);
                                    view.getGamePanel().refresh();
                                    if (newSelectedCell.getIslandOwner() == model.getGame().getCurrentPlayer()){
                                        logger.info("Scoring sailor");
                                        // Score sailor
                                        if (newSelectedCell.getPiece().peekTopRing() == PieceType.GOLD_SIREN)
                                            model.getGame().adjGoldSailorsScored(1);
                                        else if (newSelectedCell.getPiece().peekTopRing() == PieceType.SILVER_SIREN)
                                            model.getGame().adjSilverSailorsScored(1);
                                        // Return 1 ring to each player, the rest are lost
                                        Optional<PieceType> goldRing = 
                                                newSelectedCell.getPiece().getRings().stream().filter(ring -> ring == PieceType.GOLD_SIREN).findFirst();
                                        Optional<PieceType> silverRing = 
                                                newSelectedCell.getPiece().getRings().stream().filter(ring -> ring == PieceType.SILVER_SIREN).findFirst();
                                        if (goldRing.isPresent()){
                                            Piece goldSiren = model.getGame().getBoard().findPieceWithType(PieceType.GOLD_SIREN);
                                            goldSiren.pushRing(goldRing.get());
                                        }
                                        if (silverRing.isPresent()){
                                            Piece silverSiren = model.getGame().getBoard().findPieceWithType(PieceType.SILVER_SIREN);
                                            silverSiren.pushRing(silverRing.get());
                                        }
                                        newSelectedCell.setPiece(null);
                                    }
                                    actionsTaken += 1;
                                    model.setSelectedCell(null);
                                    run();
                                    return;
                                }
                            }
                        }

                        // If player selected own siren and top ring belongs to current player, transfer ring to siren
                        if (newSelectedCell.getPiece() != null && newSelectedCell.getPiece().getType() == currentPlayer){
                            if (newSelectedCell.getPiece().getRings().size() < 3 && model.getSelectedCell().getPiece().peekTopRing() == currentPlayer){
                                // Move top ring to siren
                                logger.info("Moving ring from sailor to siren at " + newSelectedCell);
                                PieceType ring = model.getSelectedCell().getPiece().popRing();
                                newSelectedCell.getPiece().pushRing(ring);
                                actionsTaken += 1;
                                model.setSelectedCell(null);
                                run();
                                return;
                            }
                        }
                    }
                }
            }
        });
    }

    public void run(){
        if (model.getGame() == null)
            return;

        while (model.getGame().getPhase() != Phase.GAMEOVER){
            view.getGamePanel().refresh();
            checkGameOver();
            switch(model.getGame().getPhase()){
                case SETUP: {
                    switch(model.getGame().getPhaseStep()){
                        case START_PHASE: {
                            model.getGame().init();
                            model.getGame().setPhaseStep(PhaseStep.END_PHASE);
                            break;
                        }
                        case END_PHASE: {
                            model.getGame().setPhase(Phase.PLAY);
                            break;
                        }
                    }
                    break;
                }
                case PLAY: {
                    switch(model.getGame().getPhaseStep()){
                        case START_PHASE: {
                            if (actionsTaken >= 2){
                                model.getGame().setPhaseStep(PhaseStep.END_PHASE);
                                break;
                            }
                            // Wait for player to take action
                            return;
                        }
                        case END_PHASE: {
                            model.getGame().setCurrentPlayer(
                                    model.getGame().getCurrentPlayer() == PieceType.GOLD_SIREN? 
                                            PieceType.SILVER_SIREN: 
                                            PieceType.GOLD_SIREN);
                            actionsTaken = 0;
                            model.getGame().setPhaseStep(PhaseStep.START_PHASE);
                            break;
                        }
                    }
                    break;
                }
            }
        }
    }

    private void checkGameOver(){
        boolean goldPlayerWins = false;
        boolean silverPlayerWins = false;
        goldPlayerWins   = model.getGame().getGoldSailorsScored() >= 4;
        silverPlayerWins = model.getGame().getSilverSailorsScored() >= 4;

        // TODO Check if player trapped

        if (goldPlayerWins || silverPlayerWins){
            model.getGame().setPhase(Phase.GAMEOVER);
            ViewUtil.popupNotify("Game Over: " + (goldPlayerWins? "Gold": "Silver") + " player wins!");
        }
    }
}
