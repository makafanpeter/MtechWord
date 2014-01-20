package com.mtechcomm.mtechword.Utils;

/**
 * Created by Peter on 8/26/13.
 */
public class Score implements Comparable<Score> {

    private int scoreNumber;
    private String scoreDate;

    public Score(String date, int number) {
        scoreDate = date;
        scoreNumber = number;
    }

    @Override
    public int compareTo(Score score) {
        //Return 0 if equal
        //1 if passed greater than this
        //-1 if passed greater than passed
        return score.scoreNumber > scoreNumber ? 1 : score.scoreNumber < scoreNumber ? -1 : 0;
    }


    public String getScoreText() {
        return scoreDate + " - " + scoreNumber;
    }
}
