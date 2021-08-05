package enums;

import com.google.gson.Gson;
import javafx.scene.input.KeyCode;
import launcher.Setup;
import models.Inputs;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class InputHandler {
    private static Inputs inputs = null;
    private static final int loadRetryMaximum = 5;

    private InputHandler() {}

    public static void loadInputs() {
        try {
            Gson gson = new Gson();
            Scanner in = new Scanner(new File(Setup.INPUTS_PATH));
            inputs = gson.fromJson(in.nextLine(), Inputs.class);
        } catch (IOException e) {
            Setup.writeInputs(Inputs.createDefaultInputsFile());
        }
    }

    public static Inputs getInputs() {
        return inputs;
    }

    public static Input loadInputFromKeyCode(char cmp) {
        for (int i = 0; inputs == null && i < loadRetryMaximum; ++i)
            loadInputs();
        if (inputs == null)
            throw new IllegalStateException("Failed to parse inputs file after " + loadRetryMaximum + " attempts!");

        if (cmp == inputs.getLeft())
            return Input.LEFT;
        else if (cmp == inputs.getDown())
            return Input.DOWN;
        else if (cmp == inputs.getRight())
            return Input.RIGHT;
        else if (cmp == inputs.getUp())
            return Input.UP;
        else if (cmp == inputs.getPause())
            return Input.PAUSE;
        else if (cmp == inputs.getLeftRotate())
            return Input.LROTATE;
        else if (cmp == inputs.getRightRotate())
            return Input.RROTATE;
        else
            return Input.INVALID;
    }
}
