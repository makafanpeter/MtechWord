package com.mtechcomm.mtechword.board;

/**
 * Created by Peter on 9/20/13.
 */
public class ThreeWordsBoard extends Board {
    private final int SIZE = 3;
    private final int WIDTH = 3;

    public ThreeWordsBoard(String b) {
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
        return 1;
    }
}
