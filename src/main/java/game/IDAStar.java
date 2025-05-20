package game;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/* Kelas untuk solver menggunakan algoritma Iterative Deepening A* */
public class IDAStar extends Algorithm {
    // Fungsi solver utama, mengembalikan array of Object berisi jalur dan jumlah gerakan
    public static Object[] solve(BoardState initialState) {
        int threshold = initialState.getValue(); // Threshold awal adalah nilai heuristik dari state awal
        
        // Set berisi state yang sudah dikunjungi
        Set<BoardState> visited = new HashSet<>();
        visited.add(initialState); // Setiap state yang dijelajah dimasukkan

        // Map untuk menyimpan parent dari setiap state
        Map<BoardState, BoardState> parentMap = new HashMap<>();

        while (true) {
            // Lakukan eksplorasi dengan batas threshold
            Result result = search(initialState, threshold, visited, parentMap);

            if (result.isGoalFound) {
                return new Object[]{reconstructPath(parentMap, result.goalState), result.countNode}; // jika sudah mencapai tujuan, kembalikan jalur
            }

            if (result.nextThreshold == Integer.MAX_VALUE) {
                // Jika tidak ada threshold baru (tidak ada state yang bisa dieksplorasi), tidak ada solusi
                return null;
            }

            // Perbarui threshold ke nilai minimum f(n) > threshold sebelumnya
            threshold = result.nextThreshold;
            // reset visited dan parentMap
            visited.clear(); 
            parentMap.clear(); 
            visited.add(initialState); // Setiap state yang dijelajah dimasukkan
        }
    }

    // Fungsi rekursif DFS untuk eksplorasi dengan batas threshold
    private static Result search(BoardState currentState, int threshold, Set<BoardState> visited, Map<BoardState, BoardState> parentMap) {
        int countNode = 0; // menghitung jumlah gerakan yang dieksplorasi
        int f = currentState.getDepth() + currentState.getValue(); // f(n) = g(n) + h(n)

        if (f > threshold) {
            // Jika f(n) melebihi threshold, kembalikan threshold baru
            return new Result(false, null, f, countNode);
        }

        if (currentState.isGoal()) {
            // Jika goal ditemukan, kembalikan hasil
            return new Result(true, currentState, threshold, countNode);
        }

        visited.add(currentState); // Tandai state sebagai dikunjungi
        int minThreshold = Integer.MAX_VALUE; // Threshold baru untuk iterasi berikutnya

        // daftar semua langkah
        List<BoardState> possibleMoves = currentState.getPossibleMoves();
        
        // eksplorasi semua kemungkinan
        for (BoardState nextState : possibleMoves) {
            if (!visited.contains(nextState)) {
                countNode++;
                visited.add(nextState); 
                parentMap.put(nextState, currentState); 

                Result result = search(nextState, threshold, visited, parentMap);
                countNode += result.countNode; 

                if (result.isGoalFound) {
                    // jika sudah mencapai tujuan, kembalikan jalur
                    result.countNode = countNode;
                    return result;
                }

                // Perbarui threshold baru 
                if (result.nextThreshold < minThreshold) {
                    minThreshold = result.nextThreshold;
                }
            }
        }

        return new Result(false, null, minThreshold, countNode);
    }

    // Kelas untuk menyimpan hasil eksplorasi
    private static class Result {
        boolean isGoalFound;
        BoardState goalState;
        int nextThreshold;
        int countNode;

        Result(boolean isGoalFound, BoardState goalState, int nextThreshold, int countNode) {
            this.isGoalFound = isGoalFound;
            this.goalState = goalState;
            this.nextThreshold = nextThreshold;
            this.countNode = countNode;
        }
    }
}