package game.board;

import audio.EffectsHandler;
import audio.MusicPlayer;
import enums.Direction;
import enums.Shape;
import enums.ShapeHelper;
import game.shapes.IShape;
import game.shapes.IShapeFactory;
import game.shapes.ShapeFactory;
import game.tile.BaseTile;
import game.tile.Tile;
import helpers.functional.ITextUpdateAction;
import javafx.scene.layout.Pane;

import java.util.Random;

public class Board implements IBoard {
    private int level;
    private IShape active;
    private IShape next;
    private Shape nextShape;
    private final IShapeFactory factory;
    private int downCounter;
    private BaseTile[][] tiles;
    private BaseTile[][] clearedLines;
    private int[] lineClearIds;
    private final Pane pane;
    private int clears;
    private final Random rng;
    private Direction lastRotation;
    private Direction lastMovement;
    private int dasCounter;
    private final IGame parent;
    private final ITextUpdateAction uiCallback;

    public Board(int level, Pane pane, IGame parent, ITextUpdateAction uiCallback) {
        rng = new Random();
        this.uiCallback = uiCallback;
        this.level = level;
        factory = new ShapeFactory();
        this.pane = pane;
        this.parent = parent;
        initialize();
    }

    public int getDownCounter() {
        return downCounter;
    }

    @Override
    public void moveLeft() {
        if (lastMovement != Direction.LEFT || ++dasCounter == 6 || dasCounter == 23) {
            if (verifyLeftMovement()) {
                for (BaseTile tile : active.getTiles()) {
                    tile.moveLeft();
                }
                if (lastMovement != Direction.LEFT) {
                    dasCounter = 7;
                    lastMovement = Direction.LEFT;
                }
                else dasCounter = 0;
                EffectsHandler.getInstance().playClipFromName("move");
            } else dasCounter = 5;
        }
    }

    @Override
    public void moveRight() {
        if (lastMovement != Direction.RIGHT || ++dasCounter == 6 || dasCounter == 23) {
            if (verifyRightMovement()) {
                for (BaseTile tile : active.getTiles()) {
                    tile.moveRight();
                }
                if (lastMovement != Direction.RIGHT) {
                    dasCounter = 7;
                    lastMovement = Direction.RIGHT;
                }
                else dasCounter = 0;
                EffectsHandler.getInstance().playClipFromName("move");
            } else dasCounter = 5;
        }
    }

    @Override
    public void clearSideMove() {
        lastMovement = Direction.NONE;
    }

    @Override
    public boolean moveDown() {
        downCounter = 0;
        if (!verifyVertical()) {
            return placeTiles();
        }
        moveVertical();
        return false;
    }

    @Override
    public void pause(boolean isPaused) {
        MusicPlayer.getInstance().pause();
        if (isPaused) {
            for (BaseTile[] row : tiles)
                for (BaseTile tile : row)
                    if (tile != null) pane.getChildren().add(tile);
            pane.getChildren().addAll(active.getTiles());
            pane.getChildren().addAll(next.getTiles());
        } else {
            for (BaseTile[] row : tiles)
                for (BaseTile tile : row)
                    if (tile != null) pane.getChildren().remove(tile);
            pane.getChildren().removeAll(active.getTiles());
            pane.getChildren().removeAll(next.getTiles());
        }
    }

    @Override
    public void rotateRight() {
        if (lastRotation != Direction.RIGHT) {
            if (active.getShapeRotationAction(Direction.RIGHT).tryRotate(tiles)) {
                lastRotation = Direction.RIGHT;
                EffectsHandler.getInstance().playClipFromName("rotate");
            }
            else lastRotation = Direction.NONE;
        }
    }

    @Override
    public void rotateLeft() {
        if (lastRotation != Direction.LEFT) {
            if (active.getShapeRotationAction(Direction.LEFT).tryRotate(tiles)) {
                lastRotation = Direction.LEFT;
                EffectsHandler.getInstance().playClipFromName("rotate");
            }
            else lastRotation = Direction.NONE;
        }
    }

    @Override
    public void clearRotation() {
        lastRotation = Direction.NONE;
    }

