package Game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

/* Kelas untuk solver menggunakan algoritma Greedy Best First Search */
public class GBFS {
    // Fungsi solver utama, mengembalikan array of Object berisi jalur dan jumlah gerakan
    public static Object[] solve(BoardState initialState) {
        BoardState currentState = initialState; // state awal

        // PriorityQueue untuk menyimpan state berdasarkan nilai heuristik
        PriorityQueue<BoardState> queue = new PriorityQueue<>(Comparator.comparingInt(BoardState::getValue));
        queue.add(currentState);

        // Set berisi state yang sudah dikunjungi
        Set<BoardState> visited = new HashSet<>();
        visited.add(currentState); // Setiap state yang dijelajah dimasukkan

        // Map untuk menyimpan parent dari setiap state
        Map<BoardState, BoardState> parentMap = new HashMap<>();

        int countNode = 0; // menghitung jumlah gerakan yang dieksplorasi

        // eksplorasi semua kemungkinan
        while (!queue.isEmpty()) {
            currentState = queue.poll(); // cek state terdepan
            countNode++; // increment jumlah node

            if (currentState.isGoal()) {
                return new Object[]{reconstructPath(parentMap, currentState), countNode}; // jika sudah mencapai tujuan, kembalikan jalur
            }

            // daftar semua langkah
            List<BoardState> possibleMoves = currentState.getPossibleMoves();

            // iterasi setiap langkah dan tambahkan jika belum dikunjungi
            for (BoardState nextState : possibleMoves) {
                if (!visited.contains(nextState)) {
                    visited.add(nextState);
                    parentMap.put(nextState, currentState);
                    queue.add(nextState);
                }
            }
        }

        return null; // tidak ada solusi
    }

    // Fungsi untuk membangun jalur berdasarkan parentMap
    public static List<BoardState> reconstructPath(Map<BoardState, BoardState> parentMap, BoardState currentState) {
        List<BoardState> path = new ArrayList<>();
        while (currentState != null) {
            path.add(currentState);
            currentState = parentMap.get(currentState);
        }
        Collections.reverse(path);
        return path;
    }
}
