package Game;

/* Kelass Coordinate yang merepresentasikan suatu koordinat pada papan */
public class Coordinate {
    private int row; // posisi baris
    private int col; // posisi kolom

    // Constructor
    public Coordinate(int row, int col) {
        this.row = row;
        this.col = col;
    }

    // Getter
    public int getRow() {
        return row;
    }
    public int getCol() {
        return col;
    }

    // Setter
    public void setRow(int row) {
        this.row = row;
    }
    public void setCol(int col) {
        this.col = col;
    }

    // Method untuk mengubah posisi koordinat
    public void shift(int rowShift, int colShift) {
        this.row += rowShift;
        this.col += colShift;
    }
    public void moveTo(Coordinate shift) {
        this.row = shift.getRow();
        this.col = shift.getCol();
    }
    public void shiftRow(int rowShift) {
        this.row += rowShift;
    }
    public void shiftCol(int colShift) {
        this.col += colShift;
    }

}
