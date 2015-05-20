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
 *
 * A class to show off how to use parameterized tests with JUnit 4.
 * Note: Needs RunWith annotation since it's using a more specific runner than the standard.
 */
@RunWith(Parameterized.class)
public class ParameterTestExample {

    /*******************
     * PARAMETER SETUP *
     *******************/

    /**
     * Create an iterable of object arrays that can be handed over to the test
     * as parameters to the constructor.
     *
     * For example, here we have a constructor that takes 3 items: The first string,
     * the second string, and the expected overlap string. Each of these arrays
     * of strings will be passed in to the constructor in that order.
     */
    @Parameters
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][] {
                { "cat", "eta", "at" }, //Returns in order of first string
                { "dog", "pug", "g"}, //Single letter
                { "bob", "steve", null }, //No letters
                { "mary", "roger", "r"}, //Many of one letter
                { "ellen", "lilia", "l" }, //Many of the same letter
                { null, "wat", null }, //Null first param
                { "rly", null, null } //Null second param
        });
    }


    /*************
     * VARIABLES *
     *************/

    private String              mFirstString;
    private String              mSecondString;
    private String              mExpectedCommonLetters;
    private CommonLetterFinder  mLetterFinder;

    /***************
     * CONSTRUCTOR *
     ***************/

    /**
     * Test constructor that takes the params defined above.
     * @param aFirstString      The first string to compare.
     * @param aSecondString     The second string to compare.
     * @param aCommonLetters    The expected common letters in the string.
     */
    public ParameterTestExample(String aFirstString, String aSecondString, String aCommonLetters) {
        mFirstString = aFirstString;
        mSecondString = aSecondString;
        mExpectedCommonLetters = aCommonLetters;
        mLetterFinder = new CommonLetterFinder();
    }

    /***********************
     * PARAMETERIZED TESTS *
     ***********************/

    @Test
    public void findCommonLetters() {
        String foundCommonLetters = mLetterFinder.commonLetters(mFirstString, mSecondString);
        assertEquals(mExpectedCommonLetters, foundCommonLetters);
    }

    @Test
    public void findCommonLettersIgnoringCase() {
        String foundCommonLetters = mLetterFinder.commonLettersCaseInsensitive(mFirstString, mSecondString);
        assertEquals(mExpectedCommonLetters, foundCommonLetters);
    }
}
