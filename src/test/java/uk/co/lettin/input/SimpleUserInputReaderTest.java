package uk.co.lettin.input;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.co.lettin.service.CurrencyCodeValidator;

import java.math.BigDecimal;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SimpleUserInputReaderTest {

    //Subject Under Test
    @InjectMocks
    private SimpleUserInputReader subject;

    //Mocks
    @Mock
    Scanner mockScanner;
    @Mock
    CurrencyCodeValidator currencyCodeValidator;

    //Valid Values
    private static final BigDecimal VALID_AMOUNT = new BigDecimal("4.29");
    private static final String VALID_SOURCE_CURRENCY = "GBP";
    private static final String VALID_TARGET_CURRENCY = "EUR";

    //Incorrect Values
    private static final String INVALID_TEXT_AMOUNT = "dragon";
    private static final String INVALID_MALFORMED_AMOUNT = "2.333";

    private static final String INVALID_TOO_LONG_CURRENCY_CODE = "GBPX";
    private static final String INVALID_CONTAINS_NUMBER_CURRENCY_CODE = "G3P";
    private static final String INVALID_NOT_RECOGNISED_CURRENCY_CODE = "NOT";

    private static final Exception FORCE_TERMINATE_EXCEPTION = new RuntimeException("Exception to Terminate application");

    /* Notes:
     * - Since the implementation has an essentially infinite loop if a value is flagged incorrect, each mock stubbing
     *     throws an exception at the end to prevent the test not completing. This had to be added after implementation.
     * - Once it was realised that this exception could occur, it felt "safer" to include the try-catch wrap on every
     *     test, as even inputs that are wrong are not expected to throw an exception
     * - Repeated code has been left in place for ease of changing individual scenarios. Given this is all in one class
     *     it can be "find-replace" if we need to change all occurrences of a behaviour. It could, if we knew that
     *     wouldn't ever happen, or were we to adopt perfect "DRY" be extracted into a local utility method in this
     *     class - "assertExpectedValues()" or similar
     * - Some of the "decoration" on this class is subject to the coding standards of the teams maintaining it. I've
     *     always liked the "Given\\When\\Then" pattern as it matches the patterns of good requirements, so I have
     *     chosen that here
     */

    @Test //Happy Path
    public void whenUserInputsValidAmount_ThenReturnCorrectDto() {
        //Given
        when(mockScanner.nextLine())
            .thenReturn(VALID_AMOUNT.toPlainString())
            .thenReturn(VALID_SOURCE_CURRENCY)
            .thenReturn(VALID_TARGET_CURRENCY)
            .thenThrow(FORCE_TERMINATE_EXCEPTION);

        when(currencyCodeValidator.isAcceptableCode(any())).thenReturn(true);

        //When
        CurrencyInputDto result = null;
        try {
             result = subject.fetchInputData();
        } catch (Exception e) {
            fail(e);
        }

        //Then
        verify(mockScanner, times(3)).nextLine();
        verifyNoMoreInteractions(mockScanner);
        assertEquals(result.amount(), VALID_AMOUNT);
        assertEquals(result.sourceCode(), VALID_SOURCE_CURRENCY);
        assertEquals(result.targetCode(), VALID_TARGET_CURRENCY);
    }

    @Test
    public void whenUserInputsInvalidTextAmount_ThenRequestRetry() {
        //Given
        when(mockScanner.nextLine())
            .thenReturn(INVALID_TEXT_AMOUNT)
            .thenReturn(VALID_AMOUNT.toPlainString())
            .thenReturn(VALID_SOURCE_CURRENCY)
            .thenReturn(VALID_TARGET_CURRENCY)
            .thenThrow(FORCE_TERMINATE_EXCEPTION);

        when(currencyCodeValidator.isAcceptableCode(any())).thenReturn(true);

        //When
        CurrencyInputDto result = null;
        try {
            result = subject.fetchInputData();
        } catch (Exception e) {
            fail(e);
        }

        //Then - Extra invocation on the scanner asking for the value again
        verify(mockScanner, times(4)).nextLine();
        verifyNoMoreInteractions(mockScanner);
        //Still puts each value in the right place
        assertEquals(result.amount(), VALID_AMOUNT);
        assertEquals(result.sourceCode(), VALID_SOURCE_CURRENCY);
        assertEquals(result.targetCode(), VALID_TARGET_CURRENCY);
    }

    @Test
    public void whenUserInputsMalformattedAmount_ThenRequestRetry() {
        //Given
        when(mockScanner.nextLine())
                .thenReturn(INVALID_MALFORMED_AMOUNT)
                .thenReturn(VALID_AMOUNT.toPlainString())
                .thenReturn(VALID_SOURCE_CURRENCY)
                .thenReturn(VALID_TARGET_CURRENCY)
                .thenThrow(FORCE_TERMINATE_EXCEPTION);

        when(currencyCodeValidator.isAcceptableCode(any())).thenReturn(true);

        //When
        CurrencyInputDto result = null;
        try {
            result = subject.fetchInputData();
        } catch (Exception e) {
            fail(e);
        }

        //Then - Extra invocation on the scanner asking for the value again
        verify(mockScanner, times(4)).nextLine();
        verifyNoMoreInteractions(mockScanner);
        //Still puts each correct value in the right place:
        assertEquals(result.amount(), VALID_AMOUNT);
        assertEquals(result.sourceCode(), VALID_SOURCE_CURRENCY);
        assertEquals(result.targetCode(), VALID_TARGET_CURRENCY);
    }

    @Test
    public void whenUserInputsInvalidTooLongSourceCurrency_ThenRequestRetry() {
        //Given
        when(mockScanner.nextLine())
                .thenReturn(VALID_AMOUNT.toPlainString())
                .thenReturn(INVALID_TOO_LONG_CURRENCY_CODE)
                .thenReturn(VALID_SOURCE_CURRENCY)
                .thenReturn(VALID_TARGET_CURRENCY)
                .thenThrow(FORCE_TERMINATE_EXCEPTION);

        when(currencyCodeValidator.isAcceptableCode(any())).thenReturn(true);

        //When
        CurrencyInputDto result = null;
        try {
            result = subject.fetchInputData();
        } catch (Exception e) {
            fail(e);
        }

        //Then - Extra invocation on the scanner asking for the value again
        verify(mockScanner, times(4)).nextLine();
        verifyNoMoreInteractions(mockScanner);
        //Still puts each correct value in the right place:
        assertEquals(result.amount(), VALID_AMOUNT);
        assertEquals(result.sourceCode(), VALID_SOURCE_CURRENCY);
        assertEquals(result.targetCode(), VALID_TARGET_CURRENCY);
    }

    @Test
    public void whenUserInputsInvalidContainsNumberSourceCurrency_ThenRequestRetry() {
        //Given
        when(mockScanner.nextLine())
                .thenReturn(VALID_AMOUNT.toPlainString())
                .thenReturn(INVALID_CONTAINS_NUMBER_CURRENCY_CODE)
                .thenReturn(VALID_SOURCE_CURRENCY)
                .thenReturn(VALID_TARGET_CURRENCY)
                .thenThrow(FORCE_TERMINATE_EXCEPTION);

        when(currencyCodeValidator.isAcceptableCode(any())).thenReturn(true);

        //When
        CurrencyInputDto result = null;
        try {
            result = subject.fetchInputData();
        } catch (Exception e) {
            fail(e);
        }

        //Then - Extra invocation on the scanner asking for the value again
        verify(mockScanner, times(4)).nextLine();
        verifyNoMoreInteractions(mockScanner);
        //Still puts each correct value in the right place:
        assertEquals(result.amount(), VALID_AMOUNT);
        assertEquals(result.sourceCode(), VALID_SOURCE_CURRENCY);
        assertEquals(result.targetCode(), VALID_TARGET_CURRENCY);
    }

    @Test
    public void whenUserInputsInvalidTooLongTargetCurrency_ThenRequestRetry() {
        //Given
        when(mockScanner.nextLine())
                .thenReturn(VALID_AMOUNT.toPlainString())
                .thenReturn(VALID_SOURCE_CURRENCY)
                .thenReturn(INVALID_TOO_LONG_CURRENCY_CODE)
                .thenReturn(VALID_TARGET_CURRENCY)
                .thenThrow(FORCE_TERMINATE_EXCEPTION);

        when(currencyCodeValidator.isAcceptableCode(any())).thenReturn(true);

        //When
        CurrencyInputDto result = null;
        try {
            result = subject.fetchInputData();
        } catch (Exception e) {
            fail(e);
        }

        //Then - Extra invocation on the scanner asking for the value again
        verify(mockScanner, times(4)).nextLine();
        verifyNoMoreInteractions(mockScanner);
        //Still puts each correct value in the right place:
        assertEquals(result.amount(), VALID_AMOUNT);
        assertEquals(result.sourceCode(), VALID_SOURCE_CURRENCY);
        assertEquals(result.targetCode(), VALID_TARGET_CURRENCY);
    }

    @Test
    public void whenUserInputsInvalidContainsNumberTargetCurrency_ThenRequestRetry() {
        //Given
        when(mockScanner.nextLine())
                .thenReturn(VALID_AMOUNT.toPlainString())
                .thenReturn(VALID_SOURCE_CURRENCY)
                .thenReturn(INVALID_CONTAINS_NUMBER_CURRENCY_CODE)
                .thenReturn(VALID_TARGET_CURRENCY)
                .thenThrow(FORCE_TERMINATE_EXCEPTION);

        when(currencyCodeValidator.isAcceptableCode(any())).thenReturn(true);

        //When
        CurrencyInputDto result = null;
        try {
            result = subject.fetchInputData();
        } catch (Exception e) {
            fail(e);
        }

        //Then - Extra invocation on the scanner asking for the value again
        verify(mockScanner, times(4)).nextLine();
        verifyNoMoreInteractions(mockScanner);
        //Still puts each correct value in the right place:
        assertEquals(result.amount(), VALID_AMOUNT);
        assertEquals(result.sourceCode(), VALID_SOURCE_CURRENCY);
        assertEquals(result.targetCode(), VALID_TARGET_CURRENCY);
    }

    @Test
    public void whenUserInputsInvalidNotRecognisedTargetCurrency_ThenRequestRetry() {
        //Given
        when(mockScanner.nextLine())
                .thenReturn(VALID_AMOUNT.toPlainString())
                .thenReturn(INVALID_NOT_RECOGNISED_CURRENCY_CODE)
                .thenReturn(VALID_SOURCE_CURRENCY)
                .thenReturn(VALID_TARGET_CURRENCY)
                .thenThrow(FORCE_TERMINATE_EXCEPTION);

        when(currencyCodeValidator.isAcceptableCode(INVALID_NOT_RECOGNISED_CURRENCY_CODE)).thenReturn(false);
        when(currencyCodeValidator.isAcceptableCode(VALID_SOURCE_CURRENCY)).thenReturn(true);
        when(currencyCodeValidator.isAcceptableCode(VALID_TARGET_CURRENCY)).thenReturn(true);

        //When
        CurrencyInputDto result = null;
        try {
            result = subject.fetchInputData();
        } catch (Exception e) {
            fail(e);
        }

        //Then - Extra invocation on the scanner asking for the value again
        verify(mockScanner, times(4)).nextLine();
        verifyNoMoreInteractions(mockScanner);
        //Still puts each correct value in the right place:
        assertEquals(result.amount(), VALID_AMOUNT);
        assertEquals(result.sourceCode(), VALID_SOURCE_CURRENCY);
        assertEquals(result.targetCode(), VALID_TARGET_CURRENCY);
    }

}