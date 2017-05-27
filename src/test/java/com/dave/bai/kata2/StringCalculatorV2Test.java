package com.dave.bai.kata2;

import org.apache.log4j.*;
import org.apache.log4j.spi.LoggingEvent;
import org.junit.*;
import org.junit.rules.ExpectedException;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import static org.junit.Assert.*;

public class StringCalculatorV2Test {

    private static Vector<Appender> appenders;
    private static StringWriter writer;
    private static boolean additivity;

    private StringCalculatorV2 stringCalculator;

    @BeforeClass
    public static void setUpOnce() {
        appenders = new Vector<>(1);
        appenders.add(new ConsoleAppender(new PatternLayout("%d [%t] %-5p %c - %m%n")));
        writer = new StringWriter();
        appenders.add(new WriterAppender(new PatternLayout("%p, %m%n"), writer));
    }

    @Before
    public void setUp() throws Exception {
        stringCalculator = new StringCalculatorV2();

        for (Appender appender : appenders) {
            stringCalculator.getLogger().addAppender(appender);
        }
        additivity = stringCalculator.getLogger().getAdditivity();
        stringCalculator.getLogger().setAdditivity(false);
    }

    private void calculateAndAssert(String numbersToAdd, int expected) {
        int result = stringCalculator.add(numbersToAdd);
        assertEquals(expected, result);
    }

    @Test
    public void returnsZeroWhenEmptyStringGiven() throws Exception {
        calculateAndAssert("", 0);
    }

    @Test
    public void whenSingleNumberGivenThenItIsReturned() {
        calculateAndAssert("10", 10);
    }

    @Test
    public void whenTwoNumbersGivenWithNoSpaceThenTheirSumIsReturned() throws Exception {
        calculateAndAssert("1,2", 3);
    }

    @Test
    public void whenManyNumbersGivenWithNoSpaceThenTheirSumIsReturned() throws Exception {
        calculateAndAssert("1,2,3,4,5,6", 21);
    }

    @Test
    public void whenThreeNumbersGivenWithOneNewLineSumIsReturned() throws Exception {
        calculateAndAssert("1\n2,3", 6);
    }

    @Test(expected = Exception.class)
    public void whenANewLineAndACommaSeperateANumberAnExceptionIsThrown() throws Exception {
        stringCalculator.add("1\n,2,3");
    }

    @Test(expected = Exception.class)
    public void misplacedNewlineAfterCustomDelimeterThrowsException() throws Exception {
        stringCalculator.add("//;1;2\n");
    }

    @Test
    public void whenADelimiterIsGivenThenThatDelimiterIsUsed() throws Exception {
        calculateAndAssert("//;\n1;2", 3);
    }

    @Test
    public void negativeValuesAreDisallowedAndThrownInException() throws Exception {
        expectedEx.expect(RuntimeException.class);
        expectedEx.expectMessage("Negatives not allowed: -1, -2");

        stringCalculator.add("-1,-2,3");
    }

    @Test
    public void inputLargerThen1000Ignored() throws Exception {
        calculateAndAssert("1,1001,1000", 1001);
    }

    @Test
    public void allowMultiCharacterDelemiters() throws Exception {
        calculateAndAssert("//[***]\n1***2***3", 6);
    }

    @Test(expected = NumberFormatException.class)
    public void testVariedMultiCharDelimThrowsNumberFormatException() throws Exception {
        calculateAndAssert("//[**;]\n1**2**3", 6);
    }

    @Test
    public void testVariedMultiCharDelimGivesCorrectAnswer() throws Exception {
        calculateAndAssert("//[**;]\n1**;2**;3", 6);
    }

    @Test
    public void allowMultipleMultiCharDelimiters() throws Exception {
        calculateAndAssert("//[*][%]\n1*2%3", 6);
    }

    @Test
    public void testAllowMultipleDelimsEachWithMultipleChars() throws Exception {
        calculateAndAssert("//[**][%%]\n1**2%%3", 6);
    }

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();
    /*  above tests are copied over from kata1
        the ones starting below are new to kata2
     */

    /*  step 1:
        Add Logging Abilities to your new String Calculator (to an ILogger.Write()) interface (you will need a mock).
        Every time you call Add(), the sum result will be logged to the logger.
     */

    private void calculateAndAssertAndLogVerification(String numbersToAdd, int expectedResult) {
        calculateAndAssert(numbersToAdd, expectedResult);
        assertEquals("INFO, Summation result for String Calculator(v2) ===> " + expectedResult + "\n", writer.toString());
    }

    @Test
    public void string_calculator_has_logger() throws Exception {
        assertNotNull(stringCalculator.getLogger());
    }

    @Test
    public void summation_result_is_logged() throws Exception {
        writer.getBuffer().setLength(0); // clear writer buffer of old stuff before getting the new log content
        calculateAndAssertAndLogVerification("1,2,3", 6);
    }

    @After
    public void tearDown() {
        for (Appender appender : appenders) {
            stringCalculator.getLogger().removeAppender(appender);
        }
        stringCalculator.getLogger().setAdditivity(additivity);
    }

    /*  step 2:
        When calling Add() and the logger throws an exception, the string calculator
        should notify an IWebservice of some kind that logging has failed with the message
        from the logger’s exception (you will need a mock and a stub).
     */
}