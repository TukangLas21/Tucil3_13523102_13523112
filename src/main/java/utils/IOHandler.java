package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import game.BoardState;
import game.Coordinate;
import game.Move;
import game.Piece;

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
    public static BoardState convertInput(File file) {
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
                    if (i >= linePieces.length) {
                        throw new IOException("kolom kelebihan le");
                    }
                    tempBoard[tempRow][i] = linePieces[i].charAt(0);
                }
                tempRow++;
                if (tempRow >= tempBoard.length) {
                    throw new IOException("baris kelebihan le");
                }
                line = reader.readLine();
            }

            // System.out.println("Getting exit coordinates");
            // get exit coordinate
            Coordinate exitCoordinate = getExitCoordinate(tempBoard);
            if (exitCoordinate == null) {
                throw new IOException("Koordinat keluar tidak ditemukan");
            }

            // System.out.println("Getting exit direction");
            // get exit side
            Move.Direction exitSide = getExitSide(tempBoard);

            // System.out.println("Trimming board");
            // trim board
            char[][] newBoard = trimBoard(tempBoard, exitSide);
            if (newBoard == null) {
                throw new IOException("Gagal memotong papan");
            }

            // System.out.println("Converting pieces");
            // convert pieces
            List<Piece> pieces = convertPieces(newBoard, numPieces);

            // System.out.println("Finished converting pieces");

            Piece primaryPiece = getPrimaryPiece(pieces);

            for (Piece piece : pieces) {
                System.out.println(piece);
            }

            for (int i = 0; i < newBoard.length; i++) {
                for (int j = 0; j < newBoard[i].length; j++) {
                    System.out.print(newBoard[i][j]);
                }
                System.out.println();
            }
            // buat BoardState dengan papan lengkap dari awal
            return new BoardState(row, col, newBoard, pieces, exitCoordinate, primaryPiece, null, null, 0);
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
            return null;
        }
    }

    // dapatkan list pieces
    public static List<Piece> convertPieces(char[][] board, int numPieces) {
        List<Piece> pieces = new ArrayList<>();
        Set<Character> processedPieces = new HashSet<>();
        int targetCount = numPieces + 1;  // +1 for primary piece
        int currentCount = 0;

        for (int i = 0; i < board.length && currentCount < targetCount; i++) {
            for (int j = 0; j < board[i].length && currentCount < targetCount; j++) {
                char c = board[i][j];
                
                if (!Character.isLetter(c) || c == 'K' || processedPieces.contains(c)) {
                    continue;
                }

                List<Coordinate> coordinates = new ArrayList<>();
                for (int k = i; k < board.length; k++) {
                    for (int l = (k == i ? j : 0); l < board[k].length; l++) {
                        if (board[k][l] == c) {
                            coordinates.add(new Coordinate(k, l));
                        }
                    }
                }

                boolean isPrimary = c == 'P';
                boolean isHorizontal = Utils.isPieceHorizontal(coordinates);
                pieces.add(new Piece(c, coordinates, isPrimary, isHorizontal));
                
                processedPieces.add(c);
                currentCount++;
            }
        }
        
        return pieces;
    }

    public static Coordinate getExitCoordinate(char[][] fileBoard) {
        for (int i = 0; i < fileBoard.length; i++) {
            for (int j = 0; j < fileBoard[i].length; j++) {
                if (fileBoard[i][j] == 'K') {
                    return new Coordinate(i, j);
                }
            }
        }
        return null;
    }

    // dapatkan sisi keluar dari papan
    public static Move.Direction getExitSide(char[][] fileBoard) {
        for (int i = 0; i < fileBoard.length; i++) {
            for (int j = 0; j < fileBoard[i].length; j++) {
                if (fileBoard[i][j] == 'K') {
                    if (i == 0 && j < fileBoard[i].length - 1) {
                        System.out.println("UP");
                        return Move.Direction.UP;
                    } else if (i == fileBoard.length - 1 && j < fileBoard[i].length - 1) {
                        System.out.println("DOWN");
                        return Move.Direction.DOWN;
                    } else if (j == 0 && i < fileBoard.length - 1) {
                        System.out.println("LEFT");
                        return Move.Direction.LEFT;
                    } else if (j == fileBoard[i].length - 1 && i < fileBoard.length - 1) {
                        System.out.println("RIGHT");
                        return Move.Direction.RIGHT;
                    }
                }
            }
        }
        return null;
    }

    // trim papan sesuai dengan sisi keluar
    public static char[][] trimBoard(char[][] fileBoard, Move.Direction exitSide) {
        switch (exitSide) {
            case UP -> {
                char[][] newBoard = new char[fileBoard.length][fileBoard[0].length-1];
                // trim kolom paling kanan
                for (int i = 0; i < fileBoard.length; i++) {
                    System.arraycopy(fileBoard[i], 0, newBoard[i], 0, fileBoard[i].length - 1);
                }
                return newBoard;
            }
            case DOWN -> {
                char[][] newBoard = new char[fileBoard.length][fileBoard[0].length-1];
                // trim kolom paling kanan
                for (int i = 0; i < fileBoard.length; i++) {
                    System.arraycopy(fileBoard[i], 0, newBoard[i], 0, fileBoard[i].length - 1);
                }
                return newBoard;
            }
            case LEFT -> {
                char[][] newBoard = new char[fileBoard.length-1][fileBoard[0].length];
                // trim baris paling bawah
                for (int i = 0; i < fileBoard.length - 1; i++) {
                    System.arraycopy(fileBoard[i], 0, newBoard[i], 0, fileBoard[i].length);
                }
                return newBoard;
            }
            case RIGHT -> {
                char[][] newBoard = new char[fileBoard.length-1][fileBoard[0].length];
                // trim baris paling bawah
                for (int i = 0; i < fileBoard.length - 1; i++) {
                    System.arraycopy(fileBoard[i], 0, newBoard[i], 0, fileBoard[i].length);
                }
                return newBoard;
            }
        }
        return null;
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

    public static Piece getPieceByName(char name, List<Piece> pieces) {
        for (Piece piece : pieces) {
            if (piece.getName() == name) {
                return piece;
            }
        }
        return null;
    }

    public static Piece getPrimaryPiece(List<Piece> pieces) {
        for (Piece piece : pieces) {
            if (piece.isPrimary()) {
                return piece;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        // Contoh penggunaan
        File file = readInputFile();
        BoardState boardState = convertInput(file);
        if (boardState != null) {
            System.out.println("Board State berhasil dikonversi.");
            System.out.println("Exit Coordinate: " + boardState.getExitCoordinate());
            System.out.println("Primary Piece: " + boardState.getPrimaryPiece().getName());
        } else {
            System.out.println("Gagal mengonversi Board State.");
        }
    }

    public static void findPrimaryPiece(List<Piece> pieces) {
        for (Piece piece : pieces) {
            if (piece.isPrimary()) {
                System.out.println("Primary Piece: " + piece.getName());
                return;
            }
        }
        System.out.println("Primary Piece tidak ditemukan.");
    }
}
