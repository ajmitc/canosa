package canosa.game;

/**
 *
 * @author aaron.mitchell
 */
public enum PieceType {
    GOLD_SIREN,
    SILVER_SIREN,
    SAILOR;

    public boolean isSiren(){
        return this == GOLD_SIREN || this == SILVER_SIREN;
    }
}
