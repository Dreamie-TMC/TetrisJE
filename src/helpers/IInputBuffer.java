package helpers;

import enums.Input;

public interface IInputBuffer {
    void add(Input input);

    void flush();

    void remove(Input input);

    boolean isLeft();

    boolean isRight();

    boolean isDown();

    boolean isUp();

    boolean isPauseAndConsume();

    boolean isRightRotate();

    boolean isLeftRotate();
}
