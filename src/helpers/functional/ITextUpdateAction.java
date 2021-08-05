package helpers.functional;

import enums.Shape;
import javafx.scene.image.Image;

public interface ITextUpdateAction {
    void updateStatistic(Shape shape);

    void resetStatistics();

    void updateClears(int lines, boolean tetris);

    /**
     * This method should be used to handle updating the score. This method should take in incrementation in points
     * rather than the entire score
     * @param points The amount of points to add to the score
     */
    void updateScore(int points);

    void updateLevel(int newLevel);

    void updateBackground(Image fill);
}
