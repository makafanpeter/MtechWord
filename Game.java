package com.mtechcomm.mtechword;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mtechcomm.mtechword.Logic.WordGame;
import com.mtechcomm.mtechword.Utils.Score;
import com.mtechcomm.mtechword.Views.BoardView;
import com.mtechcomm.mtechword.Views.ScoreView;
import com.mtechcomm.mtechword.board.Board;
import com.mtechcomm.mtechword.board.SixWordsBoard;
import com.mtechcomm.mtechword.board.ThreeWordsBoard;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Game extends Activity implements OnClickListener {

    public static final String GAME_PREF = "WordGameFile";
    private int lengthOfWords;
    private final long startTime = 150 * 1000;
    private final long interval = 1 * 1000;
    private WordGame mGame;
    private int score = 0;
    private Button mNewWord;
    private Button mClear;
    private Button mCheck;
    private String guessedWord;
    private ScoreView mScore;
    private TextView mWord;
    private TextView mTimer;
    private boolean isGameOn;
    private CountDownTimer countDownTimer;
    private Animation shakeAnimation;
    private BoardView playGrid;
    private Board board;
    /**
     * Shared preference
     */
    private SharedPreferences gamePrefs;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.game);
        gamePrefs = getSharedPreferences(GAME_PREF, 0);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        playGrid = (BoardView) findViewById(R.id.play_grid);
        mClear = (Button) findViewById(R.id.clear);
        mCheck = (Button) findViewById(R.id.check);
        mNewWord = (Button) this.findViewById(R.id.newWord);
        mTimer = (TextView) findViewById(R.id.timer);
        mClear.setOnClickListener(this);
        mCheck.setOnClickListener(this);
        mNewWord.setOnClickListener(this);
        mWord = (TextView) this.findViewById(R.id.word);
        mTimer = (TextView) this.findViewById(R.id.timer);
        //mTimer.setTypeface(Typeface.createFromAsset(getAssets(), "DigitalDream.ttf"));
        mScore = (ScoreView) this.findViewById(R.id.ScoreLabel);
        setLengthOfWords(Integer.parseInt(prefs.getString("wordLength", "3")));
        shakeAnimation = AnimationUtils.loadAnimation(this, R.anim.incorrect_shake);
        shakeAnimation.setRepeatCount(3); // animation repeats 3 times
        int seconds = (int) (startTime / 1000);
        int minutes = seconds / 60;
        seconds = seconds % 60;
        countDownTimer = new MyCountDownTimer(startTime, interval);
        mTimer.setText(mTimer.getText()
                + String.valueOf("" + minutes + ":0" + seconds));
        startNewGame();
        isGameOn = true;
    }

    @Override
    protected void onStop() {
        countDownTimer.cancel();
        isGameOn = false;
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (isGameOn) {
            startNewGame();
        }
    }

    public void clear() {
        mWord.setText(null);
        playGrid.clearGrid();

    }

    private void guessWord() {
        guessedWord = (String) mWord.getText();
        if (mGame.checkWord(guessedWord)) {
            Toast results = Toast.makeText(this, "+" + IncrementScore(guessedWord) + " pts", Toast.LENGTH_SHORT);
            results.setGravity(Gravity.TOP, results.getXOffset() / 2, results.getYOffset() / 2);
            results.show();
            mScore.setText(Integer.toString(getScore()));
        } else {
            mWord.startAnimation(shakeAnimation);
            Toast results = Toast.makeText(this, "Try " + mGame.getWord() + " Next Time", Toast.LENGTH_SHORT);
            results.setGravity(Gravity.TOP, results.getXOffset() / 2, results.getYOffset() / 2);
            results.show();
            mScore.setText(Integer.toString(getScore()));
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_option, menu);
        return true;
    }

    // Handles menu item selections
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.newGame:
                startNewGame();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * @return the score
     */
    public int getScore() {
        return score;
    }

    /**
     * @param score the score to set
     */
    public void setScore(int score) {
        this.score = score;
    }

    public int IncrementScore(String word) {
        int length = word.length();
        int currentPoint = getScore();
        setScore(currentPoint + length);
        return length;
    }

    public int decrementScore(String word) {
        int length = word.length();
        if (getScore() == 0) {
            return 0;
        } else if (length > getScore()) {
            return 0;
        }
        setScore(getScore() - length);
        return length;
    }

    private void startNewGame() {
        switch (getLengthOfWords()) {
            case 3:
                board = new ThreeWordsBoard("wor");
                break;
            case 6:
                board = new SixWordsBoard("wordGam");
        }
        playGrid.setBoard(board);
        this.playGrid.setLetterTouchedListener(this.playGrid.new LetterTouchedHandler() {
            public void handleLetterTouched(int index) {
                mWord.setText(playGrid.getSelectedWord());
            }
        });
        mGame = new WordGame(getLengthOfWords(), this);
        getNewWord();
        setScore(0);
        mScore.setText(Integer.toString(getScore()));
        countDownTimer.start();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.clear) {
            clear();
        } else if (v.getId() == R.id.newWord) {
            getNewWord();
        } else if (v.getId() == R.id.check) {
            if (mWord.getText().toString().length() != 0) {
                guessWord();
                getNewWord();
            } else {
                Toast results = Toast.makeText(this, getString(R.string.tryAWord), Toast.LENGTH_SHORT);
                results.setGravity(Gravity.TOP, results.getXOffset() / 2, results.getYOffset() / 2);
                results.show();
            }
        }

    }

   /* private void submitGuess(Button mBoardButtons) {
        String temp;
        temp = (String) mWord.getText();
        temp += mBoardButtons.getText();
        mWord.setText(temp);
        mBoardButtons.setEnabled(false);
    }*/

    private void getNewWord() {
        mWord.setText(null);
        mGame.newWord();
        playGrid.setLetters(mGame.getScrambleWord());


        /*String temp;
        for (int row = 0; row < getButtonLayOut().getChildCount(); ++row)
            ((TableRow) getButtonLayOut().getChildAt(row)).removeAllViews();

        // get a reference to the LayoutInflater service
        LayoutInflater inflater = (LayoutInflater) getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);

        // add 3, 6, or 9 answer Buttons based on the value of guessRows
        int guessRows = getGuessRows();
        for (int row = 0; row < guessRows; row++) {
            TableRow currentTableRow = getTableRow(row);
            // place Buttons in currentTableRow
            for (int column = 0; column < 3; column++) {
                Button mBoardButtons = (Button) inflater.inflate(R.layout.keypad, null);
                temp = new String(wordToArray[(row * 3) + column] + "");
                mBoardButtons.setText(temp);
                mBoardButtons.setEnabled(true);
                mBoardButtons.setOnClickListener(this);
                currentTableRow.addView(mBoardButtons);
            }

        }*/
    }

    public void gameOver() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(R.string.game_over); // title bar string

        // set the AlertDialog's message to display game results
        builder.setMessage(String.format("Your Score: %s", getScore()));

        builder.setCancelable(false);

        // add "Reset Quiz" Button                              
        builder.setPositiveButton(R.string.new_game,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        setHighScore();
                        isGameOn = false;
                        mTimer.setTextColor(getResources().getColor(R.color.blackText));
                        startNewGame();
                    } // end method onClick
                } // end anonymous inner class
        ); // end call to setPositiveButton

        // create AlertDialog from the Builder
        AlertDialog resetDialog = builder.create();
        resetDialog.show(); // display the Dialog
    }


    //set high score
    private void setHighScore() {
        int exScore = getScore();
        if (exScore > 0) {
            //we have a valid score
            SharedPreferences.Editor scoreEdit = gamePrefs.edit();
            DateFormat dateForm = new SimpleDateFormat("dd MMMM yyyy");
            String dateOutput = dateForm.format(new Date());
            //get existing scores
            String scores = gamePrefs.getString("highScores", "");
            //check for scores
            if (scores.length() > 0) {
                //we have existing scores
                List<Score> scoreStrings = new ArrayList<Score>();
                //split scores
                String[] exScores = scores.split("\\|");
                //add score object for each
                for (String eSc : exScores) {
                    String[] parts = eSc.split(" - ");
                    scoreStrings.add(new Score(parts[0], Integer.parseInt(parts[1])));
                }
                //new score
                Score newScore = new Score(dateOutput, exScore);
                scoreStrings.add(newScore);
                //sort
                Collections.sort(scoreStrings);
                //get top ten
                StringBuilder scoreBuild = new StringBuilder("");
                for (int s = 0; s < scoreStrings.size(); s++) {
                    if (s >= 10) break;
                    if (s > 0) scoreBuild.append("|");
                    scoreBuild.append(scoreStrings.get(s).getScoreText());
                }
                //write to prefs
                scoreEdit.putString("highScores", scoreBuild.toString());
                scoreEdit.commit();

            } else {
                //no existing scores
                scoreEdit.putString("highScores", "" + dateOutput + " - " + exScore);
                scoreEdit.commit();
            }
        }
    }

    public int getLengthOfWords() {
        return lengthOfWords;
    }

    public void setLengthOfWords(int lengthOfWords) {
        this.lengthOfWords = lengthOfWords;
    }

    /*public int getGuessRows() {
        int guessRows;
        switch (getLengthOfWords()) {
            case 3:
                guessRows = 1;
                break;
            case 4:
                guessRows = 2;
                break;
            case 5:
                guessRows = 2;
                break;
            case 6:
                guessRows = 2;
                break;
            default:
                guessRows = 1;
        }
        return guessRows;
    }*/

    public class MyCountDownTimer extends CountDownTimer {
        private boolean redTimer = false;

        public MyCountDownTimer(long startTime, long interval) {
            super(startTime, interval);
        }

        @Override
        public void onFinish() {
            redTimer = false;//Turn off the timer red text.
            mTimer.setText("0:00");
            gameOver();
        }

        @SuppressLint("ResourceAsColor")
        @Override
        public void onTick(long millisUntilFinished) {
            int seconds = (int) (millisUntilFinished / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;

            //Change the timer Text color Red
            if (minutes == 0 && !redTimer) {

                mTimer.setTextColor(getResources().getColor(R.color.redText));
                redTimer = true;
            }
            if (seconds < 10) {
                mTimer.setText("" + minutes + ":0" + seconds);
            } else {
                mTimer.setText("" + minutes + ":" + seconds);
            }

        }
    }
}
