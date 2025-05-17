package Utils;

import Game.Papan;
import Game.Piece;
import Game.Coordinate;
import java.io.File;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

/*  */
public class IOHandler {
    private static final Scanner scanner = new Scanner(System.in);

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

    public static void convertInput(File file, Papan papan) {
        // TODO: Implementasi konversi input file ke dalam format yang sesuai
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
            String[][] tempBoard = new String[row+1][col+1];
            int tempRow = 0;
            while (line != null) {
                String[] linePieces = line.split("");
                for (int i = 0; i < linePieces.length; i++) {
                    if (linePieces[i].equals(" ")) {
                        tempBoard[tempRow][i] = "."; // mengisi dengan . untuk posisi kosong
                    } else {
                        tempBoard[tempRow][i] = linePieces[i];
                    }
                }
                tempRow++;
                line = reader.readLine();
            }

        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    public static List<Piece> convertPieces(String[][] newBoard) {
        List<Piece> pieces = new ArrayList<>();
        
        return pieces;
    }

    public static Coordinate convertExitCoordinate(String[][] fileBoard) {
        Coordinate exitCoordinate = null;
        int exitSide = getExitSide(fileBoard);
        for (int i = 0; i < fileBoard.length; i++) {
            for (int j = 0; j < fileBoard[i].length; j++) {
                if (fileBoard[i][j].equals("K")) {
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

    public static int getExitSide(String[][] fileBoard) {
        for (int i = 0; i < fileBoard.length; i++) {
            for (int j = 0; j < fileBoard[i].length; j++) {
                if (fileBoard[i][j].equals("K")) {
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

    public static String[][] trimBoard(String[][] fileBoard, int exitSide) {
        String[][] newBoard = new String[fileBoard.length - 1][fileBoard[0].length - 1];
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
