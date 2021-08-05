package models;

public class Inputs {
    char left;
    char right;
    char down;
    char leftRotate;
    char rightRotate;
    char pause;
    char up;

    public char getLeft() {
        return left;
    }

    public void setLeft(char left) {
        this.left = left;
    }

    public char getRight() {
        return right;
    }

    public void setRight(char right) {
        this.right = right;
    }

    public char getDown() {
        return down;
    }

    public void setDown(char down) {
        this.down = down;
    }

    public char getLeftRotate() {
        return leftRotate;
    }

    public void setLeftRotate(char leftRotate) {
        this.leftRotate = leftRotate;
    }

    public char getRightRotate() {
        return rightRotate;
    }

    public void setRightRotate(char rightRotate) {
        this.rightRotate = rightRotate;
    }

    public char getPause() {
        return pause;
    }

    public void setPause(char pause) {
        this.pause = pause;
    }

    public char getUp() {
        return up;
    }

    public void setUp(char up) {
        this.up = up;
    }

    public static Inputs createDefaultInputsFile() {
        Inputs inputs = new Inputs();
        inputs.setLeft('a');
        inputs.setRight('d');
        inputs.setDown('s');
        inputs.setPause(' ');
        inputs.setLeftRotate('.');
        inputs.setRightRotate('/');
        inputs.setUp('w');
        return inputs;
    }
}
