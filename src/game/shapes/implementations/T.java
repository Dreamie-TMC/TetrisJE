package game.shapes.implementations;

import enums.Direction;
import enums.Shape;
import game.shapes.BaseShape;
import game.tile.BaseTile;
import game.tile.Tile;
import helpers.functional.IFlipAction;
import helpers.functional.IRotateAction;

public class T extends BaseShape {

    /**
     * Shape ID for this object type
     */
    private static final Shape id = Shape.T;

    /**
     * The number of tiles that make up this shape
     */
    private static final int tileCount = 4;

    protected T() {
        super(tileCount);
    }

    public T(boolean next, int level) {
        super(tileCount);
        if (!next) initialize(level, 4, BASELINE_ROW_OFFSET, BASELINE_COLUMN_OFFSET);
        else initialize(level, 0, BASELINE_NEXT_ROW_OFFSET_EVEN, BASELINE_NEXT_COLUMN_OFFSET_ODD);
    }

    @Override
    public IRotateAction getShapeRotationAction(Direction direction) {
        if (direction == Direction.LEFT) rotationAmount = (rotationAmount + 2) % 4;
        if (rotationAmount == 0)
            return getRotationActionZero(direction);
        else if (rotationAmount == 1)
            return getRotationActionOne(direction);
        else if (rotationAmount == 2)
            return getRotationActionTwo(direction);
        else
            return getRotationActionThree(direction);
    }

    private IRotateAction getRotationActionZero(Direction direction) {
        return board -> {
            IFlipAction action = null;
            if (direction == Direction.LEFT) {
                action = getFlip(0, -2, 2, 0, 0, 2);
                action.flip();
            }
            BaseTile tile1, tile2, tile3;
            tile1 = tiles[0];
            if (!verifyTilePotentialRotation(tile1, -1, 1, board, action)) return false;
            tile2 = tiles[1];
            if (!verifyTilePotentialRotation(tile2, -1, -1, board, action)) return false;
            tile3 = tiles[3];
            if (!verifyTilePotentialRotation(tile3, 1, -1, board, action)) return false;
            applyTileRotation(tile1, -1, 1);
            applyTileRotation(tile2, -1, -1);
            applyTileRotation(tile3, 1, -1);
            ++rotationAmount;
            return true;
        };
    }

    private IRotateAction getRotationActionOne(Direction direction) {
        return board -> {
            IFlipAction action = null;
            if (direction == Direction.LEFT) {
                action = getFlip(-2, 0, 0, -2, 2, 0);
                action.flip();
            }
            BaseTile tile1, tile2, tile3;
            tile1 = tiles[0];
            if (!verifyTilePotentialRotation(tile1, 1, 1, board, action)) return false;
            tile2 = tiles[1];
            if (!verifyTilePotentialRotation(tile2, -1, 1, board, action)) return false;
            tile3 = tiles[3];
            if (!verifyTilePotentialRotation(tile3, -1, -1, board, action)) return false;
            applyTileRotation(tile1, 1, 1);
            applyTileRotation(tile2, -1, 1);
            applyTileRotation(tile3, -1, -1);
            ++rotationAmount;
            return true;
        };
    }

    private IRotateAction getRotationActionTwo(Direction direction) {
        return board -> {
            IFlipAction action = null;
            if (direction == Direction.LEFT) {
                action = getFlip(0, 2, -2, 0, 0, -2);
                action.flip();
            }
            BaseTile tile1, tile2, tile3;
            tile1 = tiles[0];
            if (!verifyTilePotentialRotation(tile1, 1, -1, board, action)) return false;
            tile2 = tiles[1];
            if (!verifyTilePotentialRotation(tile2, 1, 1, board, action)) return false;
            tile3 = tiles[3];
            if (!verifyTilePotentialRotation(tile3, -1, 1, board, action)) return false;
            applyTileRotation(tile1, 1, -1);
            applyTileRotation(tile2, 1, 1);
            applyTileRotation(tile3, -1, 1);
            ++rotationAmount;
            return true;
        };
    }

    private IRotateAction getRotationActionThree(Direction direction) {
        return board -> {
            IFlipAction action = null;
            if (direction == Direction.LEFT) {
                action = getFlip(2, 0, 0, 2, -2, 0);
                action.flip();
            }
            BaseTile tile1, tile2, tile3;
            tile1 = tiles[0];
            if (!verifyTilePotentialRotation(tile1, -1, -1, board, action)) return false;
            tile2 = tiles[1];
            if (!verifyTilePotentialRotation(tile2, 1, -1, board, action)) return false;
            tile3 = tiles[3];
            if (!verifyTilePotentialRotation(tile3, 1, 1, board, action)) return false;
            applyTileRotation(tile1, -1, -1);
            applyTileRotation(tile2, 1, -1);
            applyTileRotation(tile3, 1, 1);
            rotationAmount = 0;
            return true;
        };
    }

    protected void initialize(int level, int columnStart, int rowOffset, int columnOffset) {
        BaseTile tile = new Tile(0, columnStart, rowOffset, columnOffset, id);
        tile.updateFill(level);
        tiles[0] = tile;
        tile = new Tile(1, columnStart + 1, rowOffset, columnOffset, id);
        tile.updateFill(level);
        tiles[1] = tile;
        tile = new Tile(0, columnStart + 1, rowOffset, columnOffset, id);
        tile.updateFill(level);
        tiles[2] = tile;
        tile = new Tile(0, columnStart + 2, rowOffset, columnOffset, id);
        tile.updateFill(level);
        tiles[3] = tile;
    }
}
