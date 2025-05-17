package Game;

import java.util.List;

/* Kelas Piece yang merepresentasikan sebuah piece */
public class Piece {

    // Atribut
    private char name;
    private List<Coordinate> coordinates; // list koordinat dari piece
    private boolean isPrimary; // flag untuk primary piece
    private boolean isHorizontal; // flag untuk horizontal piece

    // Methods
    public Piece(char name, List<Coordinate> coordinates, boolean isPrimary, boolean isHorizontal) {
        this.name = name;
        this.coordinates = coordinates;
        this.isPrimary = isPrimary;
        this.isHorizontal = isHorizontal;
    }

    // Getter
    public char getName() {
        return name;
    }
    public List<Coordinate> getCoordinates() {
        return coordinates;
    }
    public boolean isPrimary() {
        return isPrimary;
    }
    public boolean isHorizontal() {
        return isHorizontal;
    }

    // Setter
    public void setName(char name) {
        this.name = name;
    }
    public void setCoordinates(List<Coordinate> coordinates) {
        this.coordinates = coordinates;
    }
    public void setPrimary(boolean isPrimary) {
        this.isPrimary = isPrimary;
    }
    public void setHorizontal(boolean isHorizontal) {
        this.isHorizontal = isHorizontal;
    }

    // Method untuk mengubah posisi piece
    public void shift(int rowShift, int colShift) {
        for (Coordinate coordinate : coordinates) {
            coordinate.shift(rowShift, colShift);
        }
    }
    public void move(Coordinate shift) {
        for (Coordinate coordinate : coordinates) {
            coordinate.moveTo(shift);
        }
    }
}
