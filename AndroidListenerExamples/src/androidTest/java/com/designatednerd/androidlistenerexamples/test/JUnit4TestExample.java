package com.designatednerd.androidlistenerexamples.test;

import android.util.Log;

import com.designatednerd.androidlistenerexamples.model.CommonLetterFinder;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

/**
 * Created by ellen on 5/19/15.
 */
public class JUnit4TestExample {

    /*************
     * VARIABLES *
     *************/

    private static final String LOG_TAG = "JUnit4TestExample";

    private CommonLetterFinder mFinder;

    /******************
     * TEST LIFECYCLE *
     ******************/

    @BeforeClass
    public static void beforeClass() {
        Log.d(LOG_TAG, "Before any of the tests in the class begin!");
    }

    @Before
    public void beforeInstance() {
        Log.d(LOG_TAG, "Before each test in the class begins!");
        mFinder = new CommonLetterFinder();
    }

    @After
    public void afterInstance() {
        Log.d(LOG_TAG, "After each test in the class ends!");
        mFinder = null;
    }

    @AfterClass
    public static void afterClass() {
        Log.d(LOG_TAG, "After all the tests in the class end!");
    }

    /*********
     * TESTS *
     *********/

    @Test
    public void lettersReturnedInOrderOfFirstWord() {
        String found = mFinder.commonLetters("cat", "eta");
        assertEquals("at", found);
    }

    @Test
    public void singleLetterFound() {
        String found = mFinder.commonLetters("dog", "pug");
        assertEquals("g", found);
    }

    @Test
    public void noOverlappingLettersReturnsNull() {
        String found = mFinder.commonLetters("bob", "steve");
        assertNull(found);
    }

    @Test
    public void manyInSecondOfOneInFirstReturnsOneLetter() {
        String found = mFinder.commonLetters("mary", "roger");
        assertEquals("r", found);
    }

    @Test
    public void manyInSecondOfManyInFirstReturnsOneLetter() {
        String found = mFinder.commonLetters("ellen", "lilia");
        assertEquals("l", found);
    }

    @Test
    public void nullFirstParamReturnsNull() {
        String found = mFinder.commonLetters(null, "wat");
        assertNull(found);
    }

    @Test
    public void nullSecondParamReturnsNull() {
        String found = mFinder.commonLetters("ohai", null);
        assertNull(found);
    }

    @Ignore("Oh wait, I need to implement removal of spaces")
    @Test
    public void superLongStringsDontIncludeSpacesInOverlap() {
        String found = mFinder.commonLetters("i am the very model of a modern major general",
                "i've got the horse right here, his name is paul revere");
        assertFalse(found.contains(" "));
    }
}
