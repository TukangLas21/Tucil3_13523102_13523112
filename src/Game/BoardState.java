package Game;

import Utils.Utils;
import java.util.ArrayList;
import java.util.List;

/* Kelas yang merepresentasikan state papan pada permainan */
public class BoardState {
    private int row; 
    private int col; 
    private char[][] board; 
    private Coordinate exitCoordinate; // koordinat keluar
    private List<Piece> pieces; // list piece
    private Piece primaryPiece; // piece utama
    private int value = calcValue(); // nilai heuristik
    private Move lastMove; // langkah terakhir yang diambil

    // Konstruktor
    public BoardState() {
        this.row = 0;
        this.col = 0;
        this.board = null;
        this.exitCoordinate = null;
        this.pieces = null;
        this.primaryPiece = null;
        this.lastMove = null;
    }
    public BoardState(int row, int col, char[][] board, List<Piece> pieces, Coordinate exitCoordinate, Piece primaryPiece, Move lastMove) {
        this.row = row;
        this.col = col;
        this.board = board;
        this.exitCoordinate = exitCoordinate;
        this.pieces = pieces;
        this.primaryPiece = primaryPiece;
        this.lastMove = lastMove;
    }

    
    // perhitungan nilai heuristik berdasarkan jarak piece utama ke koordinat keluar
    private int calcValue() {
        if (primaryPiece.isHorizontal()) { // jarak horizontal
            return Math.abs(primaryPiece.getCoordinates().get(0).getCol() - exitCoordinate.getCol());
        } else { // jarak vertikal
            return Math.abs(primaryPiece.getCoordinates().get(0).getRow() - exitCoordinate.getRow());
        }
    }

    // cek apakah state sudah mencapai tujuan
    public boolean isGoal() {
        return Utils.isPieceOnCoordinate(primaryPiece, exitCoordinate);
    }

    // membuat state baru dari state yang ada dengan menggerakkan sebuah piece
    public BoardState movePiece(Piece piece, Move.Direction direction) {
        if (!isValidMove(piece, direction)) {
            return null; // jika tidak valid, kembalikan null
        }

        // ubah posisi piece pada list pieces
        for (Piece p : this.pieces) {
            if (p.getName() == piece.getName()) {
                p.moveDirection(direction);
                break;
            }
        }

        char[][] newBoard = buildBoard(this.pieces);

        return new BoardState(this.row, this.col, newBoard, this.pieces, this.exitCoordinate, this.primaryPiece, new Move(piece.getName(), direction, this));
    }

    // membuat board dari list of pieces
    public char[][] buildBoard(List<Piece> pieces) {
        char[][] newBoard = new char[row][col];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                newBoard[i][j] = '.';
            }
        }

        for (Piece piece : pieces) {
            for (Coordinate coordinate : piece.getCoordinates()) {
                newBoard[coordinate.getRow()][coordinate.getCol()] = piece.getName();
            }
        }

        return newBoard;
    }

    // cek apakah piece bisa bergerak ke arah yang diinginkan
    public boolean isValidMove(Piece piece, Move.Direction direction) {
        switch (direction) {
            case UP -> {
                for (Coordinate coordinate : piece.getCoordinates()) {
                    if (coordinate.getRow() == 0 || (board[coordinate.getRow()-1][coordinate.getCol()] != '.' && board[coordinate.getRow()-1][coordinate.getCol()] != piece.getName())) {
                        return false;
                    }
                }
                return true;
            }
            case DOWN -> {
                for (Coordinate coordinate : piece.getCoordinates()) {
                    if (coordinate.getRow() == row-1 || (board[coordinate.getRow()+1][coordinate.getCol()] != '.' && board[coordinate.getRow()+1][coordinate.getCol()] != piece.getName())) {
                        return false;
                    }
                }
                return true;
            }
            case LEFT -> {
                for (Coordinate coordinate : piece.getCoordinates()) {
                    if (coordinate.getCol() == 0 || (board[coordinate.getRow()][coordinate.getCol()-1] != '.' && board[coordinate.getRow()][coordinate.getCol()-1] != piece.getName())) {
                        return false;
                    }
                }
                return true;
            }
            case RIGHT -> {
                for (Coordinate coordinate : piece.getCoordinates()) {
                    if (coordinate.getCol() == col-1 || (board[coordinate.getRow()][coordinate.getCol()+1] != '.' && board[coordinate.getRow()][coordinate.getCol()+1] != piece.getName())) {
                        return false;
                    }
                }
                return true;
            }
            default -> {
                return false;
            }
        }
    }

    public List<BoardState> getPossibleMoves() {
        List<BoardState> possibleMoves = new ArrayList<>();
        for (Piece piece : pieces) {
            if (piece.isHorizontal()) {
                for (Move.Direction direction : new Move.Direction[]{Move.Direction.LEFT, Move.Direction.RIGHT}) {
                    BoardState newState = movePiece(piece, direction);
                    if (newState != null) {
                        possibleMoves.add(newState);
                    }
                }
            } else {
                for (Move.Direction direction : new Move.Direction[]{Move.Direction.UP, Move.Direction.DOWN}) {
                    BoardState newState = movePiece(piece, direction);
                    if (newState != null) {
                        possibleMoves.add(newState);
                    }
                }
            }
        }
        return possibleMoves;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof BoardState)) return false;
        BoardState other = (BoardState) obj;
        if (this.row != other.row || this.col != other.col) return false;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (this.board[i][j] != other.board[i][j]) return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + row;
        result = 31 * result + col;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                result = 31 * result + board[i][j];
            }
        }
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                sb.append(board[i][j]);
            }
        }
        return sb.toString();
    }

    // Getter
    public int getRow() {
        return row;
    }
    public int getCol() {
        return col;
    }
    public char[][] getBoard() {
        return board;
    }
    public Coordinate getExitCoordinate() {
        return exitCoordinate;
    }
    public List<Piece> getPieces() {
        return pieces;
    }
    public Piece getPrimaryPiece() {
        return primaryPiece;
    }
    public int getValue() {
        return value;
    }
    public Move getLastMove() {
        return lastMove;
    }

    // Setter
    public void setRow(int row) {
        this.row = row;
    }
    public void setCol(int col) {
        this.col = col;
    }
    public void setBoard(char[][] board) {
        this.board = board;
    }
    public void setExitCoordinate(Coordinate exitCoordinate) {
        this.exitCoordinate = exitCoordinate;
    }
    public void setPieces(List<Piece> pieces) {
        this.pieces = pieces;
    }
    public void setPrimaryPiece(Piece primaryPiece) {
        this.primaryPiece = primaryPiece;
    }
    public void setValue(int value) {
        this.value = value;
    }
    public void setLastMove(Move lastMove) {
        this.lastMove = lastMove;
    }
    
}