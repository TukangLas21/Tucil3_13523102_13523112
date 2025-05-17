import java.util.List;

public class Papan {
    /* Kelas yang merepresentasikan papan pada permainan */

    /* Atribut */
    // baris dan kolom total, termasuk koordinat keluar
    private int totalRow; 
    private int totalCol; 
    // baris dan kolom efektif untuk permainan
    private int effRow;
    private int effCol;

    private List<Piece> pieces; 
    private Coordinate exitCoordinate; // list koordinat keluar
    private int[][] borad; // papan permainan

    /* Methods */
    // Constructor
    public Papan(int row, int col, List<Piece> pieces, Coordinate exitCoordinate) {
        this.totalRow = row + 2; // total baris
        this.totalCol = col + 2; // total kolom
        this.effRow = row; // baris efektif
        this.effCol = col; // kolom efektif
        this.pieces = pieces;
        this.exitCoordinate = exitCoordinate;
        this.borad = new int[totalRow][totalCol]; // inisialisasi papan
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
    public int[][] getBorad() {
        return borad;
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
    public void setBorad(int[][] borad) {
        this.borad = borad;
    }
}