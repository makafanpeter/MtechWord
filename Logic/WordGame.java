package com.mtechcomm.mtechword.Logic;

/**
 * Created by Peter on 9/10/13.
 */

import android.content.Context;
import android.content.res.AssetManager;

import com.mtechcomm.mtechword.trie.Trie;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * @author Peter
 */
public class WordGame {

    private static Trie trie;
    private String word;
    private String scrambleWord;
    private List<String> randomWords = new LinkedList<String>();
    private Random random;


    public WordGame(int lenght, Context context) {
        trie = new Trie();
        setRandomWords(buildTrie(lenght, context));
        newWord();
    }

    /**
     * @return the trie
     */
    public static Trie getTrie() {
        return trie;
    }

    /**
     * @param aTrie the trie to set
     */
    public static void setTrie(Trie aTrie) {
        trie = aTrie;
    }

    private static List<String> buildTrie(int length, Context context) {
        AssetManager assetManager = context.getAssets();
        InputStream dictionary = null;
        try {
            dictionary = assetManager.open("wordlist.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<String> tempRandomWords = new LinkedList<String>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(dictionary));
            String line = null;
            while ((line = reader.readLine()) != null) {
                String tempWord = line.trim().toLowerCase();
                if (tempWord.length() == length) {
                    tempRandomWords.add(tempWord);
                }
                trie.insertWord(tempWord);
            }
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                }
            }
        }
        return tempRandomWords;
    }

    /**
     * @return the word
     */
    public String getWord() {
        return word;
    }

    /**
     * @param word the word to set
     */
    public void setWord(String word) {
        this.word = word;
    }

    /**
     * @return the scrambleWord
     */
    public String getScrambleWord() {
        return scrambleWord;
    }

    /**
     * @param scrambleWord the scrambleWord to set
     */
    public void setScrambleWord(String scrambleWord) {
        this.scrambleWord = scrambleWord;
    }

    /**
     * @return the randomWords
     */
    public List<String> getRandomWords() {
        return randomWords;
    }

    /**
     * @param randomWords the randomWords to set
     */
    public final void setRandomWords(List<String> randomWords) {
        this.randomWords = randomWords;
    }

    /**
     * Return a random word
     *
     * @return String
     */
    public String getRandomWord() {
        String myWord;
        random = new Random();
        myWord = getRandomWords().get(
                random.nextInt(getRandomWords().size() - 1));
        return myWord;
    }

    public String generateScrambledWord(String word) {
        String myWord;
        random = new Random();
        int firstPosition;
        int secondPosition;
        myWord = getWord();
        char[] scrambledLetters;
        scrambledLetters = new char[myWord.length()];
        scrambledLetters = myWord.toCharArray();

        for (int i = 0; i < word.length(); i++) {
            firstPosition = random.nextInt(word.length() - 1);
            secondPosition = random.nextInt(word.length() - 1);
            char temp = scrambledLetters[firstPosition];
            scrambledLetters[firstPosition] = scrambledLetters[secondPosition];
            scrambledLetters[secondPosition] = temp;
        }
        return new String(scrambledLetters);

    }

    public boolean checkWord(String word) {
        if (getTrie().searchWord(word)) {
            return true;
        } else {
            return false;
        }
    }

    public final void newWord() {
        this.setWord(this.getRandomWord());
        this.setScrambleWord(this.generateScrambledWord(this.getWord()));
    }
}
