package com.omaits.thebinranger.utility;

import org.springframework.boot.logging.LogLevel;

import java.util.regex.Pattern;

public class StringUtility {
    static Pattern numericPattern = Pattern.compile("-?\\d+(\\.\\d+)?");
    public static boolean isNumeric(String input)
    {
        if (input == null) {
            return false;
        }

        return numericPattern.matcher(input).matches();
    }

    private void log(LogLevel logLevel, String message)
    {
        //this is here for illustrative purposes. don't do anything at this time.
    }
}
