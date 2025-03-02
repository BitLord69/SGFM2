package com.sgfm2.utils;

/*
Programmerat av Jan-Erik "Janis" Karlsson 2020-02-20
Programmering i Java EMMJUH19, EC-Utbildning
CopyLeft 2020 - JanInc
*/

import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;

public class TextUtil {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String BRIGHT_BLACK =  "\u001b[30;1m";
    public static final String BRIGHT_RED = "\u001b[31;1m";
    public static final String BRIGHT_GREEN =  "\u001b[32;1m";
    public static final String BRIGHT_YELLOW = "\u001b[33;1m";
    public static final String BRIGHT_MAGENTA = "\u001b[35;1m";
    public static final String BRIGHT_CYAN =  "\u001b[36;1m";
    public static final String BRIGHT_WHITE = "\u001b[37;1m";

    public static final int RESET_COLOR_TOKEN_COUNT = 9;

    public static final int LEVEL_NORMAL = 0;
    public static final int LEVEL_BOLD = 1;
    public static final int LEVEL_STRESSED = 2;
    public static final int LEVEL_INFO = 3;
    public static final int LEVEL_WARNING = 4;

    public static String pimpString(String title, int level) {
        String prefix;

        switch (level) {
            case LEVEL_NORMAL:
                prefix = ANSI_BLACK;
                break;

            case LEVEL_BOLD:
                prefix = ANSI_PURPLE;
                break;

            case LEVEL_STRESSED:
                prefix = ANSI_GREEN;
                break;

            case LEVEL_INFO:
                prefix = ANSI_BLUE;
                break;

            case LEVEL_WARNING:
                prefix = ANSI_RED;
                break;

            default:
                prefix = "";
        } // switch

        return prefix + title + ANSI_RESET;
    } // pimpString

    public static String pimpString(int number, int level) {
        return pimpString(String.format("%d", number), level);
    } // pimpString

    public static String pimpString(double number, int level) {
        return pimpString(String.format("%.2f", number), level);
    } // pimpString

    public static String titleCase(String inputString) {
        if (inputString == null || inputString.isEmpty()) {
            return inputString;
        } // if inputString...

        return Arrays
                .stream(inputString.split(" "))
                .map(word -> word.isEmpty()
                        ? word
                        : Character.toTitleCase(word.charAt(0)) + word
                        .substring(1)
                        .toLowerCase())
                .collect(Collectors.joining(" "));
    } // titleCase

    public static String generateRandomString (int targetStringLength) {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        return generatedString;
    } // generateRandomString

    public static String centerText(String input, int width){
        int left = (width - input.length()) / 2;
        int right = width - left;
        String str = "%" + left + "s%-" + right + "s";
        return String.format(str, " " , input);
    } // centerText
} // class TextUtil