    @Override
    public boolean advanceFallCounter() {
        return ++downCounter == framesByLevel(level);
    }

    @Override
    public int clearFullLines() {
        if (!checkFullLines()) return 0;
        for (int i = tiles.length - 1; i >= 0; --i) {
            for (int j = 0; j <= lineClearIds.length; ++j) {
                if (j == lineClearIds.length || i > lineClearIds[j]) {
                    for (BaseTile tile : tiles[i]) {
                        if (tile != null) {
                            tile.setRow(tile.getRow() + j);
                            tile.setUpdateCounter(24);
                        }
                    }
                    tiles[i + j] = tiles[i];
                    break;
                } else if (i == lineClearIds[j]) {
                    queueTileUnload(tiles[i]);
                    clearedLines[j] = tiles[i];
                    break;
                }
            }
        }
        for (BaseTile tile : next.getTiles()) {
            tile.setUpdateCounter(24);
        }
        for (int i = 0; i < lineClearIds.length; ++i) {
            tiles[i] = new Tile[10];
        }
        clears += lineClearIds.length;
        uiCallback.updateClears(clears, lineClearIds.length % 4 == 0);
        uiCallback.updateScore(determineScore());
        EffectsHandler.getInstance().playClipFromName(lineClearIds.length % 4 == 0 ? "tetris" : "clear");
        return clears;
    }

    @Override
    public void updateBoard() {
        for (BaseTile[] row : tiles) {
            for (BaseTile tile : row) {
                if (tile != null) tile.applyUpdate();
            }
        }
        for (BaseTile[] row : clearedLines) {
            for (BaseTile tile : row) {
                if (tile != null) tile.applyUpdate();
            }
        }
    }

    @Override
    public void updateActiveAndNext() {
        if (active != null)
            for (BaseTile tile : active.getTiles())
                tile.applyUpdate();

        for (BaseTile tile : next.getTiles())
            tile.applyUpdate();
    }

    @Override
    public void clear() {
        for (BaseTile[] tiles : this.tiles) {
            pane.getChildren().removeAll(tiles);
        }
        pane.getChildren().removeAll(next.getTiles());
    }

    @Override
    public void levelUp() {
        ++level;
        for (BaseTile[] row : tiles) {
            for (BaseTile tile : row) {
                if (tile != null) tile.updateFill(level);
            }
        }
        for (BaseTile tile : next.getTiles()) {
            tile.updateFill(level);
        }
        uiCallback.updateLevel(level);
        EffectsHandler.getInstance().playClipFromName("level");
    }

    @Override
    public void loadActiveAndNext() {
        loadNextActiveShape();
        determineNext();
        loadNextShape();
    }

    @Override
    public void reset(int level) {
        for (BaseTile[] row : tiles) {
            pane.getChildren().removeAll(row);
        }
        pane.getChildren().removeAll(next.getTiles());
        if (active != null)
            pane.getChildren().removeAll(active.getTiles());
        this.level = level;
        initialize();
    }

    private boolean verifyLeftMovement() {
        for (BaseTile tile : active.getTiles()) {
            if (tile.getColumn() - 1 < 0 || (tile.getRow() >= 0 && tiles[tile.getRow()][tile.getColumn() - 1] != null)) return false;
        }
        return true;
    }

    private boolean verifyRightMovement() {
        for (BaseTile tile : active.getTiles()) {
            if (tile.getColumn() + 1 > 9 || (tile.getRow() >= 0 && tiles[tile.getRow()][tile.getColumn() + 1] != null)) return false;
        }
        return true;
    }

    private void queueTileUnload(BaseTile[] tiles) {
        for (int i = 4; i >= 0; --i) {
            int delay = 24 - ((i + 1) * 4);
            tiles[i].setUnloadAction(e -> pane.getChildren().remove(e));
            tiles[i].setUpdateCounter(delay);
            tiles[9 - i].setUnloadAction(e -> pane.getChildren().remove(e));
            tiles[9 - i].setUpdateCounter(delay);
        }
    }

