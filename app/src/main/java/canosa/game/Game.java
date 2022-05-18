package canosa.game;

import canosa.game.board.Board;

/**
 *
 * @author aaron.mitchell
 */
public class Game {
    private Board board;
    private Phase phase = Phase.SETUP;
    private PhaseStep phaseStep = PhaseStep.START_PHASE;
    private int goldSailorsScored = 0;
    private int silverSailorsScored = 0;
    private PieceType currentPlayer = PieceType.GOLD_SIREN;

    public Game(){
        board = new Board();
    }

    public void init(){
        board.init();
        goldSailorsScored = 0;
        silverSailorsScored = 0;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public Phase getPhase() {
        return phase;
    }

    public void setPhase(Phase phase) {
        this.phase = phase;
        this.phaseStep = PhaseStep.START_PHASE;
    }

    public PhaseStep getPhaseStep() {
        return phaseStep;
    }

    public void setPhaseStep(PhaseStep phaseStep) {
        this.phaseStep = phaseStep;
    }

    public int getGoldSailorsScored() {
        return goldSailorsScored;
    }

    public void setGoldSailorsScored(int goldSailorsScored) {
        this.goldSailorsScored = goldSailorsScored;
    }

    public void adjGoldSailorsScored(int v) {
        this.goldSailorsScored += v;
    }

    public int getSilverSailorsScored() {
        return silverSailorsScored;
    }

    public void setSilverSailorsScored(int silverSailorsScored) {
        this.silverSailorsScored = silverSailorsScored;
    }

    public void adjSilverSailorsScored(int v) {
        this.silverSailorsScored += v;
    }

    public PieceType getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(PieceType currentPlayer) {
        this.currentPlayer = currentPlayer;
    }
}
