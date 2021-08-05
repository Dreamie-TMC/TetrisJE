package helpers.functional;

import game.tile.BaseTile;

@FunctionalInterface
public interface ITileUnloadAction {
    void unloadAction(BaseTile self);
}
