package game;

public class DistanceToExitHeuristic implements Heuristic {
    // Konstruktor
    public DistanceToExitHeuristic() {
    }
    
    /**
     * Menghitung nilai heuristik berdasarkan jarak piece utama ke koordinat keluar.
     *
     * @param state BoardState yang akan dihitung nilai heuristiknya.
     * @return Nilai heuristik untuk state tersebut.
     */
    @Override
    public int calcValue(BoardState state) {
        if (state.getPrimaryPiece().isHorizontal()) { // jarak horizontal
            return Math.abs(state.getPrimaryPiece().getCoordinates().get(0).getCol() - state.getExitCoordinate().getCol());
        } else { // jarak vertikal
            return Math.abs(state.getPrimaryPiece().getCoordinates().get(0).getRow() - state.getExitCoordinate().getRow());
        }
    }
}