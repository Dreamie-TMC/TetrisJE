package animations;

import audio.EffectsHandler;
import controllers.MainController;
import enums.Shape;
import game.tile.BaseTile;
import game.tile.FloatingTile;
import game.tile.Tile;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

public final class OpeningCinema implements IAnimation {
    private final Pane screen;
    private static final int BASE_COL_OFFSET = 255;
    private static final int BASE_ROW_OFFSET = 0;
    private static final int WIDTH = 600;
    private static final int HEIGHT = 450;
    private static final double WIDTH_HEIGHT = 12.5;

    private final MainController controller;
    private List<BaseTile> tiles;
    private final List<BaseTile> placed;
    private final List<List<BaseTile>> ending;
    private int shapesDrawn = 0;
    private int frameWait;
    private int endingCycles = 0;
    private boolean invertMovement;
    private int loadTimer;

    public OpeningCinema(Pane pane, MainController controller) {
        screen = pane;
        this.controller = controller;
        frameWait = 20;
        invertMovement = false;
        tiles = new ArrayList<>();
        placed = new ArrayList<>();
        ending = new ArrayList<>();
        loadTimer = 360;
    }

    public void nextFrame() {
        if (--loadTimer == 0)
            controller.showMainMenu();
        if (--frameWait > 0)
            return;
        if (invertMovement) {
            if (--endingCycles >= 0) {
                screen.getChildren().removeAll(ending.get(endingCycles));
                frameWait = 4;
                return;
            }
        }
        if (endingCycles > 0) {
            List<BaseTile> tiles = ending.get(ending.size() - endingCycles);
            screen.getChildren().addAll(tiles);
            for (BaseTile tile : tiles) {
                tile.updateFill(0);
                tile.applyUpdate();
            }
            frameWait = 4;
            if (--endingCycles == 0) {
                invertMovement = true;
                endingCycles = HEIGHT / BaseTile.TILE_WIDTH_HEIGHT;
                frameWait = 40;
            }
            return;
        }
        if (tiles.size() == 0) {
            createNextShape();
            return;
        }

        for (BaseTile tile : tiles) {
            if (tile.getRow() + 1 >= HEIGHT / WIDTH_HEIGHT) {
                placeShape();
                return;
            }

            for (BaseTile t : placed) {
                if (tile.getRow() + 1 == t.getRow()) {
                    placeShape();
                    return;
                }
            }
        }

        for (BaseTile tile : tiles) {
            tile.moveDown();
            tile.applyUpdate();
        }
    }

    private void createNextShape() {
        switch (shapesDrawn) {
            case 5:
            case 3:
                buildT();
                break;
            case 4:
                buildE();
                break;
            case 2:
                buildR();
                break;
            case 1:
                buildI();
                break;
            case 0:
                buildS();
                break;
            case 6:
                loadEndingScene();
        }
        for (BaseTile tile : tiles) {
            tile.updateFill(0);
            tile.applyUpdate();
        }
        screen.getChildren().addAll(tiles);
        shapesDrawn += 1;
    }

