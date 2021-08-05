package game.shapes.implementations;

import enums.Direction;
import enums.Shape;
import game.shapes.BaseShape;
import game.tile.BaseTile;
import game.tile.Tile;
import helpers.functional.IRotateAction;

public class I extends BaseShape {
    /**
     * Shape ID for this object type
     */
    private static final Shape id = Shape.I;

    /**
     * The number of tiles that make up this shape
     */
    private static final int tileCount = 4;

    protected I() {
        super(tileCount);
    }

    public I(boolean next, int level) {
        super(tileCount);
        if (!next) initialize(level, 3, BASELINE_ROW_OFFSET, BASELINE_COLUMN_OFFSET);
        else initialize(level, 0, BASELINE_NEXT_ROW_OFFSET_ODD, BASELINE_NEXT_COLUMN_OFFSET_EVEN);
    }

    @Override
    public IRotateAction getShapeRotationAction(Direction direction) {
        if (rotationAmount == 0) {
            return getRotationActionZero();
        }
        else return getRotationActionOne();
    }

    private IRotateAction getRotationActionZero() {
        return board -> {
            BaseTile tile1, tile2, tile3;
            tile1 = tiles[0];
            if (!verifyTilePotentialRotation(tile1, -2, 2, board, null)) return false;
            tile2 = tiles[1];
            if (!verifyTilePotentialRotation(tile2, -1, 1, board, null)) return false;
            tile3 = tiles[3];
            if (!verifyTilePotentialRotation(tile3, 1, -1, board, null)) return false;
            applyTileRotation(tile1, -2, 2);
            applyTileRotation(tile2, -1, 1);
            applyTileRotation(tile3, 1, -1);
            ++rotationAmount;
            return true;
        };
    }

    private IRotateAction getRotationActionOne() {
        return board -> {
            BaseTile tile1, tile2, tile3;
            tile1 = tiles[0];
            if (!verifyTilePotentialRotation(tile1, 2, -2, board, null)) return false;
            tile2 = tiles[1];
            if (!verifyTilePotentialRotation(tile2, 1, -1, board, null)) return false;
            tile3 = tiles[3];
            if (!verifyTilePotentialRotation(tile3, -1, 1, board, null)) return false;
            applyTileRotation(tile1, 2, -2);
            applyTileRotation(tile2, 1, -1);
            applyTileRotation(tile3, -1, 1);
            --rotationAmount;
            return true;
        };
    }

    protected void initialize(int level, int columnStart, int rowOffset, int columnOffset) {
        BaseTile tile = new Tile(0, columnStart, rowOffset, columnOffset, id);
        tile.updateFill(level);
        tiles[0] = tile;
        tile = new Tile(0, columnStart + 1, rowOffset, columnOffset, id);
        tile.updateFill(level);
        tiles[1] = tile;
        tile = new Tile(0, columnStart + 2, rowOffset, columnOffset, id);
        tile.updateFill(level);
        tiles[2] = tile;
        tile = new Tile(0, columnStart + 3, rowOffset, columnOffset, id);
        tile.updateFill(level);
        tiles[3] = tile;
    }
}
