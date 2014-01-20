package com.mtechcomm.mtechword.board;

/**
 * Created by Peter on 9/20/13.
 */
public abstract class Board {
    private String[] board;

    public Board(String b) {
        setBoard(b);
    }

    public String[] getBoard() {
        return board;
    }

    public void setBoard(String board) {
        this.board = new String[this.getSize()];
        for (int index = 0; index < this.getSize(); index++) {
            this.board[index] = board.substring(index, index + 1);
        }

    }

    public synchronized String elementAt(int i) {
        return board[i];
    }

    public synchronized String elementAt(int x, int y) {
        return board[x + getWidth() * y];
    }

    public synchronized String toString() {
        StringBuilder sb = new StringBuilder();
        int size = getSize();
        for (int i = 0; i < size - 1; i++) {
            sb.append(board[i]);
            sb.append(",");
        }
        sb.append(board[size - 1]);

        return sb.toString();
    }


    public synchronized void rotate() {
        String[] newbrd = new String[getSize()];

        int w = getWidth();
        int maxX = w - 1;

        for (int i = 0; i < getSize(); i++) {
            newbrd[w * (i % w) + ((w - 1) - (i / w))] = board[i];
        }

        board = newbrd;
    }

    public abstract int getWidth();

    public abstract int getSize();

    public abstract int getNumberOfRows();

}