    private void buildT() {
        tiles.add(new FloatingTile(0, 0, BASE_ROW_OFFSET, BASE_COL_OFFSET, WIDTH_HEIGHT, WIDTH_HEIGHT, Shape.I));
        tiles.add(new FloatingTile(0, 1, BASE_ROW_OFFSET, BASE_COL_OFFSET, WIDTH_HEIGHT, WIDTH_HEIGHT, Shape.I));
        tiles.add(new FloatingTile(0, 2, BASE_ROW_OFFSET, BASE_COL_OFFSET, WIDTH_HEIGHT, WIDTH_HEIGHT, Shape.I));
        tiles.add(new FloatingTile(0, 3, BASE_ROW_OFFSET, BASE_COL_OFFSET, WIDTH_HEIGHT, WIDTH_HEIGHT, Shape.I));
        tiles.add(new FloatingTile(0, 4, BASE_ROW_OFFSET, BASE_COL_OFFSET, WIDTH_HEIGHT, WIDTH_HEIGHT, Shape.I));
        tiles.add(new FloatingTile(0, 5, BASE_ROW_OFFSET, BASE_COL_OFFSET, WIDTH_HEIGHT, WIDTH_HEIGHT, Shape.I));
        tiles.add(new FloatingTile(1, 2, BASE_ROW_OFFSET, BASE_COL_OFFSET, WIDTH_HEIGHT, WIDTH_HEIGHT, Shape.I));
        tiles.add(new FloatingTile(1, 3, BASE_ROW_OFFSET, BASE_COL_OFFSET, WIDTH_HEIGHT, WIDTH_HEIGHT, Shape.I));
        tiles.add(new FloatingTile(2, 2, BASE_ROW_OFFSET, BASE_COL_OFFSET, WIDTH_HEIGHT, WIDTH_HEIGHT, Shape.I));
        tiles.add(new FloatingTile(2, 3, BASE_ROW_OFFSET, BASE_COL_OFFSET, WIDTH_HEIGHT, WIDTH_HEIGHT, Shape.I));
        tiles.add(new FloatingTile(3, 2, BASE_ROW_OFFSET, BASE_COL_OFFSET, WIDTH_HEIGHT, WIDTH_HEIGHT, Shape.I));
        tiles.add(new FloatingTile(3, 3, BASE_ROW_OFFSET, BASE_COL_OFFSET, WIDTH_HEIGHT, WIDTH_HEIGHT, Shape.I));
        tiles.add(new FloatingTile(4, 2, BASE_ROW_OFFSET, BASE_COL_OFFSET, WIDTH_HEIGHT, WIDTH_HEIGHT, Shape.I));
        tiles.add(new FloatingTile(4, 3, BASE_ROW_OFFSET, BASE_COL_OFFSET, WIDTH_HEIGHT, WIDTH_HEIGHT, Shape.I));
        tiles.add(new FloatingTile(5, 2, BASE_ROW_OFFSET, BASE_COL_OFFSET, WIDTH_HEIGHT, WIDTH_HEIGHT, Shape.I));
        tiles.add(new FloatingTile(5, 3, BASE_ROW_OFFSET, BASE_COL_OFFSET, WIDTH_HEIGHT, WIDTH_HEIGHT, Shape.I));
    }

