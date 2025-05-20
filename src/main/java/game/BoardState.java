package game;

import java.util.ArrayList;
import java.util.List;

import utils.Utils;

/* Kelas yang merepresentasikan state papan pada permainan */
public class BoardState {
    private int row; 
    private int col; 
    private char[][] board; 
    private Coordinate exitCoordinate; // koordinat keluar
    private List<Piece> pieces; // list piece
    private Piece primaryPiece; // piece utama
    private Heuristic heuristic; // heuristik yang digunakan
    private int value; // nilai heuristik
    private Move lastMove; // langkah terakhir yang diambil
    private int depth; // depth node

    // Konstruktor
    public BoardState() {
        this.row = 0;
        this.col = 0;
        this.board = null;
        this.exitCoordinate = null;
        this.pieces = null;
        this.primaryPiece = null;
        this.heuristic = null;
        this.value = 0;
        this.lastMove = null;
        this.depth = 0;
    }
    public BoardState(int row, int col, char[][] board, List<Piece> pieces, Coordinate exitCoordinate, Piece primaryPiece, Heuristic heuristic, Move lastMove, int depth) {
        this.row = row;
        this.col = col;
        this.board = board;
        this.exitCoordinate = exitCoordinate;
        this.pieces = pieces;
        this.primaryPiece = primaryPiece;
        this.heuristic = heuristic;
        BoardState self = this;
        this.value = 0;
        if(this.heuristic != null) this.value = heuristic.calcValue(self);
        this.lastMove = lastMove;
        this.depth = depth;
    }

    // cek apakah state sudah mencapai tujuan
    public boolean isGoal() {
        return Utils.isPieceOnCoordinate(primaryPiece, exitCoordinate);
    }

    // membuat state baru dari state yang ada dengan menggerakkan sebuah piece
    public BoardState movePiece(Piece pieceToMove, Move.Direction direction) { // Renamed parameter for clarity
        if (!isValidMove(pieceToMove, direction)) {
            return null; // jika tidak valid, kembalikan null
        }

        // Create a deep copy of the pieces list
        List<Piece> newPiecesList = new ArrayList<>();
        Piece movedPieceInNewList = null;
        for (Piece p : this.pieces) {
            Piece copiedPiece = new Piece(p); 
            newPiecesList.add(copiedPiece);
            if (p.getName() == pieceToMove.getName()) {
                movedPieceInNewList = copiedPiece;
            }
        }

        if (movedPieceInNewList == null) {
            return null;
        }
        movedPieceInNewList.moveDirection(direction);

        Piece newPrimaryPiece = (this.primaryPiece.getName() == movedPieceInNewList.getName()) ? movedPieceInNewList : null;
        if (newPrimaryPiece == null) {
            for (Piece p : newPiecesList) {
                if (p.getName() == this.primaryPiece.getName()) {
                    newPrimaryPiece = p;
                    break;
                }
            }
        }

        char[][] newBoardArray = buildBoard(newPiecesList);

        return new BoardState(this.row, this.col, newBoardArray, newPiecesList, this.exitCoordinate, newPrimaryPiece, this.heuristic, new Move(movedPieceInNewList.getName(), direction, this), this.depth + 1);
    }

