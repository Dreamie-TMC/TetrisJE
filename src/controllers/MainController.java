package controllers;

import animations.IAnimation;
import animations.OpeningCinema;
import audio.MusicPlayer;
import enums.InputHandler;
import enums.Shape;
import enums.states.ControllerState;
import game.board.Game;
import game.board.IGame;
import game.shapes.BaseShape;
import game.shapes.IShape;
import game.shapes.implementations.statistics.*;
import game.tile.BaseTile;
import helpers.ImageHandler;
import helpers.functional.ITextUpdateAction;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import menu.IMenu;
import menu.Menu;

public final class MainController implements IStatefulController<ControllerState> {
    //General items - do not hide
    @FXML
    private Pane pane;
    @FXML
    private ImageView background;
    //Menu items
    @FXML
    private Pane menuItemPane;
    @FXML
    private Rectangle selector;
    @FXML
    private Pane scores;
    @FXML
    private Pane musicPlayer;
    @FXML
    private Label song;
    @FXML
    private Pane levels;
    //Game items
    @FXML
    private Pane stats;
    @FXML
    private Label clears;
    @FXML
    private Label top;
    @FXML
    private Label score;
    @FXML
    private Label level;
    @FXML
    private Pane gameItemPane;

    private IGame game;
    private IMenu menu;
    private IAnimation animation;
    private Timeline gameLoop;
    private BaseShape[] statShapes;
    private ControllerState state;
    private int timelineStateChangeCycleCount;
    private int startLevel = 0;

    @Override
    public void loadInput(char code) {
        if (state == ControllerState.MENU)
            menu.addInput(InputHandler.loadInputFromKeyCode(code));
        else if (state == ControllerState.PLAYING)
            game.addInput(InputHandler.loadInputFromKeyCode(code));
    }

    @Override
    public void flushInput(char code) {
        if (state == ControllerState.MENU)
            menu.removeInput(InputHandler.loadInputFromKeyCode(code));
        else if (state == ControllerState.PLAYING)
            game.removeInput(InputHandler.loadInputFromKeyCode(code));
    }

    @Override
    public void initialize() {
        state = ControllerState.ANIMATION;
        updateGameVisibility(false);
        updateMenuVisibility(false);
        background.setImage(ImageHandler.getCinemaBg());
        animation = new OpeningCinema(pane, this);
        timelineStateChangeCycleCount = 490;
        gameLoop = new Timeline();
        KeyFrame frame = new KeyFrame(Duration.seconds(1.0/60), e -> nextFrame());
        gameLoop.getKeyFrames().add(frame);
        gameLoop.setCycleCount(-1);
        gameLoop.play();
    }

    @Override
    public void updateState(ControllerState state) {
        this.state = state;
    }

    @Override
    public void setLevel(int level) {
        startLevel = level;
        this.level.setText(level + "");
        showGame();
    }

    public void showMainMenu() {
        updateGameVisibility(false);
        updateMenuVisibility(true);
        background.setImage(ImageHandler.getMenuBg());
        menu = new Menu(scores, musicPlayer, song, selector, levels, this);
    }

    public void showGame() {
        updateMenuVisibility(false);
        updateGameVisibility(true);
        background.setImage(ImageHandler.getNormalBg());
        top.setText(menu.getTopScore() + "");
        game = new Game(pane, generateUpdateAction(), startLevel, this);
        initStats();
    }

