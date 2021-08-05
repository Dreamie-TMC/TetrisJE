package game.tile;

import enums.Shape;
import game.shapes.IShape;
import helpers.functional.ITileUnloadAction;
import helpers.ImageHandler;
import javafx.scene.paint.ImagePattern;

public class Tile extends BaseTile {
    private ITileUnloadAction unloadAction;
    private boolean unloadQueued;
    private int updateCounter;
    private int row;
    private int column;
    private ImagePattern fill;
    private int level;
    private Shape shape;
    private final int rowOffset;
    private final int columnOffset;

    public Tile() {
        this(0, 0, IShape.BASELINE_ROW_OFFSET, IShape.BASELINE_COLUMN_OFFSET, Shape.DUMMY);
    }

    public Tile(int row, int column, int rowOffset, int columnOffset, Shape shape) {
        unloadQueued = false;
        updateCounter = 0;
        setHeight(TILE_WIDTH_HEIGHT);
        setWidth(TILE_WIDTH_HEIGHT);
        this.shape = shape;
        this.row = row;
        this.column = column;
        this.rowOffset = rowOffset;
        this.columnOffset = columnOffset;
        setVisible(false);
    }

    @Override
    public void applyUpdate() {
        if (--updateCounter <= 0) {
            if (unloadQueued) {
                unloadAction.unloadAction(this);
                return;
            }
            setX(columnOffset + (column * TILE_WIDTH_HEIGHT));
            setY(rowOffset + (row * TILE_WIDTH_HEIGHT));
            if (row < 0 && isVisible()) setVisible(false);
            if (!isVisible() && row >= 0) setVisible(true);
            refreshFill();
        }
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
        unloadQueued = true;
        this.unloadAction = unloadAction;
    }

    @Override
    public void setUpdateCounter(int waitTimeInFrames) {
        updateCounter = waitTimeInFrames;
    }

    @Override
    public void updateFill(int level) {
        fill = new ImagePattern(ImageHandler.getImage(shape, level % 10), getX(), getY(), TILE_WIDTH_HEIGHT, TILE_WIDTH_HEIGHT, false);
        this.level = level % 10;
    }

    @Override
    public void changeShape(Shape shape) {
        this.shape = shape;
    }

    private void refreshFill() {
        fill = new ImagePattern(ImageHandler.getImage(shape, level), getX(), getY(), TILE_WIDTH_HEIGHT, TILE_WIDTH_HEIGHT, false);
        setFill(fill);
    }
}
