package game;

import java.util.*;

import game.Piece.Type;



public class HumanAgent extends Agent {

    public HumanAgent(Board board) {
        super(board);
    }

    /**
     * Asks the human for a move with from and to coordinates and makes sure its valid.
     * Create a Move object with the chosen fromCell and toCell
     * @return the valid human inputted Move
     */
    //String turn = "[" + this.board.getTurn().toString() + "] ";
    @Override
    public Move getMove() { // TODO
        // assumed that input format is valid and coordinates valid!!!!
        Scanner scanner = new Scanner(System.in);
   
        Move move;
        Cell fromCell = null;
        String turn = "[" + this.board.getTurn().toString() + "] ";

        while (true) {
            String s = turn + "Possible pieces are";
            List<Cell> froms = this.board.getPossibleCells();
            for (Cell cell : froms)
                s += " [" + cell.getCoordinate().toString() + "]";
            s += ". Enter the peice you want to move: ";
            System.out.print(s);

            String input = scanner.nextLine();
            Coordinate coo = null;
            Boolean ok = true;
            try {
            	if (input.charAt(0) > 96) {
            		coo = new Coordinate(Integer.parseInt(String.valueOf(input.charAt(1))) - 1,
                            input.charAt(0) - 97);
            	}
            	else {
            		coo = new Coordinate(Integer.parseInt(String.valueOf(input.charAt(1))) - 1,
                            input.charAt(0) - 65);
            	}
                
            } catch (Exception e) {
                System.out.println(input + " is an invalid coordinate");
                ok = false;
            }
            if (ok && (coo.row < 0 || coo.row >= 5 || coo.col < 0 || coo.col >= 5)) {
                System.out.println(input + " is an invalid coordinate");
                ok = false;
            }

            if (ok) {
                for (Cell cell : froms)
                    if (coo.row == cell.getCoordinate().row && coo.col == cell.getCoordinate().col) {
                        fromCell = cell;
                        break;
                    }
                if (fromCell != null)
                    break;
                System.out.println(input + " is an invalid possible piece.");
            }
        }

        Cell ToCell = null;
        while (true) {
            String s = turn + "Possible destinations are";
            List<Cell> to = this.board.getPossibleDestinations(fromCell);
            for (Cell cell : to)
                s += " [" + cell.getCoordinate().toString() + "]";
            s += ". Enter the destination: ";
            System.out.print(s);

            String input = scanner.nextLine();
            Coordinate coo = null;
            Boolean ok = true;
            try {
            	if (input.charAt(0) > 96) {
            		coo = new Coordinate(Integer.parseInt(String.valueOf(input.charAt(1))) - 1,
                            input.charAt(0) - 97);
            	}
            	else {
            		coo = new Coordinate(Integer.parseInt(String.valueOf(input.charAt(1))) - 1,
                            input.charAt(0) - 65);
            	}
                
            } catch (Exception e) {
                System.out.println(input + " is an invalid coordinate");
                ok = false;
            }
            if (ok && (coo.row < 0 || coo.row >= 5 || coo.col < 0 || coo.col >= 5)) {
                System.out.println(input + " is an invalid coordinate");
                ok = false;
            }

            if (ok) {
                for (Cell cell : to)
                    if (coo.row == cell.getCoordinate().row && coo.col == cell.getCoordinate().col) {
                        ToCell = cell;
                        break;
                    }
                if (ToCell != null)
                    break;
                System.out.println(input + " is an invalid destination.");
            }
        }

        return new Move(fromCell, ToCell);

    }
}
