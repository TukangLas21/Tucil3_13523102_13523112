package game;

public class Move {
    private final char pieceName;
    private final Direction direction;
    BoardState board;

    public Move(char pieceName, Direction direction, BoardState board) {
        this.pieceName = pieceName;
        this.direction = direction;
        this.board = board;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Move move = (Move) obj;
        return pieceName == move.pieceName && direction == move.direction && board.equals(move.board);
    }

    @Override
    public int hashCode() {
        int result = Character.hashCode(pieceName);
        result = 31 * result + (direction != null ? direction.hashCode() : 0);
        result = 31 * result + (board != null ? board.hashCode() : 0);
        return result;
    }

    public enum Direction {
        UP, DOWN, LEFT, RIGHT
    }
}
