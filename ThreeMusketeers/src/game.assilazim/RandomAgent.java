package game;

import java.util.*;

public class RandomAgent extends Agent {

    public RandomAgent(Board board) {
        super(board);
    }

    /**
     * Gets a valid random move the RandomAgent can do.
     * @return a valid Move that the RandomAgent can perform on the Board
     */
    @Override
    public Move getMove() { // TODO
        Random rand = new Random();
        List<Move> moves = this.board.getPossibleMoves();
        return moves.get(rand.nextInt(moves.size()));
    }
}
