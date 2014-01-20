package com.mtechcomm.mtechword.Views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.mtechcomm.mtechword.board.Board;

/**
 * Created by Peter on 9/20/13.
 */
public class BoardView extends View implements View.OnTouchListener {


    private Board board;
    public BoardView.LetterTouchedHandler _letterTouchedHandler = null;

    public abstract class LetterTouchedHandler {
        public abstract void handleLetterTouched(int index);
    }

    // Initialise some colours
    private static final int backgroundColor = 0x00FFFFFF;
    private static final int gridColor = Color.rgb(166, 51, 83);
    private static final int centerBackgroundColor = 0x80000000;
    private static final int centerLetterColor = Color.WHITE;
    private static final int letterColor = Color.BLACK;
    private static final int letterHighlightColor = 0x90FFFF00;
    private static final int middleHighlightColor = 0x90BFBF00;

    // Set paint objects
    private Paint backgroundPaint;        // Overall background
    private Paint gridPaint;            // Grid lines
    private Paint letterPaint;            // Outside letters
    private Paint centerLetterPaint;    // Center letter
    private Paint centerPaint;            // Center background
    private Paint highlightPaint;        // Square highlight
    private Paint middleHighlightPaint; // Middle square's highlight
    private int currentWidth; // Current width of view

    // An array to indicate which letters are displayed as highlighted
    private boolean[] highlights;

    // Array of grid indices of selected letters
    private int[] selectedWord;

    public BoardView(Context context) {
        super(context);
        board = null;
        initGameBoardView();
    }

