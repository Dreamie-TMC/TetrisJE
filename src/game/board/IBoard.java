package game.board;

public interface IBoard {
    int[] FRAME_DROP_PER_LEVEL = {48, 43, 38, 33, 28, 23, 18, 13, 8, 6, 5, 5,
            5, 4, 4, 4, 3, 3, 3, 2};

    void moveLeft();

    void moveRight();

    void clearSideMove();

    boolean moveDown();

    void pause(boolean isPaused);

    void rotateRight();

    void rotateLeft();

    void clearRotation();

    boolean advanceFallCounter();

    int clearFullLines();

    int getDownCounter();

    void clear();

    void updateBoard();

    void updateActiveAndNext();

    void levelUp();

    void loadActiveAndNext();

    void reset(int level);

    default int framesByLevel(int level) {
        if (level < 19) {
            return FRAME_DROP_PER_LEVEL[level];
        } else if (level < 29) {
            return 2;
        } else {
            return 1;
        }
    }
}
