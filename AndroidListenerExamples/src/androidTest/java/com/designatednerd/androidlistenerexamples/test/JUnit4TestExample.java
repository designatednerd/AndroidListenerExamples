package com.designatednerd.androidlistenerexamples.test;

import com.designatednerd.androidlistenerexamples.model.CommonLetterFinder;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

/**
 * Created by ellen on 5/19/15.
 */
public class JUnit4TestExample {

    private CommonLetterFinder mFinder;

    /******************
     * TEST LIFECYCLE *
     ******************/

    @Before
    public void beforeInstance() {
        mFinder = new CommonLetterFinder();
    }

    @After
    public void afterInstance() {
        mFinder = null;
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
