package com.seamfix.bvnservice.SeamfixBvnServiceTest.helper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utility {

    public static boolean isNumeric(String number) {
        boolean isValid = false;
        String expression = "^[-+]?[0-9]*\\.?[0-9]+$";
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(number);
        if (matcher.matches()) {
            isValid = true;
        }

        return isValid;
    }
}
