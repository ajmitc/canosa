package canosa.game.board;

import canosa.game.Piece;
import canosa.game.PieceType;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

/**
 *
 * @author aaron.mitchell
 */
public class Board {
    private List<Cell> cells = new ArrayList<>(36);

    public Board(){

    }

    public void init(){
        cells.clear();
        for (int y = 0; y < 6; ++y){
            for (int x = 0; x < 6; ++x){
                cells.add(new Cell(x, y));
            }
        }
        getCell(0, 5).setIslandOwner(PieceType.SILVER_SIREN);
        getCell(5, 0).setIslandOwner(PieceType.GOLD_SIREN);

        getCell(0, 5).setPiece(new Piece(PieceType.SILVER_SIREN, PieceType.SILVER_SIREN, PieceType.SILVER_SIREN));
        getCell(5, 0).setPiece(new Piece(PieceType.GOLD_SIREN, PieceType.GOLD_SIREN, PieceType.GOLD_SIREN));

        getCell(0, 1).setPiece(new Piece(PieceType.SAILOR, PieceType.GOLD_SIREN));
        getCell(1, 2).setPiece(new Piece(PieceType.SAILOR, PieceType.GOLD_SIREN));
        getCell(2, 3).setPiece(new Piece(PieceType.SAILOR, PieceType.GOLD_SIREN));
        getCell(3, 4).setPiece(new Piece(PieceType.SAILOR, PieceType.GOLD_SIREN));
        getCell(4, 5).setPiece(new Piece(PieceType.SAILOR, PieceType.GOLD_SIREN));

        getCell(1, 0).setPiece(new Piece(PieceType.SAILOR, PieceType.SILVER_SIREN));
        getCell(2, 1).setPiece(new Piece(PieceType.SAILOR, PieceType.SILVER_SIREN));
        getCell(3, 2).setPiece(new Piece(PieceType.SAILOR, PieceType.SILVER_SIREN));
        getCell(4, 3).setPiece(new Piece(PieceType.SAILOR, PieceType.SILVER_SIREN));
        getCell(5, 4).setPiece(new Piece(PieceType.SAILOR, PieceType.SILVER_SIREN));
    }

    public boolean isAdjacent(Cell cell1, Cell cell2){
        return Math.abs(cell1.getX() - cell2.getX()) <= 1 && Math.abs(cell1.getY() - cell2.getY()) <= 1;
    }

    public boolean isOrthogonallyAdjacent(Cell cell1, Cell cell2){
        int dx = Math.abs(cell1.getX() - cell2.getX());
        int dy = Math.abs(cell1.getY() - cell2.getY());
        return (dx <= 1 && dy <= 1 && dx != dy);
    }

    public boolean isCloserToIsland(Cell origCell, Cell destCell, PieceType island){
        Cell islandCell = island == PieceType.GOLD_SIREN? getIslandCell(PieceType.GOLD_SIREN): getIslandCell(PieceType.SILVER_SIREN);
        int distanceOrig = getDistanceBetween(origCell, islandCell);
        int distanceDest = getDistanceBetween(destCell, islandCell);
        return distanceDest <= distanceOrig;
    }

    public int getDistanceBetween(Cell cell1, Cell cell2){
        int x1 = cell1.getX();
        int x2 = cell2.getX();
        int y1 = cell1.getY();
        int y2 = cell2.getY();
        return Math.abs(x2 - x1) + Math.abs(y2 - y1);
    }

    public Piece findPieceWithType(PieceType type){
        Optional<Piece> piece = 
                cells.stream()
                        .filter(cell -> cell.getPiece() != null && cell.getPiece().getType() == type)
                        .map(cell -> cell.getPiece())
                        .findFirst();
        return piece.isPresent()? piece.get(): null;
    }

    public Cell getIslandCell(PieceType type){
        Optional<Cell> cell = 
                cells.stream()
                        .filter(c -> c.getIslandOwner() == type)
                        .findFirst();
        return cell.isPresent()? cell.get(): null;
    }

    public List<Cell> getCells() {
        return cells;
    }

    public Cell getCell(int x, int y){
        Optional<Cell> cell = cells.stream().filter(c -> c.getX() == x && c.getY() == y).findFirst();
        return cell.isPresent()? cell.get(): null;
    }
}
