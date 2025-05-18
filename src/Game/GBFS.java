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

public class GBFS {
    public static List<BoardState> solve(BoardState initialState) {
        BoardState currentState = initialState;

        PriorityQueue<BoardState> queue = new PriorityQueue<>(Comparator.comparingInt(BoardState::getValue));
        queue.add(currentState);

        Set<String> visited = new HashSet<>();
        visited.add(currentState.toString());

        Map<BoardState, BoardState> parentMap = new HashMap<>();

        while (!queue.isEmpty()) {
            currentState = queue.poll();

            if (currentState.isGoal()) {
                return reconstructPath(parentMap, currentState); // kembalikan jalur dari awal
            }

            // daftar semua langkah
            List<BoardState> possibleMoves = currentState.getPossibleMoves();

            // iterasi setiap langkah dan tambahkan jika belum dikunjungi
            for (BoardState nextState : possibleMoves) {
                if (!visited.contains(nextState.toString())) {
                    visited.add(nextState.toString());
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