    // membuat board dari list of pieces
    public char[][] buildBoard(List<Piece> pieces) {
        char[][] newBoard = new char[board.length][board[0].length];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j] == 'K') {
                    newBoard[i][j] = 'K'; // Exit point
                }
            }
        }

        // exit coordinate di atas
        if (exitCoordinate.getRow() == 0 && exitCoordinate.getCol() < board[0].length-1) {
            for (int i = 1; i < row+1; i++) {
                for (int j = 0; j < col; j++) {
                    newBoard[i][j] = '.'; 
                }
            }
        } else if (exitCoordinate.getCol() == 0 && exitCoordinate.getRow() < board.length-1) { // exit coord di kiri
            for (int i = 0; i < row; i++) {
                for (int j = 1; j < col+1; j++) {
                    newBoard[i][j] = '.'; 
                }
            } 
        } else {
            for (int i = 0; i < row; i++) {
                for (int j = 0; j < col; j++) {
                    newBoard[i][j] = '.'; 
                }
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
        int trow = board.length;
        int tcol = board[0].length;

        for (Coordinate coordinate : piece.getCoordinates()) {
            int nextRow = coordinate.getRow();
            int nextCol = coordinate.getCol();

            switch (direction) {
                case UP:    nextRow--; break;
                case DOWN:  nextRow++; break;
                case LEFT:  nextCol--; break;
                case RIGHT: nextCol++; break;
                default: return false;
            }

            if (nextRow < 0 || nextRow >= trow || nextCol < 0 || nextCol >= tcol) {
                return false; 
            }

            char targetCell = board[nextRow][nextCol];

            if (targetCell == piece.getName()) { 
                continue;
            }

            if (piece.isPrimary()) {
                if (targetCell != '.' && targetCell != 'K') {
                    return false; 
                }
            } else {
                if (targetCell != '.') {
                    return false; 
                }
            }
        }
        return true; 
    }

    private boolean isBetween(int value, int bound1, int bound2) {
        return (bound1 < value && value < bound2) || (bound2 < value && value < bound1);
    }

    public boolean isPieceBlocking(Piece piece) {
        for (Coordinate coordinate : piece.getCoordinates()) {
            if(this.primaryPiece.isHorizontal()){
                // berarti blockingnya kan vertikal
                if(coordinate.getRow() == exitCoordinate.getRow()){
                    for(Coordinate coordinate2 : this.primaryPiece.getCoordinates()){
                        if(isBetween(coordinate.getCol(), coordinate2.getCol(), exitCoordinate.getCol())){
                            return true;
                        }
                    }
                }
            }
            else{
                // berarti blockingnya kan horizontal
                if(coordinate.getCol() == exitCoordinate.getCol()){
                    for(Coordinate coordinate2 : this.primaryPiece.getCoordinates()){
                        if(isBetween(coordinate.getRow(), coordinate2.getRow(), exitCoordinate.getRow())){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public List<BoardState> getPossibleMoves() {
        List<BoardState> possibleMoves = new ArrayList<>();
        for (Piece piece : this.pieces) { // Iterate through a stable list of pieces
            Move.Direction[] directionsToTry;
            if (piece.isHorizontal()) {
                directionsToTry = new Move.Direction[]{Move.Direction.LEFT, Move.Direction.RIGHT};
            } else {
                directionsToTry = new Move.Direction[]{Move.Direction.UP, Move.Direction.DOWN};
            }

            for (Move.Direction direction : directionsToTry) {
                BoardState nextState = movePiece(piece, direction);
                if (nextState != null) {
                    possibleMoves.add(nextState);
                }
            }
        }
        return possibleMoves;
    }

    public Move.Direction getDirectionToExit() {
        if (this.primaryPiece.isHorizontal()) {
            // antara kiri atau kanan
            if (this.exitCoordinate.getCol() == 0) { // di kiri
                return Move.Direction.LEFT;
            } else {
                return Move.Direction.RIGHT;
            }
        } else { // atas atau bawah
            if (this.exitCoordinate.getRow() == 0) { // di atas
                return Move.Direction.UP;
            } else {
                return Move.Direction.DOWN;
            }
        }
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
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
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
    public Heuristic getHeuristic() {
        return heuristic;
    }
    public int getValue() {
        return value;
    }
    public Move getLastMove() {
        return lastMove;
    }
    public int getDepth() {
        return depth;
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
    public void setDepth(int depth) {
        this.depth = depth;
    }
    public void setHeuristic(Heuristic heuristic) {
        this.heuristic = heuristic;
        this.value = heuristic.calcValue(this);
    }
    
}