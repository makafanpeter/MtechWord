package com.mtechcomm.mtechword;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by Peter on 8/29/13.
 */
public class HighScores extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore);
        TextView textView = (TextView) findViewById(R.id.high_scores_list);
        SharedPreferences scorePrefs = getSharedPreferences(Game.GAME_PREF, 0);
        String[] savedScores = scorePrefs.getString("highScores", "").split("\\|");
        int counter = 1;
        StringBuilder scoreBuild = new StringBuilder("");
        for (String score : savedScores) {
            scoreBuild.append(counter + ". " + score + "\n");
            counter++;
        }
        textView.setText(scoreBuild.toString());
    }
}