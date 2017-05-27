package com.dave.bai.kata1;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;

/*
    TDD pairing/Xtreme programming exercise with Matt Wissman
    Exercise from: http://osherove.com/tdd-kata-1/
 */
public class StringCalculatorTest {

    private StringCalculator stringCalculator;

    @Before
    public void setUp() throws Exception {
        stringCalculator = new StringCalculator();
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
}