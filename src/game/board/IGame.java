package game.board;

import enums.Input;

public interface IGame {
    void nextFrame();

    void addInput(Input input);

    void removeInput(Input input);

    void applyFrameWait(int frames);

    void tetris();

    void endGame();
}
