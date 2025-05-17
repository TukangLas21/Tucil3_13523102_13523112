package Game;

public class Move {
    private char pieceName;
    private Direction direction;
    BoardState board;

    public Move(char pieceName, Direction direction, BoardState board) {
        this.pieceName = pieceName;
        this.direction = direction;
        this.board = board;
    }

    public enum Direction {
        UP, DOWN, LEFT, RIGHT
    }
}
