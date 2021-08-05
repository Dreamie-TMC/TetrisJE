package game.shapes;

import enums.Direction;
import game.tile.BaseTile;
import helpers.functional.IRotateAction;

public interface IShape {

    /**
     * These two values relate to the TOP LEFT corner of the playing board. This is used to determine the coordinates of tiles.
     */
    int BASELINE_ROW_OFFSET = 75;
    int BASELINE_COLUMN_OFFSET = 240;

    /**
     * These two value relate to the MIDDLE LEFT of the next box on the ui. These should only be used in shapes with the next attribute.
     */
    int BASELINE_NEXT_ROW_OFFSET_EVEN = 245;
    int BASELINE_NEXT_ROW_OFFSET_ODD = 252;
    int BASELINE_NEXT_COLUMN_OFFSET_EVEN = 426;
    int BASELINE_NEXT_COLUMN_OFFSET_ODD = 434;

    BaseTile[] getTiles();

    IRotateAction getShapeRotationAction(Direction direction);
}
