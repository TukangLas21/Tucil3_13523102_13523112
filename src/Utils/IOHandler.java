package Utils;

import Game.Coordinate;
import Game.Papan;
import Game.Piece;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/* Kelas statik untuk handle input dan output */
public class IOHandler {
    private static final Scanner scanner = new Scanner(System.in);

    // TODO: Validasi input

    // baca file path
    public static File readInputFile() {
        System.out.println("Masukkan nama file input (pastikan berada di dalam folder test/problem): ");
        String fileName = scanner.nextLine();
        File file = new File("test/problem/" + fileName);

        while (!file.exists()) {
            System.out.println("File tidak ditemukan. Silakan coba lagi: ");
            fileName = scanner.nextLine();
            file = new File("test/problem/" + fileName);
        }

        return file;
    }   

    // konversi input file ke dalam papan
    public static void convertInput(File file, Papan papan) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;

            // membaca baris pertama untuk mendapatkan ukuran papan 
            line = reader.readLine();
            String[] boardConfig = line.split(" ");
            int row = Integer.parseInt(boardConfig[0]);
            int col = Integer.parseInt(boardConfig[1]);

            // membaca jumlah piece (kecuali piece keluar)
            line = reader.readLine();
            String pieceNum = line.trim();
            int numPieces = Integer.parseInt(pieceNum);

            line = reader.readLine();

            // membaca konfigurasi papan
            char[][] tempBoard = new char[row+1][col+1];
            int tempRow = 0;
            while (line != null) {
                String[] linePieces = line.split("");
                for (int i = 0; i < linePieces.length; i++) {
                    if (linePieces[i].equals(" ")) {
                        tempBoard[tempRow][i] = '.'; // mengisi dengan . untuk posisi kosong
                    } else {
                        tempBoard[tempRow][i] = linePieces[i].charAt(0); // mengisi dengan karakter asli
                    }
                }
                tempRow++;
                line = reader.readLine();
            }

            // get exit coordinate
            Coordinate exitCoordinate = convertExitCoordinate(tempBoard);

            // get exit side
            int exitSide = getExitSide(tempBoard);

            // trim board
            char[][] newBoard = trimBoard(tempBoard, exitSide);

            // convert pieces
            List<Piece> pieces = convertPieces(newBoard, numPieces);

            // isi Papan
            papan.setTotalRow(row+2);
            papan.setTotalCol(col+2);
            papan.setEffRow(row);
            papan.setEffCol(col);
            papan.setPieces(pieces);
            papan.setExitCoordinate(exitCoordinate);
            papan.setBoard(Utils.buildNewBoard(newBoard));
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    // dapatkan list pieces
    // TODO: optimize
    public static List<Piece> convertPieces(char[][] newBoard, int numPieces) {
        List<Piece> pieces = new ArrayList<>();
        int totPiece = numPieces + 1; // tambah 1 untuk primary piece
        int countPiece = 0;

        // daftar semua piece ke dalam list
        while (countPiece < totPiece) {
            // traversal papan, jika piece belum ada di list, tambahkan
            for (int i = 0; i < newBoard.length; i++) {
                for (int j = 0; j < newBoard[i].length; j++) {
                    if (newBoard[i][j] != '.' && !Utils.isPieceInList(newBoard[i][j], pieces)) {
                        // buat list koordinat piece
                        List<Coordinate> coordinates = new ArrayList<>();
                        coordinates.add(new Coordinate(i+1, j+1));

                        // cari semua koordinat piece yang sama
                        for (int k = 0; k < newBoard.length; k++) {
                            for (int l = 0; l < newBoard[k].length; l++) {
                                if (newBoard[k][l] == newBoard[i][j] && !(k == i && l == j)) {
                                    coordinates.add(new Coordinate(k+1, l+1));
                                }
                            }
                        }

                        // buat piece baru dan tambahkan ke list
                        Piece piece = new Piece(newBoard[i][j], coordinates, newBoard[i][j] == 'P', Utils.isPieceHorizontal(coordinates));
                        pieces.add(piece);
                        countPiece++;
                    }
                }
            }
        }
        return pieces;
    }

    // dapatkan koordinat titik keluar papan
    public static Coordinate convertExitCoordinate(char[][] fileBoard) {
        Coordinate exitCoordinate = null;
        int exitSide = getExitSide(fileBoard);
        for (int i = 0; i < fileBoard.length; i++) {
            for (int j = 0; j < fileBoard[i].length; j++) {
                if (fileBoard[i][j] == 'K') {
                    switch (exitSide) {
                        case 1 -> exitCoordinate = new Coordinate(i, j+1); // atas
                        case 2 -> exitCoordinate = new Coordinate(i+1, j+1); // bawah
                        case 3 -> exitCoordinate = new Coordinate(i+1, j); // kiri
                        case 4 -> exitCoordinate = new Coordinate(i+1, j+1); // kanan
                    }
                }
            }
        }
        return exitCoordinate;
    }

    // dapatkan sisi keluar dari papan
    public static int getExitSide(char[][] fileBoard) {
        for (int i = 0; i < fileBoard.length; i++) {
            for (int j = 0; j < fileBoard[i].length; j++) {
                if (fileBoard[i][j] == 'K') {
                    if (i == 0) {
                        return 1; // atas
                    } else if (i == fileBoard.length - 1) {
                        return 2; // bawah
                    } else if (j == 0) {
                        return 3; // kiri
                    } else if (j == fileBoard[i].length - 1) {
                        return 4; // kanan
                    }
                }
            }
        }
        return 0; // tidak ditemukan
    }

    // trim papan sesuai dengan sisi keluar
    public static char[][] trimBoard(char[][] fileBoard, int exitSide) {
        char[][] newBoard = new char[fileBoard.length - 1][fileBoard[0].length - 1];
        switch (exitSide) {
            case 1 -> {
                // trim row atas
                for (int i = 1; i < fileBoard.length; i++) {
                    System.arraycopy(fileBoard[i], 0, newBoard[i - 1], 0, fileBoard[i].length);
                }
                break;
            } 
            case 2 -> {
                // trim row bawah
                for (int i = 0; i < fileBoard.length - 1; i++) {
                    System.arraycopy(fileBoard[i], 0, newBoard[i], 0, fileBoard[i].length);
                }
                break;
            }
            case 3 -> {
                // trim kolom kiri
                for (int i = 0; i < fileBoard.length; i++) {
                    System.arraycopy(fileBoard[i], 1, newBoard[i], 0, fileBoard[i].length - 1);
                }
                break;
            }
            case 4 -> {
                // trim kolom kanan
                for (int i = 0; i < fileBoard.length; i++) {
                    System.arraycopy(fileBoard[i], 0, newBoard[i], 0, fileBoard[i].length - 1);
                }
                break;
            }
        }
        return newBoard;
    }

    // input untuk algoritma
    public static int inputAlgorithm() {
        System.out.println("Pilih algoritma yang ingin digunakan: ");
        System.out.println("1. Greedy Best First Search");
        System.out.println("2. Uniformed Cost Search");
        System.out.println("3. A*");
        System.out.print("Pilihan Anda: ");
        int choice = scanner.nextInt();

        while (choice < 1 || choice > 3) {
            System.out.print("Pilihan tidak valid. Silakan coba lagi: ");
            choice = scanner.nextInt();
        }

        return choice;
    }
}
