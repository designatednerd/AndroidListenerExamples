package com.designatednerd.androidlistenerexamples.test;

import com.designatednerd.androidlistenerexamples.model.CommonLetterFinder;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;

import static org.junit.runners.Parameterized.Parameters;
import static org.junit.Assert.assertEquals;

/**
 * Created by ellen on 5/19/15.
 */
@RunWith(Parameterized.class)
public class ParameterTestExample {

    @Parameters
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][] {
                { "cat", "rat", "at" }, //Returns in order
                { "dog", "pug", "g"}, //Single letter
                { "bob", "steve", null }, //No letters
                { "mary", "roger", "r"}, //Many of one letter
                { "ellen", "lilia", "l" }, //Many of the same letter
                { null, "wat", null }, //Null first param
                { "rly", null, null } //Null second param
        });
    }

    /**
     * Create your object and the things you're passing it.
     */
    private String              mFirstString;
    private String              mSecondString;
    private String              mExpectedCommonLetters;
    private CommonLetterFinder  mLetterFinder;

    public ParameterTestExample(String aFirstString, String aSecondString, String aCommonLetters) {
        mFirstString = aFirstString;
        mSecondString = aSecondString;
        mExpectedCommonLetters = aCommonLetters;
        mLetterFinder = new CommonLetterFinder();
    }

    /**
     * Since this is being run with Parameterized, it will be run the number of times
     */
    @Test
    public void findCommonLetters() {
        String foundCommonLetters = mLetterFinder.commonLetters(mFirstString, mSecondString);
        assertEquals(mExpectedCommonLetters, foundCommonLetters);
    }
}
