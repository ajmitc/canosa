package canosa.game;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 *
 * @author aaron.mitchell
 */
public class Piece {
    private PieceType type;
    private Deque<PieceType> rings = new ArrayDeque<>(3);

    public Piece(PieceType type){
        this.type = type;
    }

    public Piece(PieceType type, PieceType ... startingRings){
        this.type = type;
        for (PieceType ring: startingRings)
            this.rings.add(ring);
    }

    public PieceType getType() {
        return type;
    }

    public Deque<PieceType> getRings() {
        return rings;
    }

    public PieceType peekTopRing(){
        return rings.peek();
    }

    public boolean pushRing(PieceType type){
        if (rings.size() < 3){
            rings.push(type);
            return true;
        }
        return false;
    }

    public PieceType popRing(){
        if (!rings.isEmpty())
            return rings.pop();
        return null;
    }
}
