package canosa.ai;

import canosa.game.ActionType;
import canosa.game.Action;
import canosa.Model;
import canosa.game.PieceType;
import canosa.game.board.Cell;
import canosa.view.View;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 *
 * @author aaron.mitchell
 */
public class EasyComputerPlayer extends ComputerPlayer{
    private static final Logger logger = Logger.getLogger(EasyComputerPlayer.class.getName());
    
    public EasyComputerPlayer(Model model, View view){
        super(model, view);
    }

    @Override
    public Action chooseAction(){
        Action action = null;

        // If we can score a sailor, do it
        logger.info("\n\nLooking for Sailor to score");
        action = tryScoreSailor();
        if (action != null){
            logger.info("  Found Sailor at " + action.getSourceCell());
            return action;
        }

        Cell sirenCell = model.getGame().getBoard().getSirenCell(siren);

        // Move a controlled sailor toward our island
        logger.info("Look for controlled sailor to move toward island");
        List<Cell> farthestSailors = model.getGame().getBoard().findFarthestControlledSailors(sirenCell);
        if (!farthestSailors.isEmpty()){
            for (Cell sailorCell: farthestSailors){
                List<Cell> path = model.getGame().getBoard().findShortestPath(sailorCell, islandCell, true);
                if (path != null && !path.isEmpty()){
                    logger.info("  Found controlled sailor at " + sailorCell + ", moving to " + path.get(0));
                    return new Action(ActionType.MOVE_SAILOR, sailorCell, path.get(0));
                }
            }
            logger.info("  Found " + farthestSailors.size() + " controlled sailors, but none have a path to the island");
        }
        else {
            logger.info("  No controlled sailors found");
        }

        Set<Cell> neighbors = model.getGame().getBoard().getNeighboringCells(sirenCell, true);
        List<Cell> neighborsWithSailors = 
                neighbors.stream()
                        .filter(c -> c.getPiece() != null && c.getPiece().getType() == PieceType.SAILOR)
                        .filter(c -> c.getPiece().peekTopRing() == null || c.getPiece().peekTopRing() != siren)
                        .filter(c -> c.getPiece().getRings().size() < 3)
                        .collect(Collectors.toList());

        // If we're not adjacent to a sailor, move toward one that we don't control
        if (neighborsWithSailors.isEmpty()){
            logger.info("Look for uncontrolled sailor to move toward");
            action = tryMoveTowardUncontrolledSailor(sirenCell);
            if (action != null){
                logger.info("  Found uncontrolled sailor.  Moving to " + action.getTargetCell());
                return action;
            }
        }
        else {
            // We're adjacent to an uncontrolled sailor, try to control it
            logger.info("Looking for adjacent uncontrolled sailor");
            action = tryTransferRingToSailor(sirenCell, neighborsWithSailors);
            if (action != null){
                logger.info("Found adjacent uncontrolled sailor.  Transferring ring to sailor at " + action.getTargetCell());
                return action;
            }
        }

        // Welp, if there's nothing to do, just move to an empty space
        logger.info("Move to an adjacent empty cell");
        Optional<Cell> emptyAdjacentCell = neighbors.stream().filter(c -> c.getPiece() == null && c.getIslandOwner() == null).findFirst();
        if (emptyAdjacentCell.isPresent())
            return new Action(ActionType.MOVE_SIREN, sirenCell, emptyAdjacentCell.get());

        // Hum, no adjacent empty cell, we must be trapped
        return null;
    }

    private Action tryScoreSailor(){
        Cell cell = findScoringSailor();
        if (cell != null){
            return new Action(ActionType.MOVE_SAILOR, cell, islandCell);
        }
        return null;
    }

    private Action tryMoveTowardUncontrolledSailor(Cell sirenCell){
        List<Cell> closestSailors = model.getGame().getBoard().findClosestUncontrolledSailors(sirenCell);
        if (!closestSailors.isEmpty()){
            List<Cell> path = model.getGame().getBoard().findShortestPath(sirenCell, closestSailors.get(0));
            if (!path.isEmpty()){
                return new Action(ActionType.MOVE_SIREN, sirenCell, path.get(0));
            }
        }
        return null;
    } 

    private Action tryTransferRingToSailor(Cell sirenCell, List<Cell> neighborsWithSailors){
        if (!sirenCell.getPiece().getRings().isEmpty()){
            return new Action(ActionType.TRANSFER_RING_TO_SAILOR, sirenCell, neighborsWithSailors.get(0));
        }
        return null;
    }
}
