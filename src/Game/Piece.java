import java.util.List;

public class Piece {
    /* Kelas Piece yang merepresentasikan sebuah piece */

    // Atribut
    private String name;
    private List<Coordinate> coordinates; // list koordinat dari piece
    private boolean isPrimary; // flag untuk primary piece

    // Methods
    Piece(String name, List<Coordinate> coordinates, boolean isPrimary) {
        this.name = name;
        this.coordinates = coordinates;
        this.isPrimary = isPrimary;
    }

    // Getter
    public String getName() {
        return name;
    }
    public List<Coordinate> getCoordinates() {
        return coordinates;
    }
    public boolean isPrimary() {
        return isPrimary;
    }

    // Setter
    public void setName(String name) {
        this.name = name;
    }
    public void setCoordinates(List<Coordinate> coordinates) {
        this.coordinates = coordinates;
    }
    public void setPrimary(boolean isPrimary) {
        this.isPrimary = isPrimary;
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
