package utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import game.Coordinate;
import game.Piece;
import javafx.scene.paint.Color;

/* Kelas untuk fungsi-fungsi utilitas */
public class Utils {
    // mengecek apakah nama piece sudah ada di dalam list
    public static boolean isPieceInList(char name, List<Piece> pieces) {
        for (Piece piece : pieces) {
            if (piece.getName() == name) {
                return true;
            }
        }
        return false;
    }

    // mengecek apakah piece horizontal berdasarkan list koordinatnya
    public static boolean isPieceHorizontal(List<Coordinate> coordinates) {
        // jika semua koordinat memiliki baris yang sama, maka piece horizontal
        for (int i = 1; i < coordinates.size(); i++) {
            if (coordinates.get(i).getRow() != coordinates.get(0).getRow()) {
                return false;
            }
        }
        return true;
    }

    // membentuk board baru dengan menambahkan 2 baris dan 2 kolom kosong
    public static char[][] buildNewBoard(char[][] board) {
        char[][] newBoard = new char[board.length+2][board[0].length+2];

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                newBoard[i+1][j+1] = board[i][j];
            }
        }

        return newBoard;
    }

    // mengecek apakah piece menempati koordinat
    public static boolean isPieceOnCoordinate(Piece piece, Coordinate coordinate) {
        for (Coordinate coord : piece.getCoordinates()) {
            if (coord.equals(coordinate)) {
                return true;
            }
        }
        return false;
    }

    // Map berisi pasangan huruf dengan warna untuk image
    public static final Map<Character, Color> imageColorMap = new HashMap<>();
    static {
        imageColorMap.put('K', Color.rgb(0, 255, 102));
        imageColorMap.put('P', Color.rgb(204, 0, 0));
        imageColorMap.put('.', Color.rgb(96, 96, 96));  
        imageColorMap.put(' ', Color.WHITE); // warna putih untuk spasi
    }
}
