package com.mtechcomm.mtechword;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

/**
 * Word Game Start Activity
 *
 * @author Peter
 */
public class Start extends Activity implements View.OnClickListener {

    private Button mStart;
    private Button mHighScore;
    private Button mHowToPlay;
    private Button mSettings;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_word);
        mStart = (Button) findViewById(R.id.start);
        mHowToPlay = (Button) findViewById(R.id.howToPlay);
        mHighScore = (Button) findViewById(R.id.highScore);
        mSettings = (Button) findViewById(R.id.settings);
        mStart.setOnClickListener(this);
        mHowToPlay.setOnClickListener(this);
        mHighScore.setOnClickListener(this);
        mSettings.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        if (view.getId() == R.id.start) {
            intent = new Intent(this, SplashScreen.class);
            startActivity(intent);
        } else if (view.getId() == R.id.howToPlay) {
            //intent = new Intent(this, HowToPlay.class);
            //startActivity(intent);
            openHowToPlayDialog();
        } else if (view.getId() == R.id.highScore) {
            /*intent = new Intent(this, HighScores.class);
            startActivity(intent);*/
            openHighScoresDialog();
        } else if (view.getId() == R.id.settings) {
            startActivityForResult(new Intent(this, GameConfig.class), 0);
        }
    }


    private void openHowToPlayDialog() {
        LayoutInflater li = LayoutInflater.from(this);
        View view = li.inflate(R.layout.activity_howtoplay, null);
        new AlertDialog.Builder(Start.this)
                .setTitle("How to play")
                .setIcon(R.drawable.howto)
                .setView(view)
                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //
                    }
                })
                .show();

    }

    private void openHighScoresDialog() {

        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View highScoreView = layoutInflater.inflate(R.layout.activity_highscore, null);
        TextView textView = (TextView) highScoreView.findViewById(R.id.high_scores_list);
        SharedPreferences scorePrefs = getSharedPreferences(Game.GAME_PREF, 0);
        String[] savedScores = scorePrefs.getString("highScores", "").split("\\|");
        int counter = 1;
        StringBuilder scoreBuild = new StringBuilder("");
        for (String score : savedScores) {
            scoreBuild.append(counter + ". " + score + "\n");
            counter++;
        }
        textView.setText(scoreBuild.toString());
        new AlertDialog.Builder(Start.this)
                .setTitle("High scores")
                .setIcon(R.drawable.about)
                .setView(highScoreView)
                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //
                    }
                })
                .show();

    }
}