    private boolean placeTiles() {
        int row = 19;
        for (BaseTile tile : active.getTiles()) {
            if (tile.getRow() < 0) continue;
            if (tiles[tile.getRow()][tile.getColumn()] != null) {
                gameOver();
                return false;
            }
            writeTile(tile);
            if (tile.getRow() < row) row = tile.getRow();
        }
        if (row < 4) parent.applyFrameWait(18);
        else if (row < 8) parent.applyFrameWait(16);
        else if (row < 12) parent.applyFrameWait(14);
        else if (row < 16) parent.applyFrameWait(12);
        else parent.applyFrameWait(10);
        active = null;
        EffectsHandler.getInstance().playClipFromName("place");
        return true;
    }

    private boolean checkFullLines() {
        int[] temp = new int[4];
        int rows = 0;
        for (int i = tiles.length - 1; i >= 0; --i) {
            BaseTile[] row = tiles[i];
            if (isLineFull(row)) temp[rows++] = i;
        }
        if (rows == 0) return false;
        lineClearIds = new int[rows];
        parent.applyFrameWait(24);
        System.arraycopy(temp, 0, lineClearIds, 0, rows);
        return true;
    }

    private static boolean isLineFull(BaseTile[] line) {
        for (BaseTile tile : line) {
            if (tile == null) return false;
        }
        return true;
    }

    private boolean verifyVertical() {
        for (BaseTile tile : active.getTiles()) {
            if (tile.getRow() + 1 >= 0) {
                if (tile.getRow() + 1 > 19 || tiles[tile.getRow() + 1][tile.getColumn()] != null) return false;
            }
        }
        return true;
    }

    private void moveVertical() {
        for (BaseTile tile : active.getTiles()) {
            tile.moveDown();
        }
    }

    private void initialize() {
        lastRotation = Direction.NONE;
        lastMovement = Direction.NONE;
        dasCounter = 7;
        tiles = new Tile[20][10];
        clearedLines = new Tile[4][10];
        clears = 0;
        nextShape = Shape.DUMMY;
        determineNext();
        loadNextActiveShape();
        determineNext();
        loadNextShape();
    }

    private void loadNextActiveShape() {
        if (next != null) pane.getChildren().removeAll(next.getTiles());
        active = factory.construct(nextShape, level);
        uiCallback.updateStatistic(nextShape);
        pane.getChildren().addAll(active.getTiles());
    }

    private void loadNextShape() {
        next = factory.constructNext(nextShape, level);
        pane.getChildren().addAll(next.getTiles());
    }

    private void determineNext() {
        while (true) {
            Shape temp = ShapeHelper.parseShapeFromIntIdentifier(rng.nextInt(7));
            if (temp != nextShape) {
                nextShape = temp;
                return;
            }
        }
    }

    private int determineScore() {
        switch (lineClearIds.length) {
            case 1:
                return 40 * (level + 1);
            case 2:
                return 100 * (level + 1);
            case 3:
                return 300 * (level + 1);
            case 4:
                return 1200 * (level + 1);
        }
        return 0;
    }

    private void gameOver() {
        parent.endGame();
        for (BaseTile tile : active.getTiles()) {
            if (tile.getRow() < 0) continue;
            writeTile(tile);
        }
        for (int i = 0; i < tiles.length; ++i) {
            BaseTile[] row = tiles[i];
            for (int j = 0; j < row.length; ++j) {
                BaseTile tile = row[j];
                if (tile == null) {
                    tile = new Tile(i, j, IShape.BASELINE_ROW_OFFSET, IShape.BASELINE_COLUMN_OFFSET, Shape.DUMMY);
                    pane.getChildren().add(tile);
                }
                tile.changeShape(Shape.DUMMY);
                tile.setUpdateCounter(i * 4 + 4);
                tile.updateFill(level);
                row[j] = tile;
            }
        }
        MusicPlayer.getInstance().pause();
        EffectsHandler.getInstance().playClipFromName("game");
    }

    private void writeTile(BaseTile tile) {
        if (tiles[tile.getRow()][tile.getColumn()] != null)
            pane.getChildren().remove(tiles[tile.getRow()][tile.getColumn()]);
        tiles[tile.getRow()][tile.getColumn()] = tile;
        if (!pane.getChildren().contains(tile))
            pane.getChildren().add(tile);
    }
}
