package menu;

import audio.MusicPlayer;
import com.google.gson.Gson;
import controllers.IStatefulController;
import controllers.OptionsController;
import enums.Direction;
import enums.Input;
import enums.states.ControllerState;
import enums.states.MenuState;
import helpers.IInputBuffer;
import helpers.ImageHandler;
import helpers.InputBuffer;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import launcher.Setup;
import models.HighScores;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Menu implements IMenu {
    private final Pane scores;
    private final Pane musicPlayer;
    private final Pane levels;
    private final Label song;
    private final Rectangle selector;
    private final IStatefulController<ControllerState> controller;
    private final IInputBuffer buffer;
    private int levelIncrement;
    private MenuState state;
    private Direction direction;
    private HighScores highScores;

    public Menu(Pane scores, Pane musicPlayer, Label song, Rectangle selector, Pane levels, IStatefulController<ControllerState> controller) {
        this.scores = scores;
        this.musicPlayer = musicPlayer;
        this.song = song;
        this.selector = selector;
        this.levels = levels;
        this.controller = controller;
        buffer = new InputBuffer();
        levelIncrement = 0;
        state = MenuState.LEVEL_SELECT;
        direction = Direction.NONE;
        initializeMusicPlayer();
        initializeScores(0);
    }

    @Override
    public void nextFrame() {
        if (buffer.isLeft()) moveLeft();
        else if (buffer.isRight()) moveRight();
        else if (buffer.isDown()) moveDown();
        else if (buffer.isUp()) moveUp();
        else clearMove();
        if (buffer.isLeftRotate() || buffer.isRightRotate()) goForward();
    }

    @Override
    public void addInput(Input input) {
        buffer.add(input);
    }

    @Override
    public void removeInput(Input input) {
        buffer.remove(input);
    }

    @Override
    public void addHighScore(long score) {
        if (highScores.isTopSixScore(score)) {
            highScores.addScore(score);
            Setup.writeHighScores(highScores);
        }
    }

    @Override
    public long getTopScore() {
        return highScores.getScore(0);
    }

    private void initializeScores(int attemptCount) {
        try {
            Gson gson = new Gson();
            Scanner in = new Scanner(new File(Setup.SCORES_PATH));
            highScores = gson.fromJson(in.nextLine(), HighScores.class);
            int j = 1;
            for (int i = 1; i < 7; ++i) {
                HBox row = (HBox)scores.getChildren().get(j);
                VBox entry = (VBox)row.getChildren().get(i % 2 == 0 ? 1 : 0);
                ((Label)entry.getChildren().get(0)).setText(highScores.getScore(i - 1) + "");
                if (i % 2 == 0) ++j;
            }
        } catch (IOException e) {
            Setup.writeHighScores(HighScores.createDefaultScores());
            if (attemptCount == 5)
                return;
            initializeScores(++attemptCount);
        }
    }

    private void initializeMusicPlayer() {
        MusicPlayer player = MusicPlayer.getInstance();
        ((ImageView)musicPlayer.getChildren().get(0)).setImage(ImageHandler.getLeftArrow());
        musicPlayer.getChildren().get(0).setOpacity(player.hasPreviousTrack() ? 1 : .5);
        ((ImageView)musicPlayer.getChildren().get(3)).setImage(ImageHandler.getRightArrow());
        musicPlayer.getChildren().get(3).setOpacity(player.hasNextTrack() ? 1 : .5);
        ((ImageView)musicPlayer.getChildren().get(2)).setImage(ImageHandler.getPlay());
        musicPlayer.getChildren().get(2).setOpacity(.5);
        ((ImageView)musicPlayer.getChildren().get(1)).setImage(ImageHandler.getPause());
        musicPlayer.getChildren().get(1).setOpacity(1);
        song.setText(MusicPlayer.getTrackName());
    }

    private void goForward() {
        buffer.remove(Input.RROTATE);
        buffer.remove(Input.LROTATE);
        switch (state) {
            case LEVEL_SELECT:
                levelSelect();
                break;
            case SONG_SELECT:
                musicPlayerSelect();
                break;
            case OPTIONS:
                loadOptionsMenu();
                break;
        }
    }

    private void loadOptionsMenu() {
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            Pane root = loader.load(getClass().getResourceAsStream("/fxml/options.fxml"));
            OptionsController controller = loader.getController();
            controller.giveWindowReference(stage);
            stage.setScene(new Scene(root));
            stage.getIcons().add(ImageHandler.getIcon());
            stage.show();
        } catch (Exception e) {
            System.out.println("Unexpected exception. Ignore.");
        }
    }

    public void levelSelect() {
        if ((int)selector.getLayoutY() == 44) {
            switch ((int)selector.getLayoutX()) {
                case 58:
                    controller.setLevel(Integer.parseInt(((Label)levels.getChildren().get(0)).getText()));
                    break;
                case 94:
                    controller.setLevel(Integer.parseInt(((Label)levels.getChildren().get(1)).getText()));
                    break;
                case 130:
                    controller.setLevel(Integer.parseInt(((Label)levels.getChildren().get(2)).getText()));
                    break;
                case 166:
                    controller.setLevel(Integer.parseInt(((Label)levels.getChildren().get(3)).getText()));
                    break;
                case 202:
                    controller.setLevel(Integer.parseInt(((Label)levels.getChildren().get(4)).getText()));
                    break;
                case 238:
                    if (levelIncrement > 0)
                        levelIncrement--;
                    if (levelIncrement == 0)
                        levels.getChildren().get(5).setDisable(true);

                    levels.getChildren().get(11).setDisable(false);
                    updateLevelChildren();
                    return;
                default:
                    return;
            }
        } else {
            switch ((int)selector.getLayoutX()) {
                case 58:
                    controller.setLevel(Integer.parseInt(((Label)levels.getChildren().get(6)).getText()));
                    break;
                case 94:
                    controller.setLevel(Integer.parseInt(((Label)levels.getChildren().get(7)).getText()));
                    break;
                case 130:
                    controller.setLevel(Integer.parseInt(((Label)levels.getChildren().get(8)).getText()));
                    break;
                case 166:
                    controller.setLevel(Integer.parseInt(((Label)levels.getChildren().get(9)).getText()));
                    break;
                case 202:
                    controller.setLevel(Integer.parseInt(((Label)levels.getChildren().get(10)).getText()));
                    break;
                case 238:
                    if (levelIncrement < 2)
                        levelIncrement++;
                    if (levelIncrement == 2)
                        levels.getChildren().get(11).setDisable(true);

                    levels.getChildren().get(5).setDisable(false);
                    updateLevelChildren();
                    return;
                default:
                    return;
            }
        }
        buffer.flush();
        controller.updateState(ControllerState.PLAYING);
    }

    private void updateLevelChildren() {
        for (int i = 0; i < 5; ++i) {
            Label temp = (Label)levels.getChildren().get(i);
            if (levelIncrement == 0) {
                temp.setFont(Font.font("Arial Black", 32));
                temp.setText(i + "");
            } else {
                temp.setFont(Font.font("Arial Black", 20));
                temp.setText(levelIncrement + "" + i);
            }
        }
        for (int i = 6; i < 11; ++i) {
            Label temp = (Label)levels.getChildren().get(i);
            if (levelIncrement == 0) {
                temp.setFont(Font.font("Arial Black", 32));
                temp.setText((i - 1) + "");
            } else {
                temp.setFont(Font.font("Arial Black", 20));
                temp.setText(levelIncrement + "" + (i - 1));
            }
        }
    }

    private void musicPlayerSelect() {
        MusicPlayer player = MusicPlayer.getInstance();
        switch ((int)selector.getLayoutX()) {
            case 421:
                if (!player.hasPreviousTrack())
                    break;
                player.stop();
                if (!player.selectPreviousTrack())
                    musicPlayer.getChildren().get(0).setOpacity(.5);
                musicPlayer.getChildren().get(3).setOpacity(player.hasNextTrack() ? 1 : .5);
                musicPlayer.getChildren().get(2).setOpacity(.5);
                musicPlayer.getChildren().get(1).setOpacity(1);
                break;
            case 455:
                if (!player.isPlaying())
                    return;
                player.pause();
                musicPlayer.getChildren().get(1).setOpacity(.5);
                musicPlayer.getChildren().get(2).setOpacity(1);
                break;
            case 489:
                if (player.isPlaying())
                    return;
                player.pause();
                musicPlayer.getChildren().get(2).setOpacity(.5);
                musicPlayer.getChildren().get(1).setOpacity(1);
                break;
            case 523:
                if (!player.hasNextTrack())
                    break;
                player.stop();
                if (!player.selectNextTrack())
                    musicPlayer.getChildren().get(3).setOpacity(.5);
                musicPlayer.getChildren().get(0).setOpacity(player.hasPreviousTrack() ? 1 : .5);
                musicPlayer.getChildren().get(2).setOpacity(.5);
                musicPlayer.getChildren().get(1).setOpacity(1);
                break;
        }
        song.setText(MusicPlayer.getTrackName());
    }

    private void optionSelect() {

    }

    private void clearMove() {
        direction = Direction.NONE;
    }

    private void moveUp() {
        if (direction == Direction.UP)
            return;
        switch (state) {
            case LEVEL_SELECT:
                if (selector.getLayoutY() <= 44)
                    return;
                selector.setLayoutY(44);
                break;
            case OPTIONS:
                selector.setLayoutX(421);
                selector.setLayoutY(117);
                selector.setWidth(37);
                state = MenuState.SONG_SELECT;
                break;
        }
        direction = Direction.UP;
    }

    private void moveDown() {
        if (direction == Direction.DOWN)
            return;
        switch (state) {
            case LEVEL_SELECT:
                if (selector.getLayoutY() <= 44) {
                    selector.setLayoutY(80);
                    break;
                }
            case SONG_SELECT:
                selector.setWidth(111);
                selector.setLayoutY(294);
                selector.setLayoutX(435);
                state = MenuState.OPTIONS;
                break;
        }
        direction = Direction.DOWN;
    }

    private void moveLeft() {
        if (direction == Direction.LEFT)
            return;
        switch (state) {
            case LEVEL_SELECT:
                if (selector.getLayoutX() <= 58)
                    return;
                selector.setLayoutX(selector.getLayoutX() - (selector.getWidth() - 1));
                break;
            case SONG_SELECT:
                if (selector.getLayoutX() <= 435) {
                    selector.setLayoutX(238);
                    selector.setLayoutY(44);
                    state = MenuState.LEVEL_SELECT;
                    break;
                }
                selector.setLayoutX(selector.getLayoutX() - 34);
                break;
        }
        direction = Direction.LEFT;
    }

    private void moveRight() {
        if (direction == Direction.RIGHT)
            return;
        switch (state) {
            case LEVEL_SELECT:
                if (selector.getLayoutX() >= 238) {
                    selector.setLayoutX(421);
                    selector.setLayoutY(117);
                    state = MenuState.SONG_SELECT;
                    break;
                }
                selector.setLayoutX(selector.getLayoutX() + (selector.getWidth() - 1));
                break;
            case SONG_SELECT:
                if (selector.getLayoutX() >= 522.5)
                    break;
                selector.setLayoutX(selector.getLayoutX() + 34);
        }
        direction = Direction.RIGHT;
    }
}
