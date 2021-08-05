package menu;

import enums.Input;

public interface IMenu {
    void nextFrame();

    void addInput(Input input);

    void removeInput(Input input);

    void addHighScore(long score);

    long getTopScore();
}
