package game;

public interface Heuristic {
    /**
     * Menghitung nilai heuristik untuk state tertentu.
     *
     * @param state State yang akan dihitung nilai heuristiknya.
     * @return Nilai heuristik untuk state tersebut.
     */
    int calcValue(BoardState state);
}
