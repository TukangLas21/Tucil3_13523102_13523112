package Game;

import java.util.List;

/* Kelas yang merepresentasikan papan pada permainan */
public class Papan {

    /* Atribut */
    // baris dan kolom total, termasuk koordinat keluar
    private int totalRow; 
    private int totalCol; 
    // baris dan kolom efektif untuk permainan
    private int effRow;
    private int effCol;

    private List<Piece> pieces; 
    private Coordinate exitCoordinate; // list koordinat keluar
    private char[][] board; // papan permainan

    /* Methods */
    // Constructor
    public Papan() {
        this.totalRow = 0;
        this.totalCol = 0;
        this.effRow = 0;
        this.effCol = 0;
        this.pieces = null;
        this.exitCoordinate = null;
        this.board = null;
    }

    public Papan(int row, int col, List<Piece> pieces, Coordinate exitCoordinate) {
        this.totalRow = row + 2; // total baris
        this.totalCol = col + 2; // total kolom
        this.effRow = row; // baris efektif
        this.effCol = col; // kolom efektif
        this.pieces = pieces;
        this.exitCoordinate = exitCoordinate;
        this.board = new char[totalRow][totalCol]; // inisialisasi papan
    }

    // Getter
    public int getTotalRow() {
        return totalRow;
    }
    public int getTotalCol() {
        return totalCol;
    }
    public int getEffRow() {
        return effRow;
    }
    public int getEffCol() {
        return effCol;
    }
    public List<Piece> getPieces() {
        return pieces;
    }
    public Coordinate getExitCoordinate() {
        return exitCoordinate;
    }
    public char[][] getBoard() {
        return board;
    }

    // Setter
    public void setTotalRow(int totalRow) {
        this.totalRow = totalRow;
    }
    public void setTotalCol(int totalCol) {
        this.totalCol = totalCol;
    }
    public void setEffRow(int effRow) {
        this.effRow = effRow;
    }
    public void setEffCol(int effCol) {
        this.effCol = effCol;
    }
    public void setPieces(List<Piece> pieces) {
        this.pieces = pieces;
    }
    public void setExitCoordinate(Coordinate exitCoordinate) {
        this.exitCoordinate = exitCoordinate;
    }
    public void setBoard(char[][] board) {
        this.board = board;
    }
}