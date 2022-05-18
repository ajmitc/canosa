package canosa.view;

import canosa.Model;
import canosa.game.PieceType;
import canosa.game.board.Cell;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.Iterator;
import javax.swing.JPanel;

/**
 *
 * @author aaron.mitchell
 */
public class GamePanel extends JPanel{
    private static final int XOFFSET     = 10;
    private static final int YOFFSET     = 10;
    private static final int CELL_SIZE   = 100;
    private static final int SIREN_SIZE  = 55;
    private static final int SAILOR_SIZE = 55;
    private static final int RING_SIZE   = 10;
    private static final int RING_STACK_WIDTH  = 30;
    private static final int RING_STACK_HEIGHT = 10;
    private static final Color BACKGROUND     = Color.white;
    private static final Color GOLD           = Color.yellow;
    private static final Color SILVER         = Color.gray;
    private static final Color BLUE           = Color.blue;
    private static final Color SAILOR_COLOR   = new Color(102, 51, 0);
    private static final Color ACTIVE_COLOR   = Color.red;
    private static final Color SELECTED_COLOR = Color.cyan;
    private static final Font SCORE_FONT      = new Font("Serif", Font.BOLD, 16);

    private Model model;
    private View view;

    public GamePanel(Model model, View view){
        super();
        this.model = model;
        this.view = view;
    }

    public Cell getSelectedCell(int mx, int my){
        int x = (mx - XOFFSET) / CELL_SIZE;
        int y = (my - YOFFSET) / CELL_SIZE;
        if (x < 0 || x >= 6 || y < 0 || y >= 6)
            return null;
        return model.getGame().getBoard().getCell(x, y);
    }

    @Override
    public void paintComponent(Graphics graphics){
        graphics.setColor(BACKGROUND);
        graphics.fillRect(0, 0, getWidth(), getHeight());

        model.getGame().getBoard().getCells().stream()
                .forEach(cell -> drawCell(graphics, cell));

        if (model.getSelectedCell() != null)
            drawSelectedCellBorders(graphics, model.getSelectedCell());

        drawScores(graphics);
    }

    private void drawCell(Graphics g, Cell cell){
        int px = XOFFSET + (cell.getX() * CELL_SIZE);
        int py = YOFFSET + (cell.getY() * CELL_SIZE);
        if (cell.getIslandOwner() == PieceType.GOLD_SIREN)
            g.setColor(GOLD);
        else if (cell.getIslandOwner() == PieceType.SILVER_SIREN)
            g.setColor(SILVER);
        else
            g.setColor(BLUE);
        g.fillRect(px, py, CELL_SIZE, CELL_SIZE);

        g.setColor(Color.black);
        g.drawRect(px, py, CELL_SIZE, CELL_SIZE);

        if (cell.getPiece() != null){
            switch(cell.getPiece().getType()){
                case SAILOR: {
                    drawSailor(g, cell);
                    break;
                }
                case GOLD_SIREN: 
                case SILVER_SIREN: {
                    drawSiren(g, cell);
                    break;
                }
            }
        }
    }

    private void drawSelectedCellBorders(Graphics g, Cell cell){
        int px = XOFFSET + (cell.getX() * CELL_SIZE);
        int py = YOFFSET + (cell.getY() * CELL_SIZE);

        g.setColor(SELECTED_COLOR);
        g.drawRect(px, py, CELL_SIZE, CELL_SIZE);
    }

    private void drawSailor(Graphics g, Cell cell){
        drawTopRing(g, cell);

        int px = XOFFSET + (cell.getX() * CELL_SIZE);
        int py = YOFFSET + (cell.getY() * CELL_SIZE);

        px += ((CELL_SIZE - SAILOR_SIZE) / 2);
        py += ((CELL_SIZE - SAILOR_SIZE) / 2);
        
        g.setColor(SAILOR_COLOR);
        g.fillOval(px, py, SAILOR_SIZE, SAILOR_SIZE);
        drawRingStack(g, cell);
    }

    private void drawSiren(Graphics g, Cell cell){
        int px = XOFFSET + (cell.getX() * CELL_SIZE);
        int py = YOFFSET + (cell.getY() * CELL_SIZE);

        px += ((CELL_SIZE - SIREN_SIZE) / 2);
        py += ((CELL_SIZE - SIREN_SIZE) / 2);

        if (model.getGame().getCurrentPlayer() == cell.getPiece().getType())
            g.setColor(ACTIVE_COLOR);
        else
            g.setColor(Color.BLACK);
        g.fillOval(px - 5, py - 5, SIREN_SIZE + 10, SIREN_SIZE + 10);

        if (cell.getPiece().getType() == PieceType.GOLD_SIREN)
            g.setColor(GOLD);
        else
            g.setColor(SILVER);
        
        g.fillOval(px, py, SIREN_SIZE, SIREN_SIZE);
        drawRingStack(g, cell);
    }

    private void drawTopRing(Graphics g, Cell cell){
        if (cell.getPiece().getRings().isEmpty())
            return;

        int px = XOFFSET + (cell.getX() * CELL_SIZE);
        int py = YOFFSET + (cell.getY() * CELL_SIZE);

        int size = RING_SIZE;
        if (cell.getPiece().getType().isSiren()){
            size += SIREN_SIZE;
        }
        else {
            size += SAILOR_SIZE;
        }

        px += ((CELL_SIZE - size) / 2);
        py += ((CELL_SIZE - size) / 2);

        if (cell.getPiece().peekTopRing() == PieceType.GOLD_SIREN)
            g.setColor(GOLD);
        else
            g.setColor(SILVER);
        
        g.fillOval(px, py, size, size);
    }

    private void drawRingStack(Graphics g, Cell cell){
        if (cell.getPiece().getRings().isEmpty())
            return;

        int px = XOFFSET + (cell.getX() * CELL_SIZE);
        int py = YOFFSET + (cell.getY() * CELL_SIZE);

        px += ((CELL_SIZE - RING_STACK_WIDTH) / 2);
        py += ((CELL_SIZE - RING_STACK_HEIGHT) / 2);
        py += RING_STACK_HEIGHT;

        Iterator<PieceType> reverseRings = cell.getPiece().getRings().descendingIterator();
        while (reverseRings.hasNext()){
            PieceType ring = reverseRings.next();
            if (ring == PieceType.GOLD_SIREN)
                g.setColor(GOLD);
            else
                g.setColor(SILVER);
            
            g.fillRect(px, py, RING_STACK_WIDTH, RING_STACK_HEIGHT);
            g.setColor(Color.BLACK);
            g.drawRect(px, py, RING_STACK_WIDTH, RING_STACK_HEIGHT);
            py -= RING_STACK_HEIGHT;
        }
    }

    private void drawScores(Graphics g){
        g.setFont(SCORE_FONT);
        g.setColor(Color.black);

        int x = XOFFSET + (6 * CELL_SIZE) - 20;
        int y = YOFFSET + 20;
        g.drawString("" + model.getGame().getGoldSailorsScored(), x, y);

        x = XOFFSET + 10;
        y = YOFFSET + (6 * CELL_SIZE) - 10;
        g.drawString("" + model.getGame().getSilverSailorsScored(), x, y);
    }

    public void refresh(){
        repaint();
    }
}
