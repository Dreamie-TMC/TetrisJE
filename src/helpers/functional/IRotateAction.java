package helpers.functional;

import game.tile.BaseTile;

@FunctionalInterface
public interface IRotateAction {
    boolean tryRotate(BaseTile[][] board);
}
