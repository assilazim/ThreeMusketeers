package game;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.*;

import game.Piece.Type;

public class Board {
    public int size = 5;

    // 2D Array of Cells for representation of the game board
    public final game.Cell[][] board = new Cell[size][size];

    private Piece.Type turn;
    private Piece.Type winner;

    /**
     * Create a Board with the current player turn set.
     */
    public Board() {
        this.loadBoard("Boards/Starter.txt");
    }

    /**
     * Create a Board with the current player turn set and a specified board.
     * 
     * @param boardFilePath The path to the board file to import (e.g.
     *                      "Boards/Starter.txt")
     */
    public Board(String boardFilePath) {
        this.loadBoard(boardFilePath);
    }

    /**
     * Creates a Board copy of the given board.
     * 
     * @param board Board to copy
     */
    public Board(Board board) {
        this.size = board.size;
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                this.board[row][col] = new Cell(board.board[row][col]);
            }
        }
        this.turn = board.turn;
        this.winner = board.winner;
    }

    /**
     * @return the Piece.Type (Muskeeteer or Guard) of the current turn
     */
    public Piece.Type getTurn() {
        return turn;
    }

    /**
     * Get the cell located on the board at the given coordinate.
     * 
     * @param coordinate Coordinate to find the cell
     * @return Cell that is located at the given coordinate
     */
    public Cell getCell(Coordinate coordinate) { // TODO

        return this.board[coordinate.row][coordinate.col];

    }

    /**
     * @return the game winner Piece.Type (Muskeeteer or Guard) if there is one
     *         otherwise null
     */
    public Piece.Type getWinner() {
        return winner;
    }

    /**
     * Gets all the Musketeer cells on the board.
     * 
     * @return List of cells
     */
    public List<Cell> getMusketeerCells() { // TODO
        List<Cell> cells = new LinkedList<Cell>();
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (this.board[row][col].hasPiece()
                        && this.board[row][col].getPiece().getType().equals(Piece.Type.MUSKETEER)) {
                    cells.add(this.board[row][col]);
                }
            }
        }
        return cells;

    }

    /**
     * Gets all the Guard cells on the board.
     * 
     * @return List of cells
     */
    public List<Cell> getGuardCells() { // TODO
        List<Cell> cells = new LinkedList<Cell>();
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (this.board[row][col].hasPiece()
                        && this.board[row][col].getPiece().getType().equals(Piece.Type.GUARD)) {
                    cells.add(this.board[row][col]);
                }
            }
        }
        return cells;

    }

    /**
     * Executes the given move on the board and changes turns at the end of the
     * method.
     * 
     * @param move a valid move
     */
    public void move(Move move) { // TODO
        Cell FromCell = this.board[move.fromCell.getCoordinate().row][move.fromCell.getCoordinate().col];
        Cell ToCell = this.board[move.toCell.getCoordinate().row][move.toCell.getCoordinate().col];
        ToCell.setPiece(FromCell.getPiece());
        FromCell.setPiece(null);
        if (this.turn.equals(Type.MUSKETEER)) {
            this.turn = Type.GUARD;
        } else {
            this.turn = Type.MUSKETEER;
        }
    }

    /**
     * Undo the move given.
     * 
     * @param move Copy of a move that was done and needs to be undone. The move
     *             copy has the correct piece info in the
     *             from and to cell fields. Changes turns at the end of the method.
     */
    public void undoMove(Move move) { // TODO
        Cell FromCell = this.board[move.fromCell.getCoordinate().row][move.fromCell.getCoordinate().col];
        Cell ToCell = this.board[move.toCell.getCoordinate().row][move.toCell.getCoordinate().col];

        FromCell.setPiece(ToCell.getPiece());
        if (FromCell.getPiece().getType().equals(Piece.Type.MUSKETEER)) {
            ToCell.setPiece(new Guard());
        } else {
            ToCell.setPiece(null);
        }

        if (this.turn.equals(Type.MUSKETEER)) {
            this.turn = Type.GUARD;
        } else {
            this.turn = Type.MUSKETEER;
        }
    }

    /**
     * Checks if the given move is valid. Things to check:
     * (1) the toCell is next to the fromCell
     * (2) the fromCell piece can move onto the toCell piece.
     * 
     * @param move a move
     * @return True, if the move is valid, false otherwise
     */
    public Boolean isValidMove(Move move) { // TODO
        if (!move.fromCell.hasPiece()) {
            return false;
        }

        if (Math.abs(move.fromCell.getCoordinate().row - move.toCell.getCoordinate().row)
                + Math.abs(move.fromCell.getCoordinate().col - move.toCell.getCoordinate().col) != 1) {
            return false;
        }

        if (move.fromCell.getPiece().getType().equals(Piece.Type.MUSKETEER)) {
            if (move.toCell.hasPiece() && move.toCell.getPiece().getType().equals(Piece.Type.GUARD)) {
                return true;
            }
        }

        else if (!move.toCell.hasPiece()) {
            return true;
        }

        return false;
    }

    /**
     * Get all the possible cells that have pieces that can be moved this turn.
     * 
     * @return Cells that can be moved from the given cells
     */
    public List<Cell> getPossibleCells() { // TODO
        List<Cell> fromCells;
        if (this.turn == Type.MUSKETEER) {
            fromCells = this.getMusketeerCells();
        } else {
            fromCells = this.getGuardCells();
        }

        LinkedList<Cell> list = new LinkedList<Cell>();

        for (Cell cell : fromCells) {
            if (!this.getPossibleDestinations(cell).isEmpty()) {
                list.add(cell);
            }
        }

        return list;
    }

    /**
     * Get all the possible cell destinations that is possible to move to from the
     * fromCell.
     * 
     * @param fromCell The cell that has the piece that is going to be moved
     * @return List of cells that are possible to get to
     */
    public List<Cell> getPossibleDestinations(Cell fromCell) { // TODO
        LinkedList<Cell> list = new LinkedList<Cell>();
        // left position
        if (fromCell.getCoordinate().col - 1 >= 0) {
            Cell toCell = this.board[fromCell.getCoordinate().row][fromCell.getCoordinate().col - 1];

            if (this.isValidMove(new Move(fromCell, toCell)))
                list.add(toCell);
        }
        // right position
        if (fromCell.getCoordinate().col + 1 < size) {
            Cell toCell = this.board[fromCell.getCoordinate().row][fromCell.getCoordinate().col + 1];

            if (this.isValidMove(new Move(fromCell, toCell)))
                list.add(toCell);
        }

        // down position
        if (fromCell.getCoordinate().row - 1 >= 0) {
            Cell toCell = this.board[fromCell.getCoordinate().row - 1][fromCell.getCoordinate().col];

            if (this.isValidMove(new Move(fromCell, toCell))) {
                list.add(toCell);
            }
        }

        // up position
        if (fromCell.getCoordinate().row + 1 < size) {
            Cell toCell = this.board[fromCell.getCoordinate().row + 1][fromCell.getCoordinate().col];

            if (this.isValidMove(new Move(fromCell, toCell))) {
                list.add(toCell);
            }
        }
        return list;
    }

    /**
     * Get all the possible moves that can be made this turn.
     * 
     * @return List of moves that can be made this turn
     */
    public List<Move> getPossibleMoves() { // TODO
        List<Cell> list;
        if (this.turn == Type.MUSKETEER) {
            list = this.getMusketeerCells();
        } else {
            list = this.getGuardCells();
        }

        LinkedList<Move> toCells = new LinkedList<Move>();

        for (Cell possibleFromCell : list) {
            for (Cell ToCell : this.getPossibleDestinations(possibleFromCell)) {
                toCells.add(new Move(possibleFromCell, ToCell));
            }
        }
        return toCells;
    }

    /**
     * Checks if the game is over and sets the winner if there is one.
     * 
     * @return True, if the game is over, false otherwise.
     */
    public boolean isGameOver() { // TODO
        List<Cell> cells = this.getMusketeerCells();
        if (((cells.get(0).getCoordinate().row == cells.get(1).getCoordinate().row)
                && (cells.get(1).getCoordinate().row == cells.get(2).getCoordinate().row)) ||
                (cells.get(0).getCoordinate().col == cells.get(1).getCoordinate().col)
                        && (cells.get(1).getCoordinate().col == cells.get(2).getCoordinate().col)) {
            this.winner = Type.GUARD;
            return true;
        } else if (this.getPossibleMoves().isEmpty()) {
            this.winner = Type.MUSKETEER;
            return true;
        }
        return false;
    }

    /**
     * Saves the current board state to the boards directory
     */
    public void saveBoard() {
        String filePath = String.format("boards/%s.txt",
                new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()));
        File file = new File(filePath);

        try {
            file.createNewFile();
            Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
            writer.write(turn.getType() + "\n");
            for (Cell[] row : board) {
                StringBuilder line = new StringBuilder();
                for (Cell cell : row) {
                    if (cell.getPiece() != null) {
                        line.append(cell.getPiece().getSymbol());
                    } else {
                        line.append("_");
                    }
                    line.append(" ");
                }
                writer.write(line.toString().strip() + "\n");
            }
            writer.close();
            System.out.printf("Saved board to %s.\n", filePath);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.printf("Failed to save board to %s.\n", filePath);
        }
    }

    @Override
    public String toString() {
        StringBuilder boardStr = new StringBuilder("  | A B C D E\n");
        boardStr.append("--+----------\n");
        for (int i = 0; i < size; i++) {
            boardStr.append(i + 1).append(" | ");
            for (int j = 0; j < size; j++) {
                Cell cell = board[i][j];
                boardStr.append(cell).append(" ");
            }
            boardStr.append("\n");
        }
        return boardStr.toString();
    }

    /**
     * Loads a board file from a file path.
     * 
     * @param filePath The path to the board file to load (e.g.
     *                 "Boards/Starter.txt")
     */
    private void loadBoard(String filePath) {
        File file = new File(filePath);
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            System.err.printf("File at %s not found.", filePath);
            System.exit(1);
        }

        turn = Piece.Type.valueOf(scanner.nextLine().toUpperCase());

        int row = 0, col = 0;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] pieces = line.trim().split(" ");
            for (String piece : pieces) {
                Cell cell = new Cell(new Coordinate(row, col));
                switch (piece) {
                    case "O" -> cell.setPiece(new Guard());
                    case "X" -> cell.setPiece(new Musketeer());
                    default -> cell.setPiece(null);
                }
                this.board[row][col] = cell;
                col += 1;
            }
            col = 0;
            row += 1;
        }
        scanner.close();
        System.out.printf("Loaded board from %s.\n", filePath);
    }
}