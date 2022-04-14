package com.pataa.sdk;

import android.text.InputFilter;
import android.text.Spanned;

public class AlphaNumericAndDashInputOnlyA2ZFilter implements InputFilter {
    public CharSequence filter(CharSequence source, int start, int end,
                               Spanned dest, int dstart, int dend) {
        System.out.println("text_entered : " + source);
        // Only keep characters that are alphanumeric
        StringBuilder builder = new StringBuilder();
        for (int i = start; i < end; i++) {
            char input_char = source.charAt(i);

            if (dest.toString().contains("-") && dend-dest.toString().indexOf("-")>3){
                System.out.println(" Only Three digits allowed");
            }
            // CHECKING FOR ALPHABET
            else if ((input_char >= 65 && input_char <= 90)
                    || (input_char >= 97 && input_char <= 122)) {
                System.out.println(" Alphabet ");
                builder.append(input_char);
                // CHECKING FOR DIGITS
            } else if (input_char >= 48 && input_char <= 57) {
                System.out.println(" Digit ");
                builder.append(input_char);

                // OTHERWISE SPECIAL CHARACTER
            } else if (dend != 0 && input_char == '-' && !dest.toString().contains("-")) {
                builder.append(input_char);
                System.out.println(" dash for extension ");
            }

            // Carret Symbol for Pataa
            else if (input_char == '^' && dstart==0) {
                builder.append(input_char);
                System.out.println(" careat for Pataa ");
            } else {
                System.out.println(" Special Character ");
            }
        }

        // If all characters are valid, return null, otherwise only return the filtered characters
        boolean allCharactersValid = (builder.length() == end - start);
        return allCharactersValid ? null : builder.toString();
    }
}