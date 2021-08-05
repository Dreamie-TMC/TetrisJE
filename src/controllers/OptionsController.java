package controllers;

import enums.InputHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import launcher.Setup;
import models.Inputs;

public class OptionsController {
    @FXML
    private TextField up;
    @FXML
    private TextField down;
    @FXML
    private TextField left;
    @FXML
    private TextField right;
    @FXML
    private TextField lrotate;
    @FXML
    private TextField rrotate;
    @FXML
    private TextField pause;


    private Stage stage;

    public void giveWindowReference(Stage stage) {
        this.stage = stage;
    }

    public void initialize() {
        InputHandler.loadInputs();
        Inputs i = InputHandler.getInputs();
        up.setText(i.getUp() + "");
        down.setText(i.getDown() + "");
        left.setText(i.getLeft() + "");
        right.setText(i.getRight() + "");
        rrotate.setText(i.getRightRotate() + "");
        lrotate.setText(i.getLeftRotate() + "");
        pause.setText(i.getPause() + "");
    }

    @FXML
    public void save(ActionEvent actionEvent) {
        Inputs i = new Inputs();
        i.setUp(up.getText().charAt(0));
        i.setDown(down.getText().charAt(0));
        i.setLeft(left.getText().charAt(0));
        i.setRight(right.getText().charAt(0));
        i.setRightRotate(rrotate.getText().charAt(0));
        i.setLeftRotate(lrotate.getText().charAt(0));
        i.setPause(pause.getText().charAt(0));
        Setup.writeInputs(i);
        InputHandler.loadInputs();
        stage.close();
    }
}
