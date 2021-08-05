package game.tile;

import enums.Shape;
import helpers.ImageHandler;
import helpers.functional.ITileUnloadAction;
import javafx.scene.paint.ImagePattern;

public class FloatingTile extends BaseTile {
    private final double width;
    private final double height;
    private int row;
    private int column;
    private ImagePattern fill;
    private int level;
    private Shape shape;
    private final int rowOffset;
    private final int columnOffset;

    public FloatingTile(int row, int column, int rowOffset, int columnOffset, double width, double height, Shape shape) {
        setHeight(width);
        setWidth(height);
        this.shape = shape;
        this.row = row;
        this.column = column;
        this.rowOffset = rowOffset;
        this.columnOffset = columnOffset;
        this.width = width;
        this.height = height;
        setVisible(false);
    }

    @Override
    public void applyUpdate() {
        setX(columnOffset + (column * height));
        setY(rowOffset + (row * width));
        if (row < 0 && isVisible()) setVisible(false);
        if (!isVisible() && row >= 0) setVisible(true);
        refreshFill();
    }

    @Override
    public void moveDown() {
        ++row;
    }

    @Override
    public void moveLeft() {
        --column;
    }

    @Override
    public void moveRight() {
        ++column;
    }

    @Override
    public void setRow(int row) {
        this.row = row;
    }

    @Override
    public void setColumn(int column) {
        this.column = column;
    }

    @Override
    public int getRow() {
        return row;
    }

    @Override
    public int getColumn() {
        return column;
    }

    @Override
    public void setUnloadAction(ITileUnloadAction unloadAction) {
    }

    @Override
    public void setUpdateCounter(int waitTimeInFrames) {

    }

    @Override
    public void updateFill(int level) {
        fill = new ImagePattern(ImageHandler.getImage(shape, level % 10), getX(), getY(), width, height, false);
        this.level = level % 10;
    }

    @Override
    public void changeShape(Shape shape) {
        this.shape = shape;
    }

    private void refreshFill() {
        fill = new ImagePattern(ImageHandler.getImage(shape, level), getX(), getY(), width, height, false);
        setFill(fill);
    }
}
