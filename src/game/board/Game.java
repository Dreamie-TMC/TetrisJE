package game.board;

import audio.MusicPlayer;
import controllers.MainController;
import enums.Input;
import enums.states.ControllerState;
import enums.states.GameState;
import helpers.IInputBuffer;
import helpers.functional.ITextUpdateAction;
import helpers.ImageHandler;
import helpers.InputBuffer;
import javafx.scene.layout.Pane;

public class Game implements IGame {
    private IBoard board;
    private IInputBuffer buffer;
    private GameState state;
    private int frameWait;
    private int consecutiveDownFrames;
    private ITextUpdateAction uiCallback;
    private boolean tetris;
    private boolean firstLevel;
    private boolean queuedLevelUp;
    private boolean firstPiece;
    private int level;
    private int previousLevelLine;
    private MainController controller;

    public Game(Pane pane, ITextUpdateAction uiCallback, int level, MainController controller) {
        board = new Board(level, pane, this, uiCallback);
        uiCallback.updateBackground(ImageHandler.getNormalBg());
        this.level = level;
        previousLevelLine = 0;
        this.uiCallback = uiCallback;
        consecutiveDownFrames = 0;
        firstLevel = true;
        buffer = new InputBuffer();
        frameWait = 0;
        tetris = false;
        queuedLevelUp = false;
        board.updateActiveAndNext();
        firstPiece = true;
        state = GameState.PLAYING;
        this.controller = controller;
    }

    @Override
    public void nextFrame() {
        MusicPlayer.getInstance();
        switch (state) {
            case WAIT:
                if (!waitActions()) break;
            case PLAYING:
                gameLoop();
                break;
            case PAUSED:
                if (buffer.isPauseAndConsume()) {
                    board.pause(true);
                    state = GameState.PLAYING;
                }
                break;
            case ENDING:
                if (buffer.isPauseAndConsume()) {
                    board.clear();
                    uiCallback.resetStatistics();
                    MusicPlayer.getInstance().pause();
                    controller.showMainMenu();
                    controller.updateState(ControllerState.MENU);
                }
        }
        board.updateBoard();
        board.updateActiveAndNext();
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
    public void applyFrameWait(int frames) {
        frameWait = frames;
        state = GameState.WAIT;
    }

    @Override
    public void tetris() {
        tetris = true;
    }

    @Override
    public void endGame() {
        state = GameState.ENDING;
    }

    private void moveDown() {
        boolean fullLine = false;
        if (buffer.isDown()) {
            firstPiece = false;
            fullLine = board.moveDown();
            ++consecutiveDownFrames;
        }
        else if ((board.advanceFallCounter() && !firstPiece) || (firstPiece && board.getDownCounter() == 60)) {
            firstPiece = false;
            fullLine = board.moveDown();
            consecutiveDownFrames = 0;
        }
        if (fullLine) {
            uiCallback.updateScore(consecutiveDownFrames);
            int clears = board.clearFullLines();
            determineLevelUp(clears);
        }
    }

    private void determineLevelUp(int clears) {
        if (firstLevel) {
            if (level < 9 && clears >= level * 10 + 10) {
                firstLevel = false;
                queuedLevelUp = true;
            } else if (clears >= Math.max(100, level * 10 - 50)) {
                firstLevel = false;
                queuedLevelUp = true;
            }
            previousLevelLine = clears;
            while (previousLevelLine % 10 != 0)
                --previousLevelLine;
        } else if (clears >= previousLevelLine + 10) {
            queuedLevelUp = true;
            previousLevelLine = clears;
            while (previousLevelLine % 10 != 0)
                --previousLevelLine;

        }
    }

    private boolean waitActions() {
        if (--frameWait != 0) {
            if (!tetris) return false;
            if (frameWait % 4 == 0)
                uiCallback.updateBackground(ImageHandler.getTetrisBg());
            else
                uiCallback.updateBackground(ImageHandler.getNormalBg());
            return false;
        }
        if (queuedLevelUp) {
            board.levelUp();
            uiCallback.updateLevel(++level);
            queuedLevelUp = false;
        }

        state = GameState.PLAYING;
        board.loadActiveAndNext();
        consecutiveDownFrames = 0;
        tetris = false;
        return true;
    }

    private void gameLoop() {
        if (buffer.isPauseAndConsume()) {
            board.pause(false);
            state = GameState.PAUSED;
            return;
        }
        if (buffer.isLeft()) board.moveLeft();
        else if (buffer.isRight()) board.moveRight();
        else board.clearSideMove();
        if (buffer.isLeftRotate()) board.rotateLeft();
        else if (buffer.isRightRotate()) board.rotateRight();
        else board.clearRotation();
        moveDown();
    }
}
