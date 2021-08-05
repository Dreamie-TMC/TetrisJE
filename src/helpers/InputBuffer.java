package helpers;

import enums.Input;

import java.util.ArrayList;
import java.util.List;

public class InputBuffer implements IInputBuffer {
    private List<Input> inputs = new ArrayList<>();

    public void add(Input input) {
        if (!(inputs.contains(input)) && !input.equals(Input.INVALID)) inputs.add(input);
    }

    public void flush() {
        inputs = new ArrayList<>();
    }

    public void remove(Input input) {
        inputs.remove(input);
    }

    public boolean isLeft() {
        return inputs.contains(Input.LEFT);
    }

    public boolean isRight() {
        return inputs.contains(Input.RIGHT);
    }

    public boolean isDown() {
        return inputs.contains(Input.DOWN);
    }

    public boolean isUp() {
        return inputs.contains(Input.UP);
    }

    public boolean isPauseAndConsume() {
        return inputs.remove(Input.PAUSE);
    }

    public boolean isRightRotate() {
        return inputs.contains(Input.RROTATE);
    }

    public boolean isLeftRotate() {
        return inputs.contains(Input.LROTATE);
    }
}
