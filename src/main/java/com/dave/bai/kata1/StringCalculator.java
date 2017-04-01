package com.dave.bai.kata1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/*
    TDD pairing/Xtreme programming exercise with Matt Wissman
    Exercise from: http://osherove.com/tdd-kata-1/
 */
public class StringCalculator {

    private static final String DEFAULT_DELIMITERS = "[,\n]";

    public int add(String rawUnhandledInput) {

        if (rawUnhandledInput.isEmpty()) {
            return 0;
        }

        String javaCompliantRegexDelimiters;
        String actualNumbersToAdd;

        if (overrideDefaultDelimiters(rawUnhandledInput)) {
            javaCompliantRegexDelimiters = parseAndGrabDelimters(rawUnhandledInput);
            actualNumbersToAdd = grabRawSummatationHalf(rawUnhandledInput);

        } else {
            javaCompliantRegexDelimiters = DEFAULT_DELIMITERS;
            actualNumbersToAdd = rawUnhandledInput;
        }

        String[] stringSegments = actualNumbersToAdd.split(javaCompliantRegexDelimiters);

        List<Integer> integerStream = Arrays.asList(stringSegments).stream().map(Integer::parseInt).filter(i -> i <= 1000).collect(toList());

        List<Integer> negativeValues = integerStream.stream().filter(i -> i < 0).collect(toList());

        if (negativeValues.size() > 0) {
            throw new RuntimeException("Negatives not allowed: " + negativeValues.stream().map(i -> i.toString()).collect(Collectors.joining(", ")));
        }

        return integerStream.stream().collect(Collectors.summingInt(Integer::intValue));
    }

    private String grabRawSummatationHalf(String numbersToAdd) {
        String[] splitOnNewLine = numbersToAdd.split("\n");
        return splitOnNewLine[1];
    }

    private String parseAndGrabDelimters(String originalDelimiters) {
        String[] grabDelimsSplit = originalDelimiters.split("\n");
        String delimitersWithStartingSlashes = grabDelimsSplit[0];
        String delimiters = delimitersWithStartingSlashes.substring(2);

        if (delimiters.startsWith("[") && delimiters.endsWith("]")) {
            if (delimiters.split("\\[").length == 3 && delimiters.split("\\]").length == 2) {
                return "(" + parseMultipleDefinedDelimiters(delimiters) + ")";
            }

            String finalLiteralStringDelim = formAndGrabLiteralStringDelim(delimiters.substring(1, delimiters.length() - 1));
            return "(" + finalLiteralStringDelim + ")";
        }
        return "[" + delimitersWithStartingSlashes.substring(2) + "]";
    }

    private String parseMultipleDefinedDelimiters(String delimiters) {
        StringBuilder sb = new StringBuilder();
        Pattern p = Pattern.compile("\\[(.*?)\\]");
        Matcher m = p.matcher(delimiters);

        List<String> multipleDelims = new ArrayList<>();
        while (m.find()) {
            multipleDelims.add(m.group(1));
        }

        for (String inputDelimPattern : multipleDelims) {
            sb.append(formAndGrabLiteralStringDelim(inputDelimPattern));
            sb.append("|");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    private String formAndGrabLiteralStringDelim(String inputDelimPattern) {
        List<Character> individualDelimChars = new ArrayList<>();
        char[] inputChars = inputDelimPattern.toCharArray();
        for (char c : inputChars) {
            individualDelimChars.add(c);
        }
        return individualDelimChars.stream().filter(c -> !(c == '[' || c == ']')).map(c -> "\\" + String.valueOf(c)).collect(Collectors.joining());
    }

    private boolean overrideDefaultDelimiters(String numbersToAdd) {
        if (numbersToAdd.startsWith("//") && numbersToAdd.contains(System.getProperty("line.separator"))) {
            return true;
        }
        return false;
    }
}
