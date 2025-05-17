package Utils;

import Game.Coordinate;
import Game.Piece;
import java.util.List;

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
}
