package game.testing;

import game.Board;
import game.Cell;
import game.Coordinate;
import org.junit.*;

public class BoardTest {

    private Board board;

    @Before
    public void setup() {
        this.board = new Board();
    }

    @Test
    public void testGetCell() {
        Cell cell = board.getCell(new Coordinate(1, 4));
        Assert.assertNotNull(cell.getPiece());
    }

}
