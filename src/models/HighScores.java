package models;

public class HighScores {
    private long[] scores;

    public void setScores(long[] scores) {
        this.scores = scores;
    }

    public long getScore(int index) {
        return scores[index];
    }

    public void addScore(long score) {
        for (int i = 0; i < scores.length; ++i) {
            if (score > scores[i]) {
                addScore(score, i);
                return;
            }
        }
    }

    public void addScore(long score, int index) {
        if (scores.length - 1 - index >= 0)
            System.arraycopy(scores, index, scores, index + 1, scores.length - 1 - index);
        scores[index] = score;
    }

    public boolean isTopSixScore(long score) {
        for (long record : scores)
            if (score > record)
                return true;

        return false;
    }

    public static HighScores createDefaultScores() {
        HighScores scores = new HighScores();
        long[] score = new long[6];
        score[0] = 100000;
        score[1] = 75000;
        score[2] = 50000;
        score[3] = 25000;
        score[4] = 10000;
        score[5] = 5000;
        scores.setScores(score);
        return scores;
    }
}
