package com.pataa.sdk;

import android.text.TextUtils;

public class Validation {
    public static final int PATAA_CODE_MINIMUM_THRESHOLD = 7;
    public static final int SEARCH_PATAA_CODE_MAXIMUM_THRESHOLD = 15;

    public static boolean isPataaCodeValid(String pataaCode) {
        pataaCode = pataaCode.replace("^", "");

        pataaCode = pataaCode.toUpperCase();

        // validate mobile number
        if (TextUtils.isEmpty(pataaCode)) {
            return false;
        } else {
            if ((pataaCode.length() < PATAA_CODE_MINIMUM_THRESHOLD) || (pataaCode.length() > SEARCH_PATAA_CODE_MAXIMUM_THRESHOLD)) {
                return false;
            }
        }

        if (isValidPataa(pataaCode)) {
            return true;
        } else {
            return false;
        }
    }
    private static boolean isValidPataa(String s) {
        String n = ".*[0-9].*";
        String a = ".*[A-Z].*";
        return s.matches(n) || s.matches(a);
    }

}
