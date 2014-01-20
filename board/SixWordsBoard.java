package com.mtechcomm.mtechword.board;

/**
 * Created by Peter on 9/26/13.
 */
public class SixWordsBoard extends Board {


    private final int SIZE = 6;
    private final int WIDTH = 3;

    public SixWordsBoard(String b) {
        super(b);

    }

    @Override
    public int getWidth() {
        return WIDTH;
    }

    @Override
    public int getSize() {
        return SIZE;
    }

    @Override
    public int getNumberOfRows() {
        return 2;
    }
}
