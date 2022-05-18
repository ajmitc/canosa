package canosa.game.board;

import canosa.game.Piece;
import canosa.game.PieceType;

/**
 *
 * @author aaron.mitchell
 */
public class Cell {
    private Piece piece;    
    private int x = 0, y = 0;
    private PieceType islandOwner;

    public Cell(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public PieceType getIslandOwner() {
        return islandOwner;
    }

    public void setIslandOwner(PieceType islandOwner) {
        this.islandOwner = islandOwner;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder("[");
        sb.append(x);
        sb.append(", ");
        sb.append(y);
        sb.append("]");
        if (islandOwner != null){
            sb.append(" (");
            sb.append(islandOwner);
            sb.append(" island)");
        }
        if (piece != null){
            sb.append(" ");
            sb.append(piece.getType());
        }
        return sb.toString();
    }
}
