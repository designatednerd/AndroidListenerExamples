package com.designatednerd.androidlistenerexamples.model;

/**
 * A class which finds the common letters between two strings.
 * Created by ellen on 5/19/15.
 */
public class CommonLetterFinder {

    public String commonLetters(String aFirstString, String aSecondString) {
        if (aFirstString != null && aSecondString != null) {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < aFirstString.length(); i++) {
                String firstWordLetter = aFirstString.substring(i, i + 1);
                if (builder.toString().contains(firstWordLetter)) {
                    //already added, skip.
                    continue;
                }

                if (aSecondString.contains(firstWordLetter)) {
                    builder.append(firstWordLetter);
                }
            }

            if (builder.toString().length() > 0) {
                return builder.toString();
            }
        }

        //Fall-through case
        return null;
    }

    public String commonLettersCaseInsensitive(String aFirstString, String aSecondString) {
        return commonLetters(aFirstString.toLowerCase(), aSecondString.toLowerCase());
    }
}