    private ITextUpdateAction generateUpdateAction() {
        return new ITextUpdateAction() {
            @Override
            public void updateStatistic(Shape shape) {
                switch (shape) {
                    case I:
                        updateStat(0);
                        break;
                    case J:
                        updateStat(1);
                        break;
                    case L:
                        updateStat(4);
                        break;
                    case O:
                        updateStat(5);
                        break;
                    case S:
                        updateStat(6);
                        break;
                    case T:
                        updateStat(2);
                        break;
                    case Z:
                        updateStat(3);
                        break;
                }
            }

            @Override
            public void resetStatistics() {
                menu.addHighScore(Long.parseLong(score.getText()));
                for (Node l : stats.getChildren()) {
                    ((Label)l).setText("000");
                }
                top.setText(getFormattedScoreCount(menu.getTopScore()));
                score.setText(getFormattedScoreCount(0));
                for (IShape shape : statShapes)
                    pane.getChildren().removeAll(shape.getTiles());
                updateLevel(0);
                updateClears(0, false);
            }

            private void updateStat(int id) {
                Label l = (Label) stats.getChildren().get(id);
                l.setText(getFormattedLineCount(Integer.parseInt(l.getText()) + 1));
            }

            @Override
            public void updateClears(int lines, boolean tetris) {
                if (tetris) game.tetris();
                clears.setText("LINES  -  " + getFormattedLineCount(lines));
            }

            private String getFormattedLineCount(int lines) {
                if (lines < 10) return "00" + lines;
                else if (lines < 100) return "0" + lines;
                else if (lines < 1000) return "" + lines;
                else return "999";
            }

            @Override
            public void updateScore(int points) {
                long currentScore = Long.parseLong(score.getText());
                long topScore = Long.parseLong(top.getText());
                currentScore += points;
                String stringScore = getFormattedScoreCount(currentScore);
                if (currentScore > topScore)
                    top.setText(stringScore);
                score.setText(stringScore);
            }

            private String getFormattedScoreCount(long score) {
                if (score < 10) return "00000" + score;
                else if (score < 100) return "0000" + score;
                else if (score < 1000) return "000" + score;
                else if (score < 10000) return "00" + score;
                else if (score < 100000) return "0" + score;
                else return "" + score;
            }

            @Override
            public void updateLevel(int newLevel) {
                for (BaseShape shape : statShapes) {
                    for (BaseTile tile : shape.getTiles()) {
                        tile.updateFill(newLevel);
                        tile.applyUpdate();
                    }
                }
                level.setText(newLevel + "");
            }

            @Override
            public void updateBackground(Image fill) {
                background.setImage(fill);
            }
        };
    }

    private void initStats() {
        statShapes = new BaseShape[7];
        BaseShape shape = new StatI(startLevel);
        statShapes[0] = shape;
        pane.getChildren().addAll(shape.getTiles());
        shape = new StatJ(startLevel);
        statShapes[1] = shape;
        pane.getChildren().addAll(shape.getTiles());
        shape = new StatL(startLevel);
        statShapes[2] = shape;
        pane.getChildren().addAll(shape.getTiles());
        shape = new StatO(startLevel);
        statShapes[3] = shape;
        pane.getChildren().addAll(shape.getTiles());
        shape = new StatS(startLevel);
        statShapes[4] = shape;
        pane.getChildren().addAll(shape.getTiles());
        shape = new StatT(startLevel);
        statShapes[5] = shape;
        pane.getChildren().addAll(shape.getTiles());
        shape = new StatZ(startLevel);
        statShapes[6] = shape;
        pane.getChildren().addAll(shape.getTiles());
        updateShapes();
    }

    private void updateShapes() {
        for (BaseShape shape : statShapes) {
            for (BaseTile tile : shape.getTiles()) {
                tile.applyUpdate();
            }
        }
    }

    private void updateMenuVisibility(boolean visible) {
        menuItemPane.setVisible(visible);
    }

    private void updateGameVisibility(boolean visible) {
        gameItemPane.setVisible(visible);
        stats.setVisible(visible);
    }

    private void nextFrame() {
        switch (state) {
            case ANIMATION:
                if (--timelineStateChangeCycleCount == 0)
                    state = ControllerState.MENU;
                animation.nextFrame();
                break;
            case MENU:
                MusicPlayer.getInstance();
                menu.nextFrame();
                break;
            case PLAYING:
                MusicPlayer.getInstance();
                game.nextFrame();
                break;
        }
    }
}
