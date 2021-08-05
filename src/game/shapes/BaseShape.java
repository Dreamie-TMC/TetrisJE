package game.shapes;

import game.tile.BaseTile;
import helpers.functional.IFlipAction;

public abstract class BaseShape implements IShape {
    protected BaseTile[] tiles;
    protected int rotationAmount;

    protected BaseShape(int size) {
        tiles = new BaseTile[size];
        rotationAmount = 0;
    }

    @Override
    public BaseTile[] getTiles() {
        return tiles;
    }

    protected static boolean verifyTilePotentialRotation(BaseTile target, int rowOff, int colOff, BaseTile[][] board, IFlipAction action) {
        if (target.getRow() + rowOff >= 0) {
            if (target.getRow() + rowOff > 19 ||
                target.getColumn() + colOff < 0 ||
                target.getColumn() + colOff > 9 ||
                board[target.getRow() + rowOff][target.getColumn() + colOff] != null) {
                if (action != null) action.undo();
                return false;
            }
        }
        return true;
    }

    protected static void applyTileRotation(BaseTile target, int rowOff, int colOff) {
        target.setRow(target.getRow() + rowOff);
        target.setColumn(target.getColumn() + colOff);
    }

    protected IFlipAction getFlip(
            int tileOneRowOff,
            int tileOneColOff,
            int tileTwoRowOff,
            int tileTwoColOff,
            int tileThreeRowOff,
            int tileThreeColOff) {
        return new IFlipAction() {
            @Override
            public void flip() {
                BaseTile tile1, tile2, tile3;
                tile1 = tiles[0];
                tile2 = tiles[1];
                tile3 = tiles[3];
                tile1.setRow(tile1.getRow() + tileOneRowOff);
                tile1.setColumn(tile1.getColumn() + tileOneColOff);
                tile2.setRow(tile2.getRow() + tileTwoRowOff);
                tile2.setColumn(tile2.getColumn() + tileTwoColOff);
                tile3.setRow(tile3.getRow() + tileThreeRowOff);
                tile3.setColumn(tile3.getColumn() + tileThreeColOff);
            }

            @Override
            public void undo() {
                BaseTile tile1, tile2, tile3;
                tile1 = tiles[0];
                tile2 = tiles[1];
                tile3 = tiles[3];
                tile1.setRow(tile1.getRow() - tileOneRowOff);
                tile1.setColumn(tile1.getColumn() - tileOneColOff);
                tile2.setRow(tile2.getRow() - tileTwoRowOff);
                tile2.setColumn(tile2.getColumn() - tileTwoColOff);
                tile3.setRow(tile3.getRow() - tileThreeRowOff);
                tile3.setColumn(tile3.getColumn() - tileThreeColOff);
                rotationAmount = (rotationAmount + 2) % 4;
            }
        };
    }
}
