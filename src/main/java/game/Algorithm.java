package game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public abstract class Algorithm {
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