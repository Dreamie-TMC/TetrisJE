package controllers;

import javafx.scene.input.KeyCode;

public interface IController {
    void loadInput(char code);

    void flushInput(char code);

    void initialize();

    void setLevel(int level);
}
