package helpers;

import enums.Shape;
import javafx.scene.image.Image;

import java.util.HashMap;
import java.util.Map;

public class ImageHandler {
    private static Map<Shape, Map<Integer, Image>> images;
    private static Image normalBg;
    private static Image tetrisBg;
    private static Image cinemaBg;
    private static Image menuBg;
    private static Image icon;
    private static Image leftArrow;
    private static Image rightArrow;
    private static Image pause;
    private static Image play;

    private ImageHandler() {}

    public static void load() {
        images = new HashMap<>();
        Map<Integer, Image> row = getImages("Z_L_");
        images.put(Shape.L, row);
        images.put(Shape.Z, row);
        row = getImages("S_J_");
        images.put(Shape.J, row);
        images.put(Shape.S, row);
        row = getImages("Bar_Box_");
        images.put(Shape.I, row);
        images.put(Shape.O, row);
        images.put(Shape.T, row);
        images.put(Shape.DUMMY, getImages("LEVEL_BLIND_"));
        tetrisBg = new Image(ImageHandler.class.getResourceAsStream("/img/tetris_flash.png"));
        normalBg = new Image(ImageHandler.class.getResourceAsStream("/img/background.png"));
        cinemaBg = new Image(ImageHandler.class.getResourceAsStream("/img/cinema.png"));
        menuBg = new Image(ImageHandler.class.getResourceAsStream("/img/menu.png"));
        icon = new Image(ImageHandler.class.getResourceAsStream("/img/icon.png"));
        leftArrow = new Image(ImageHandler.class.getResourceAsStream("/img/left-arrow.png"));
        pause = new Image(ImageHandler.class.getResourceAsStream("/img/pause-button.png"));
        play = new Image(ImageHandler.class.getResourceAsStream("/img/play-button.png"));
        rightArrow = new Image(ImageHandler.class.getResourceAsStream("/img/right-arrow.png"));
    }

    private static Map<Integer, Image> getImages(String pathPrefix) {
        Map<Integer, Image> images = new HashMap<>();
        for (int i = 0; i <= 9; ++i) {
            images.put(i, new Image(ImageHandler.class.getResourceAsStream("/img/" + pathPrefix + i + ".png")));
        }
        return images;
    }

    public static Image getImage(Shape shape, int level) {
        if (images == null) load();
        return images.get(shape).get(level);
    }

    public static Image getTetrisBg() {
        if (tetrisBg == null) load();
        return tetrisBg;
    }

    public static Image getNormalBg() {
        if (normalBg == null) load();
        return normalBg;
    }

    public static Image getCinemaBg() {
        if (cinemaBg == null) load();
        return cinemaBg;
    }

    public static Image getMenuBg() {
        if (menuBg == null) load();
        return menuBg;
    }

    public static Image getIcon() {
        if (icon == null) load();
        return icon;
    }

    public static Image getLeftArrow() {
        if (leftArrow == null) load();
        return leftArrow;
    }

    public static Image getPause() {
        if (pause == null) load();
        return pause;
    }

    public static Image getPlay() {
        if (play == null) load();
        return play;
    }

    public static Image getRightArrow() {
        if (rightArrow == null) load();
        return rightArrow;
    }
}
