package uk.co.lettin.output;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.co.lettin.service.QualifiedCurrency;
import uk.co.lettin.repository.CurrencyData;

import java.io.PrintStream;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SimpleUserOutputWriterTest {

    @InjectMocks
    SimpleUserOutputWriter subject;

    @Mock
    PrintStream printStream;

    @Test
    public void whenProvidedWithData_thenPrintStreamPassedAppropriateValues() {
        //Given
        QualifiedCurrency inputValue = mock(QualifiedCurrency.class);
        BigDecimal expectedValue = BigDecimal.valueOf(3l);
        when(inputValue.amount()).thenReturn(expectedValue);
        String expectedName = "expectedName";
        String expectedCountry = "expectedCountry";
        CurrencyData mockCurrencyData = mock(CurrencyData.class);
        when(inputValue.currencyData()).thenReturn(mockCurrencyData);

        when(mockCurrencyData.name()).thenReturn(expectedName);
        when(mockCurrencyData.country()).thenReturn(expectedCountry);

        //When
        subject.writeOutput(inputValue);

        //Then
        ArgumentCaptor<String> stringCaptor = ArgumentCaptor.forClass(String.class);
        verify(printStream).println(stringCaptor.capture());
        assertTrue(stringCaptor.getValue().contains(expectedValue.toPlainString()));
        assertTrue(stringCaptor.getValue().contains(expectedName));
        assertTrue(stringCaptor.getValue().contains(expectedCountry));
    }

}