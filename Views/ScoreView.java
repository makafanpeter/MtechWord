package com.mtechcomm.mtechword.Views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Peter on 9/20/13.
 */
public class ScoreView extends View {
    private enum BoxColor {WHITE, YELLOW}

    ;
    private String mText = "0";


    public ScoreView(Context context, AttributeSet attrs) {
        super(context, attrs);


    }

    private void drawTile(Canvas canvas, Paint p, String letter, BoxColor color, int x, int y, int size) {

        switch (color) {
            case WHITE:
                p.setARGB(255, 255, 255, 255);
                break;
            case YELLOW:
                //FFBB33
                p.setARGB(45, 255, 187, 51);
                break;
        }

        // draw background
        canvas.drawRect(x, y, x + size, y + size, p);

        // draw border
        p.setARGB(255, 0, 0, 0);
        canvas.drawLine(x, y, x + size, y, p);
        canvas.drawLine(x, y, x, y + size, p);
        canvas.drawLine(x + size, y, x + size, y + size, p);
        canvas.drawLine(x, y + size, x + size, y + size, p);

        canvas.drawText(letter, x + size / 2, y + (size * 8 / 10), p);

    }

    @Override
    public void onDraw(Canvas canvas) {

        drawOnCanvas(canvas);


    }

    private void drawOnCanvas(Canvas canvas) {
        Paint p = new Paint();
        p.setAntiAlias(true);
        p.setTypeface(Typeface.MONOSPACE);
        p.setTextAlign(Paint.Align.CENTER);

        int height = getHeight();
        int width = getWidth();

        int size = Math.max(height, width) / 4;
        p.setTextSize(size * 8 / 10);

        int y = (height - size) / 2;
        int dx = (width - 10 - 5 * size) / 4 + size;
        int x = 5;
        char wordToArray[];
        wordToArray = mText.toCharArray();
        for (int index = 0; index < mText.length(); index++) {
            drawTile(canvas, p, wordToArray[index] + "", BoxColor.YELLOW, x, y, size);
            x += (1.8 * dx);
        }

    }

    public String getText() {
        return mText;
    }

    public void setText(String mText) {
        this.mText = mText;
        invalidate();
    }
}