    private void buildE() {
        tiles.add(new FloatingTile(0, 0, BASE_ROW_OFFSET, BASE_COL_OFFSET, WIDTH_HEIGHT, WIDTH_HEIGHT, Shape.L));
        tiles.add(new FloatingTile(0, 1, BASE_ROW_OFFSET, BASE_COL_OFFSET, WIDTH_HEIGHT, WIDTH_HEIGHT, Shape.L));
        tiles.add(new FloatingTile(0, 2, BASE_ROW_OFFSET, BASE_COL_OFFSET, WIDTH_HEIGHT, WIDTH_HEIGHT, Shape.L));
        tiles.add(new FloatingTile(0, 3, BASE_ROW_OFFSET, BASE_COL_OFFSET, WIDTH_HEIGHT, WIDTH_HEIGHT, Shape.L));
        tiles.add(new FloatingTile(0, 4, BASE_ROW_OFFSET, BASE_COL_OFFSET, WIDTH_HEIGHT, WIDTH_HEIGHT, Shape.L));
        tiles.add(new FloatingTile(0, 5, BASE_ROW_OFFSET, BASE_COL_OFFSET, WIDTH_HEIGHT, WIDTH_HEIGHT, Shape.L));
        tiles.add(new FloatingTile(1, 0, BASE_ROW_OFFSET, BASE_COL_OFFSET, WIDTH_HEIGHT, WIDTH_HEIGHT, Shape.L));
        tiles.add(new FloatingTile(2, 0, BASE_ROW_OFFSET, BASE_COL_OFFSET, WIDTH_HEIGHT, WIDTH_HEIGHT, Shape.L));
        tiles.add(new FloatingTile(2, 1, BASE_ROW_OFFSET, BASE_COL_OFFSET, WIDTH_HEIGHT, WIDTH_HEIGHT, Shape.L));
        tiles.add(new FloatingTile(2, 2, BASE_ROW_OFFSET, BASE_COL_OFFSET, WIDTH_HEIGHT, WIDTH_HEIGHT, Shape.L));
        tiles.add(new FloatingTile(2, 3, BASE_ROW_OFFSET, BASE_COL_OFFSET, WIDTH_HEIGHT, WIDTH_HEIGHT, Shape.L));
        tiles.add(new FloatingTile(3, 0, BASE_ROW_OFFSET, BASE_COL_OFFSET, WIDTH_HEIGHT, WIDTH_HEIGHT, Shape.L));
        tiles.add(new FloatingTile(3, 1, BASE_ROW_OFFSET, BASE_COL_OFFSET, WIDTH_HEIGHT, WIDTH_HEIGHT, Shape.L));
        tiles.add(new FloatingTile(3, 2, BASE_ROW_OFFSET, BASE_COL_OFFSET, WIDTH_HEIGHT, WIDTH_HEIGHT, Shape.L));
        tiles.add(new FloatingTile(3, 3, BASE_ROW_OFFSET, BASE_COL_OFFSET, WIDTH_HEIGHT, WIDTH_HEIGHT, Shape.L));
        tiles.add(new FloatingTile(4, 0, BASE_ROW_OFFSET, BASE_COL_OFFSET, WIDTH_HEIGHT, WIDTH_HEIGHT, Shape.L));
        tiles.add(new FloatingTile(5, 0, BASE_ROW_OFFSET, BASE_COL_OFFSET, WIDTH_HEIGHT, WIDTH_HEIGHT, Shape.L));
        tiles.add(new FloatingTile(5, 1, BASE_ROW_OFFSET, BASE_COL_OFFSET, WIDTH_HEIGHT, WIDTH_HEIGHT, Shape.L));
        tiles.add(new FloatingTile(5, 2, BASE_ROW_OFFSET, BASE_COL_OFFSET, WIDTH_HEIGHT, WIDTH_HEIGHT, Shape.L));
        tiles.add(new FloatingTile(5, 3, BASE_ROW_OFFSET, BASE_COL_OFFSET, WIDTH_HEIGHT, WIDTH_HEIGHT, Shape.L));
        tiles.add(new FloatingTile(5, 4, BASE_ROW_OFFSET, BASE_COL_OFFSET, WIDTH_HEIGHT, WIDTH_HEIGHT, Shape.L));
        tiles.add(new FloatingTile(5, 5, BASE_ROW_OFFSET, BASE_COL_OFFSET, WIDTH_HEIGHT, WIDTH_HEIGHT, Shape.L));
    }

