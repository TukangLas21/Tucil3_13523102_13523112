package game;

import java.util.List;
import java.util.ArrayList;

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

    public Piece(Piece other) {
        this.name = other.name;
        this.coordinates = new ArrayList<>();
        for (Coordinate coord : other.coordinates) {
            this.coordinates.add(new Coordinate(coord));
        }
        this.isPrimary = other.isPrimary;
        this.isHorizontal = other.isHorizontal;
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

    public int getWidth() {
        int minCol = Integer.MAX_VALUE;
        int maxCol = Integer.MIN_VALUE;
        for (Coordinate coordinate : coordinates) {
            if (coordinate.getCol() < minCol) {
                minCol = coordinate.getCol();
            }
            if (coordinate.getCol() > maxCol) {
                maxCol = coordinate.getCol();
            }
        }
        return maxCol - minCol + 1;
    }

    public int getHeight() {
        int minRow = Integer.MAX_VALUE;
        int maxRow = Integer.MIN_VALUE;
        for (Coordinate coordinate : coordinates) {
            if (coordinate.getRow() < minRow) {
                minRow = coordinate.getRow();
            }
            if (coordinate.getRow() > maxRow) {
                maxRow = coordinate.getRow();
            }
        }
        return maxRow - minRow + 1;
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
    public void moveDirection(Move.Direction direction) {
        for (Coordinate coordinate : coordinates) {
            switch (direction) {
                case UP -> coordinate.shiftRow(-1);
                case DOWN -> coordinate.shiftRow(1);
                case LEFT -> coordinate.shiftCol(-1);
                case RIGHT -> coordinate.shiftCol(1);
            }
        }
    }

    public void removeCoordinateAtCoordinate(Coordinate coordinate) {
        coordinates.removeIf(coord -> coord.equals(coordinate));
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Piece ").append(name).append(": ");
        for (Coordinate coordinate : coordinates) {
            sb.append("(").append(coordinate.getRow()).append(", ").append(coordinate.getCol()).append(") ");
        }
        return sb.toString();
    }
}
