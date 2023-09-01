package uk.co.lettin.input;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.co.lettin.service.CurrencyCodeValidator;
import uk.co.lettin.repository.CurrencyConversionDataRepository;
import uk.co.lettin.repository.CurrencyData;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CurrencyCodeValidatorTest {

    @InjectMocks
    CurrencyCodeValidator subject;

    @Mock
    CurrencyConversionDataRepository dataRepository;


    @Test
    public void whenCodeValid_thenReturnTrue() {
        //Given
        String validCode = "GBP";

        CurrencyData mockCurrencyData = mock(CurrencyData.class);
        when(dataRepository.getForCode(validCode)).thenReturn(mockCurrencyData);

        //When
        boolean result = subject.isAcceptableCode(validCode);

        //Then
        assertTrue(result);
    }

    @Test
    public void whenCodeInvalid_thenReturnTrue() {
        //Given
        String validCode = "GBP";
        String invalidCode = "NOT";

        CurrencyData mockCurrencyData = mock(CurrencyData.class);
        when(dataRepository.getForCode(any())).thenReturn(null);

        //When
        boolean result = subject.isAcceptableCode(validCode);

        //Then
        assertFalse(result);
    }

}