    private void buildR() {
        tiles.add(new FloatingTile(0, 0, BASE_ROW_OFFSET, BASE_COL_OFFSET, WIDTH_HEIGHT, WIDTH_HEIGHT, Shape.J));
        tiles.add(new FloatingTile(0, 1, BASE_ROW_OFFSET, BASE_COL_OFFSET, WIDTH_HEIGHT, WIDTH_HEIGHT, Shape.J));
        tiles.add(new FloatingTile(0, 2, BASE_ROW_OFFSET, BASE_COL_OFFSET, WIDTH_HEIGHT, WIDTH_HEIGHT, Shape.J));
        tiles.add(new FloatingTile(0, 3, BASE_ROW_OFFSET, BASE_COL_OFFSET, WIDTH_HEIGHT, WIDTH_HEIGHT, Shape.J));
        tiles.add(new FloatingTile(0, 4, BASE_ROW_OFFSET, BASE_COL_OFFSET, WIDTH_HEIGHT, WIDTH_HEIGHT, Shape.J));
        tiles.add(new FloatingTile(1, 0, BASE_ROW_OFFSET, BASE_COL_OFFSET, WIDTH_HEIGHT, WIDTH_HEIGHT, Shape.J));
        tiles.add(new FloatingTile(1, 1, BASE_ROW_OFFSET, BASE_COL_OFFSET, WIDTH_HEIGHT, WIDTH_HEIGHT, Shape.J));
        tiles.add(new FloatingTile(1, 4, BASE_ROW_OFFSET, BASE_COL_OFFSET, WIDTH_HEIGHT, WIDTH_HEIGHT, Shape.J));
        tiles.add(new FloatingTile(1, 5, BASE_ROW_OFFSET, BASE_COL_OFFSET, WIDTH_HEIGHT, WIDTH_HEIGHT, Shape.J));
        tiles.add(new FloatingTile(2, 0, BASE_ROW_OFFSET, BASE_COL_OFFSET, WIDTH_HEIGHT, WIDTH_HEIGHT, Shape.J));
        tiles.add(new FloatingTile(2, 1, BASE_ROW_OFFSET, BASE_COL_OFFSET, WIDTH_HEIGHT, WIDTH_HEIGHT, Shape.J));
        tiles.add(new FloatingTile(2, 2, BASE_ROW_OFFSET, BASE_COL_OFFSET, WIDTH_HEIGHT, WIDTH_HEIGHT, Shape.J));
        tiles.add(new FloatingTile(2, 3, BASE_ROW_OFFSET, BASE_COL_OFFSET, WIDTH_HEIGHT, WIDTH_HEIGHT, Shape.J));
        tiles.add(new FloatingTile(2, 4, BASE_ROW_OFFSET, BASE_COL_OFFSET, WIDTH_HEIGHT, WIDTH_HEIGHT, Shape.J));
        tiles.add(new FloatingTile(3, 0, BASE_ROW_OFFSET, BASE_COL_OFFSET, WIDTH_HEIGHT, WIDTH_HEIGHT, Shape.J));
        tiles.add(new FloatingTile(3, 1, BASE_ROW_OFFSET, BASE_COL_OFFSET, WIDTH_HEIGHT, WIDTH_HEIGHT, Shape.J));
        tiles.add(new FloatingTile(3, 2, BASE_ROW_OFFSET, BASE_COL_OFFSET, WIDTH_HEIGHT, WIDTH_HEIGHT, Shape.J));
        tiles.add(new FloatingTile(4, 0, BASE_ROW_OFFSET, BASE_COL_OFFSET, WIDTH_HEIGHT, WIDTH_HEIGHT, Shape.J));
        tiles.add(new FloatingTile(4, 1, BASE_ROW_OFFSET, BASE_COL_OFFSET, WIDTH_HEIGHT, WIDTH_HEIGHT, Shape.J));
        tiles.add(new FloatingTile(4, 3, BASE_ROW_OFFSET, BASE_COL_OFFSET, WIDTH_HEIGHT, WIDTH_HEIGHT, Shape.J));
        tiles.add(new FloatingTile(4, 4, BASE_ROW_OFFSET, BASE_COL_OFFSET, WIDTH_HEIGHT, WIDTH_HEIGHT, Shape.J));
        tiles.add(new FloatingTile(5, 0, BASE_ROW_OFFSET, BASE_COL_OFFSET, WIDTH_HEIGHT, WIDTH_HEIGHT, Shape.J));
        tiles.add(new FloatingTile(5, 1, BASE_ROW_OFFSET, BASE_COL_OFFSET, WIDTH_HEIGHT, WIDTH_HEIGHT, Shape.J));
        tiles.add(new FloatingTile(5, 5, BASE_ROW_OFFSET, BASE_COL_OFFSET, WIDTH_HEIGHT, WIDTH_HEIGHT, Shape.J));
    }

    private void buildI() {
        buildT();
        tiles.add(new FloatingTile(5, 0, BASE_ROW_OFFSET, BASE_COL_OFFSET, WIDTH_HEIGHT, WIDTH_HEIGHT, Shape.I));
        tiles.add(new FloatingTile(5, 1, BASE_ROW_OFFSET, BASE_COL_OFFSET, WIDTH_HEIGHT, WIDTH_HEIGHT, Shape.I));
        tiles.add(new FloatingTile(5, 4, BASE_ROW_OFFSET, BASE_COL_OFFSET, WIDTH_HEIGHT, WIDTH_HEIGHT, Shape.I));
        tiles.add(new FloatingTile(5, 5, BASE_ROW_OFFSET, BASE_COL_OFFSET, WIDTH_HEIGHT, WIDTH_HEIGHT, Shape.I));
    }