    public BoardView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        board = null;
        initGameBoardView();
    }

    private void initGameBoardView() {
        this.gridPaint = new Paint();
        this.gridPaint.setColor(gridColor);
        this.gridPaint.setAntiAlias(true);

        this.backgroundPaint = new Paint();
        this.backgroundPaint.setColor(backgroundColor);
        this.backgroundPaint.setAntiAlias(true);

        this.letterPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.letterPaint.setColor(letterColor);
        this.letterPaint.setTypeface(Typeface.create(Typeface.MONOSPACE, Typeface.BOLD));
        this.letterPaint.setTextAlign(Paint.Align.CENTER);

        this.centerPaint = new Paint();
        this.centerPaint.setColor(centerBackgroundColor);

        this.centerLetterPaint = new Paint();
        this.centerLetterPaint.setColor(centerLetterColor);
        this.centerLetterPaint.setTypeface(Typeface.create(Typeface.MONOSPACE, Typeface.BOLD));
        this.centerLetterPaint.setAntiAlias(true);

        this.highlightPaint = new Paint();
        this.highlightPaint.setColor(letterHighlightColor);

        this.middleHighlightPaint = new Paint();
        this.middleHighlightPaint.setColor(middleHighlightColor);

        this.currentWidth = 0;

        this.setOnTouchListener((OnTouchListener) this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Our target grid is a square, measuring 80% of the minimum dimension
        int measuredWidth = measure(widthMeasureSpec);
        int measuredHeight = measure(heightMeasureSpec);

        int dim = Math.min(measuredWidth, measuredHeight);

        setMeasuredDimension(dim, dim);
    }

    private int measure(int measureSpec) {

        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.UNSPECIFIED)
            return 180;
        else
            return (int) (specSize * 0.8);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int boardWidth = getWidth();
        int boardHeight = getHeight();

        if (board != null) {
            int cellWidth = getBoardCellWidth();
            int cellHeight = getBoardCellHeight();
            if (boardWidth != this.currentWidth) {
                this.centerLetterPaint.setTextSize((int) (boardWidth / 5.5));
                this.letterPaint.setTextSize((int) (boardWidth / 5.5));
                this.gridPaint.setStrokeWidth(boardWidth / 50 + 1);
                this.currentWidth = boardWidth;
            }

            canvas.drawARGB(0, 255, 255, 255);


            for (int column = 0; column < board.getSize(); column++)
                drawLetter(canvas, column, this.highlights[column]);


            for (float x = 0; x <= boardWidth; x += getBoardCellWidth())
                canvas.drawLine(x, 0, x, boardHeight - 1, this.gridPaint);

            for (float y = 0; y <= boardHeight; y += getBoardCellHeight())
                canvas.drawLine(0, y, boardWidth - 1, y, this.gridPaint);
        }
    }

    // Draws a single letter in the grid, with appropriate highlight
    protected void drawLetter(Canvas canvas, int index, boolean highlighted) {
        Paint textPaint;
        Paint squarePaint;

        int size = getWidth(); // Measure one as its a square
        int cellWidth = getBoardCellWidth();
        int cellHeight = getBoardCellHeight();

        int row = index / 3;
        int col = index % 3;

        int left = col * cellWidth;
        int top = row * cellHeight;
        int right = left + cellWidth;
        int bottom = top + cellHeight;

        String letter = this.board.elementAt(index);

        float squareLeft = (index % 3) * size / 3;
        float squareTop = (float) Math.floor(index / 3) * size / 3;
        float squareSize = cellWidth;
        float letterWidth = this.letterPaint.measureText(letter);
        float letterHeight = this.letterPaint.ascent();

        if (!highlighted) {
            textPaint = this.letterPaint;
            squarePaint = this.backgroundPaint;

        } else {
            squarePaint = this.highlightPaint;
            textPaint = this.letterPaint;
        }

          /* canvas.drawRect(squareLeft, squareTop,squareLeft + squareSize, bottom, squarePaint);*/
        canvas.drawRect(left, top, right, bottom, squarePaint);
        canvas.drawText(letter, squareLeft + squareSize / 2 - letterWidth / 2, squareTop + squareSize / 2 - letterHeight / 2, textPaint);
    }

    private int getBoardCellHeight() {
        int height = 0;
        switch (board.getSize()) {
            case 3:
                height = getHeight();
                break;
            case 6:
                height = getHeight() / 2;
                break;
        }
        return height;
    }

    private int getBoardCellWidth() {
        return getWidth() / 3;
    }

    // Supplies a new word to the grid.
    // Arg must be a 9 letter string, filling the grid L-R, T-B
    public void setLetters(String word) {
        board.setBoard(word);
        //board.rotate();
        clearGrid(); // Calls invalidate() for us.

    }

    // Clears (unhighlights) the most recently selected letter from the grid.
    public void clearLastLetter() {
        int gridIndex;
        for (int i = board.getSize() - 1; i >= 0; i--) {
            gridIndex = this.selectedWord[i];
            if (gridIndex != -1) {
                this.highlights[gridIndex] = false;
                this.selectedWord[i] = -1;
                invalidate();
                return;
            }
        }
    }

    // Unhighlights the entire grid
    public void clearGrid() {
        java.util.Arrays.fill(highlights, false);
        java.util.Arrays.fill(selectedWord, -1);
        invalidate();
    }

    // Returns the string of the currently tapped out word
    public String getSelectedWord() {
        String word = "";
        int gridIndex;
        for (int i = 0; i < board.getSize(); i++) {
            gridIndex = this.selectedWord[i];
            if (gridIndex > -1)
                word += this.board.elementAt(gridIndex);
            else
                return word;
        }

        return word;
    }

    public void setLetterTouchedListener(LetterTouchedHandler handler) {
        this._letterTouchedHandler = handler;
    }

    // Handles touch events to the grid.
    //
    // Marks the letter to be highlighted, and updates the
    // ordered list of selected letters.
    //
    // Finally calls the LetterTouchedHandler for further actions.
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                int gridIndex = eventToLetterIndex(event);
                if (this.highlights[gridIndex]) {
                    unSelectIfLastLetter(gridIndex);
                    return true; // return if letter already highlighted
                }
                this.highlights[gridIndex] = true;
                invalidate();
                for (int i = 0; i < board.getSize(); i++) {
                    if (this.selectedWord[i] == -1) {
                        this.selectedWord[i] = gridIndex;
                        break;
                    }
                }
                if (this._letterTouchedHandler != null)
                    this._letterTouchedHandler.handleLetterTouched(gridIndex);
                return true;
            }
        }
        return true;
    }

    private void unSelectIfLastLetter(int gridIndex) {
        int lastIndex = -2;
        int i;
        for (i = 0; i < board.getSize(); i++) {
            if (this.selectedWord[i] == -1) {
                lastIndex = i - 1;
                break;
            }
        }
        if (lastIndex == -2 && i == board.getSize())
            lastIndex = (board.getSize() == 3) ? 2 : 5;

        if (this.selectedWord[lastIndex] == gridIndex) {
            clearLastLetter();
            this._letterTouchedHandler.handleLetterTouched(gridIndex);
        }
    }

    // Takes an onTouch event and returns the grid index of the touched letter.
    private int eventToLetterIndex(MotionEvent event) {
        int x = (int) event.getX() / getBoardCellWidth();
        int y = (int) event.getY() / getBoardCellHeight();
        int index = x + 3 * y;

        // Log.d("Target", "Row " + row + ", col " + col + ", index " + index);
        return index;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
        highlights = new boolean[board.getSize()];
        selectedWord = new int[board.getSize()];
        java.util.Arrays.fill(highlights, false);
        java.util.Arrays.fill(selectedWord, -1);
        invalidate();
    }
}



