package game.tile;

import enums.Shape;
import helpers.functional.ITileUnloadAction;
import javafx.scene.shape.Rectangle;

public abstract class BaseTile extends Rectangle {
    public static int TILE_WIDTH_HEIGHT = 15;

    public abstract void applyUpdate();

    public abstract void moveDown();

    public abstract void moveLeft();

    public abstract void moveRight();

    public abstract void setRow(int row);

    public abstract void setColumn(int column);

    public abstract int getRow();

    public abstract int getColumn();

    public abstract void setUnloadAction(ITileUnloadAction unloadAction);

    public abstract void setUpdateCounter(int waitTimeInFrames);

    public abstract void updateFill(int level);

    public abstract void changeShape(Shape shape);
}
