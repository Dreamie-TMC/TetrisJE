package game.shapes.implementations;

import enums.Direction;
import enums.Shape;
import game.shapes.BaseShape;
import game.tile.BaseTile;
import game.tile.Tile;
import helpers.functional.IRotateAction;

public class O extends BaseShape {
    /**
     * Shape ID for this object type
     */
    private static final Shape id = Shape.O;

    /**
     * The number of tiles that make up this shape
     */
    private static final int tileCount = 4;

    protected O() {
        super(tileCount);
    }

    public O(boolean next, int level) {
        super(tileCount);
        if (!next) initialize(level, 4, BASELINE_ROW_OFFSET, BASELINE_COLUMN_OFFSET);
        else initialize(level, 1, BASELINE_NEXT_ROW_OFFSET_EVEN, BASELINE_NEXT_COLUMN_OFFSET_EVEN);
    }

    @Override
    public IRotateAction getShapeRotationAction(Direction direction) {
        //This piece can't rotate so just return true so the sfx plays
        return board -> true;
    }

    protected void initialize(int level, int columnStart, int rowOffset, int columnOffset) {
        BaseTile tile = new Tile(0, columnStart, rowOffset, columnOffset, id);
        tile.updateFill(level);
        tiles[0] = tile;
        tile = new Tile(0, columnStart + 1, rowOffset, columnOffset, id);
        tile.updateFill(level);
        tiles[1] = tile;
        tile = new Tile(1, columnStart, rowOffset, columnOffset, id);
        tile.updateFill(level);
        tiles[2] = tile;
        tile = new Tile(1, columnStart + 1, rowOffset, columnOffset, id);
        tile.updateFill(level);
        tiles[3] = tile;
    }
}
