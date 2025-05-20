package game;

public class BlockingPiecesHeuristic implements Heuristic {
    /**
     * Menghitung nilai heuristik berdasarkan jumlah piece yang menghalangi piece utama
     * untuk mencapai koordinat keluar.
     *
     * @param state BoardState yang akan dihitung nilai heuristiknya.
     * @return Nilai heuristik untuk state tersebut.
     */
    @Override
    public int calcValue(BoardState state) {
        int blockingPiecesCount = 0;
        for (Piece piece : state.getPieces()) {
            if (piece != state.getPrimaryPiece() && state.isPieceBlocking(piece)) {
                blockingPiecesCount++;
            }
        }
        return blockingPiecesCount;
    }
    
}