    private void buildS() {
        tiles.add(new FloatingTile(0, 1, BASE_ROW_OFFSET, BASE_COL_OFFSET, WIDTH_HEIGHT, WIDTH_HEIGHT, Shape.L));
        tiles.add(new FloatingTile(0, 2, BASE_ROW_OFFSET, BASE_COL_OFFSET, WIDTH_HEIGHT, WIDTH_HEIGHT, Shape.L));
        tiles.add(new FloatingTile(0, 3, BASE_ROW_OFFSET, BASE_COL_OFFSET, WIDTH_HEIGHT, WIDTH_HEIGHT, Shape.L));
        tiles.add(new FloatingTile(0, 4, BASE_ROW_OFFSET, BASE_COL_OFFSET, WIDTH_HEIGHT, WIDTH_HEIGHT, Shape.L));
        tiles.add(new FloatingTile(1, 0, BASE_ROW_OFFSET, BASE_COL_OFFSET, WIDTH_HEIGHT, WIDTH_HEIGHT, Shape.L));
        tiles.add(new FloatingTile(1, 5, BASE_ROW_OFFSET, BASE_COL_OFFSET, WIDTH_HEIGHT, WIDTH_HEIGHT, Shape.L));
        tiles.add(new FloatingTile(2, 1, BASE_ROW_OFFSET, BASE_COL_OFFSET, WIDTH_HEIGHT, WIDTH_HEIGHT, Shape.L));
        tiles.add(new FloatingTile(2, 2, BASE_ROW_OFFSET, BASE_COL_OFFSET, WIDTH_HEIGHT, WIDTH_HEIGHT, Shape.L));
        tiles.add(new FloatingTile(2, 3, BASE_ROW_OFFSET, BASE_COL_OFFSET, WIDTH_HEIGHT, WIDTH_HEIGHT, Shape.L));
        tiles.add(new FloatingTile(3, 4, BASE_ROW_OFFSET, BASE_COL_OFFSET, WIDTH_HEIGHT, WIDTH_HEIGHT, Shape.L));
        tiles.add(new FloatingTile(4, 0, BASE_ROW_OFFSET, BASE_COL_OFFSET, WIDTH_HEIGHT, WIDTH_HEIGHT, Shape.L));
        tiles.add(new FloatingTile(4, 5, BASE_ROW_OFFSET, BASE_COL_OFFSET, WIDTH_HEIGHT, WIDTH_HEIGHT, Shape.L));
        tiles.add(new FloatingTile(5, 1, BASE_ROW_OFFSET, BASE_COL_OFFSET, WIDTH_HEIGHT, WIDTH_HEIGHT, Shape.L));
        tiles.add(new FloatingTile(5, 2, BASE_ROW_OFFSET, BASE_COL_OFFSET, WIDTH_HEIGHT, WIDTH_HEIGHT, Shape.L));
        tiles.add(new FloatingTile(5, 3, BASE_ROW_OFFSET, BASE_COL_OFFSET, WIDTH_HEIGHT, WIDTH_HEIGHT, Shape.L));
        tiles.add(new FloatingTile(5, 4, BASE_ROW_OFFSET, BASE_COL_OFFSET, WIDTH_HEIGHT, WIDTH_HEIGHT, Shape.L));
    }

    private void loadEndingScene() {
        screen.getChildren().removeAll(placed);
        for (int i = 0; i < HEIGHT / BaseTile.TILE_WIDTH_HEIGHT; ++i) {
            List<BaseTile> temp = new ArrayList<>();
            for (int j = 0; j < WIDTH / BaseTile.TILE_WIDTH_HEIGHT; ++j) {
                temp.add(new Tile(i, j, BASE_ROW_OFFSET, BASE_ROW_OFFSET, Shape.DUMMY));
            }
            ending.add(temp);
        }
        endingCycles = HEIGHT / BaseTile.TILE_WIDTH_HEIGHT;
        EffectsHandler.getInstance().playClipFromName("over");
    }

    private void placeShape() {
        frameWait = 16;
        EffectsHandler.getInstance().playClipFromName("place");
        placed.addAll(tiles);
        tiles = new ArrayList<>();
    }
}
