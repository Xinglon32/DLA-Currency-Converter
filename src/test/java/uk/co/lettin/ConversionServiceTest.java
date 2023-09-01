package uk.co.lettin;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.co.lettin.conversion.CurrencyConverter;
import uk.co.lettin.input.CurrencyInputDto;
import uk.co.lettin.input.UserInputReader;
import uk.co.lettin.service.ConversionService;
import uk.co.lettin.service.QualifiedCurrency;
import uk.co.lettin.output.UserOutputWriter;
import uk.co.lettin.repository.CurrencyConversionDataRepository;
import uk.co.lettin.repository.CurrencyData;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConversionServiceTest {

    //Subject
    @InjectMocks
    ConversionService subject;

    //Mocks
    @Mock
    UserInputReader mockUserInputReader;
    @Mock
    CurrencyConverter mockCurrencyConverter;
    @Mock
    UserOutputWriter mockUserOutputWriter;
    @Mock
    CurrencyConversionDataRepository currencyConversionDataRepository;

    /* Notes:
     * - While this class should be tested, how it's tested in a non-technical-test scenario would depend how it was
     *     built up. Each separate section being built first (as I did), would mean this class is nearly always going to
     *     be written without TDD, which then causes a tightly coupled implementation of the test as below.
     * - Use of Mocked "Primitives" I find to be a taste\code-style thing - I like them as it means if something new
     *     happens to them, the test is more likely to fail and force the developer to re-evaluate
     * - This test would have more meaning if the signature of any part of this application had Exceptions that expect
     *     to be handled
     */

    @Test
    public void whenStarted_ThenDelegateActions() {
        //Given

        //Setup User Input Mocks
        CurrencyInputDto mockCurrencyInputDto = mock(CurrencyInputDto.class);

        BigDecimal mockInputAmount = mock(BigDecimal.class);
        String sourceCurrencyCode = "GBP";
        String targetCurrencyCode = "EUR";

        when(mockCurrencyInputDto.amount()).thenReturn(mockInputAmount);
        when(mockCurrencyInputDto.sourceCode()).thenReturn(sourceCurrencyCode);
        when(mockCurrencyInputDto.targetCode()).thenReturn(targetCurrencyCode);

        //Setup Data Mocks
        CurrencyData mockSourceCurrencyData = mock(CurrencyData.class);
        when(currencyConversionDataRepository.getForCode(sourceCurrencyCode)).thenReturn(mockSourceCurrencyData);

        when(mockUserInputReader.fetchInputData()).thenReturn(mockCurrencyInputDto);

        QualifiedCurrency mockOutputQualifiedCurrency = mock(QualifiedCurrency.class);
        when(mockCurrencyConverter.convert(any(), eq(targetCurrencyCode)))
                .thenReturn(mockOutputQualifiedCurrency);

        doNothing().when(mockUserOutputWriter).writeOutput(mockOutputQualifiedCurrency);

        //When
        subject.start();

        //Then
        verify(mockUserInputReader).fetchInputData();

        ArgumentCaptor<QualifiedCurrency> qualifiedCurrencyCaptor = ArgumentCaptor.forClass(QualifiedCurrency.class);
        verify(mockCurrencyConverter).convert(qualifiedCurrencyCaptor.capture(), eq(targetCurrencyCode));
        assertEquals(qualifiedCurrencyCaptor.getValue().amount(), mockInputAmount);
        assertEquals(qualifiedCurrencyCaptor.getValue().currencyData(), mockSourceCurrencyData);

        verify(mockUserOutputWriter).writeOutput(mockOutputQualifiedCurrency);
    }

}