package org.crazymages.bankingspringproject.service.database.impl;

import org.crazymages.bankingspringproject.dto.CurrencyExchangeRateDTO;
import org.crazymages.bankingspringproject.entity.CurrencyExchangeRate;
import org.crazymages.bankingspringproject.exception.DataNotFoundException;
import org.crazymages.bankingspringproject.repository.CurrencyExchangeRateRepository;
import org.crazymages.bankingspringproject.service.utils.mapper.impl.CurrencyExchangeRateDTOMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CurrencyExchangeRateDatabaseServiceImplTest {

    @Mock
    CurrencyExchangeRateRepository currencyExchangeRateRepository;
    @Mock
    CurrencyExchangeRateDTOMapper currencyExchangeRateDTOMapper;

    @InjectMocks
    CurrencyExchangeRateDatabaseServiceImpl currencyExchangeRateDatabaseService;

    CurrencyExchangeRate currencyExchangeRate1;
    CurrencyExchangeRate currencyExchangeRate2;
    CurrencyExchangeRateDTO currencyExchangeRateDTO1;
    CurrencyExchangeRateDTO currencyExchangeRateDTO2;
    String currencyCode;

    @BeforeEach
    void setUp() {
        currencyExchangeRate1 = new CurrencyExchangeRate();
        currencyExchangeRate2 = new CurrencyExchangeRate();
        currencyExchangeRateDTO1 = new CurrencyExchangeRateDTO();
        currencyExchangeRateDTO2 = new CurrencyExchangeRateDTO();
        currencyCode = "EUR";
    }

    @Test
    void create_success() {
        // when
        currencyExchangeRateDatabaseService.create(currencyExchangeRate1);

        // then
        verify(currencyExchangeRateRepository).save(currencyExchangeRate1);
    }

    @Test
    void findAll_success() {
        // given
        List<CurrencyExchangeRate> expected = List.of(currencyExchangeRate1, currencyExchangeRate2);
        when(currencyExchangeRateRepository.findAllNotDeleted()).thenReturn(expected);

        // when
        List<CurrencyExchangeRate> actual = currencyExchangeRateDatabaseService.findAll();

        // then
        assertEquals(expected, actual);
        verify(currencyExchangeRateRepository).findAllNotDeleted();
    }

    @Test
    void findAllDTOs_success() {
        // given
        List<CurrencyExchangeRateDTO> expected = List.of(currencyExchangeRateDTO1, currencyExchangeRateDTO2);
        List<CurrencyExchangeRate> currencyExchangeRates = List.of(currencyExchangeRate1, currencyExchangeRate2);
        when(currencyExchangeRateRepository.findAllNotDeleted()).thenReturn(currencyExchangeRates);
        when(currencyExchangeRateDTOMapper.getListOfDTOs(currencyExchangeRates)).thenReturn(expected);

        // when
        List<CurrencyExchangeRateDTO> actual = currencyExchangeRateDatabaseService.findAllDTOs();

        // then
        assertEquals(expected, actual);
        verify(currencyExchangeRateRepository).findAllNotDeleted();
        verify(currencyExchangeRateDTOMapper).getListOfDTOs(currencyExchangeRates);
    }

    @Test
    void findById_success() {
        // given
        CurrencyExchangeRate expected = currencyExchangeRate1;
        when(currencyExchangeRateRepository.findById(currencyCode))
                .thenReturn(Optional.ofNullable(currencyExchangeRate1));

        // when
        CurrencyExchangeRate actual = currencyExchangeRateDatabaseService.findById(currencyCode);

        // then
        assertEquals(expected, actual);
        verify(currencyExchangeRateRepository).findById(currencyCode);
    }

    @Test
    void findById_currencyCodeNotFound_throwsDataNotFoundException() {
        // given
        when(currencyExchangeRateRepository.findById(currencyCode)).thenReturn(Optional.empty());

        // when, then
        assertThrows(DataNotFoundException.class, () -> currencyExchangeRateDatabaseService.findById(currencyCode));
        verify(currencyExchangeRateRepository).findById(currencyCode);
    }

    @Test
    void update_success() {
        // given
        CurrencyExchangeRate rateToUpdate = currencyExchangeRate1;
        CurrencyExchangeRate updatedRate = currencyExchangeRate2;
        when(currencyExchangeRateRepository.findById(currencyCode)).thenReturn(Optional.ofNullable(rateToUpdate));

        // when
        currencyExchangeRateDatabaseService.update(currencyCode, updatedRate);

        // then
        verify(currencyExchangeRateRepository).findById(currencyCode);
        verify(currencyExchangeRateRepository).save(updatedRate);
    }

    @Test
    void update_rateIsDeleted_entity() {
        // given
        CurrencyExchangeRate rateToUpdate = currencyExchangeRate1;
        CurrencyExchangeRate updatedRate = currencyExchangeRate2;
        rateToUpdate.setDeleted(true);
        when(currencyExchangeRateRepository.findById(currencyCode)).thenReturn(Optional.of(rateToUpdate));

        // when
        currencyExchangeRateDatabaseService.update(currencyCode, updatedRate);

        // then
        verify(currencyExchangeRateRepository).findById(currencyCode);
        verify(currencyExchangeRateRepository, times(0)).save(any(CurrencyExchangeRate.class));
    }

    @Test
    void update_currencyCodeNotFound_throwsDataNotFoundException() {
        // given
        CurrencyExchangeRate updatedRate = currencyExchangeRate2;
        when(currencyExchangeRateRepository.findById(currencyCode)).thenReturn(Optional.empty());

        // when, then
        assertThrows(DataNotFoundException.class,
                () -> currencyExchangeRateDatabaseService.update(currencyCode, updatedRate));
        verify(currencyExchangeRateRepository).findById(currencyCode);
    }

    @Test
    void delete_success() {
        // given
        when(currencyExchangeRateRepository.findById(currencyCode))
                .thenReturn(Optional.ofNullable(currencyExchangeRate1));

        // when
        currencyExchangeRateDatabaseService.delete(currencyCode);

        // then
        assertTrue(currencyExchangeRate1.isDeleted());
        verify(currencyExchangeRateRepository).findById(currencyCode);
        verify(currencyExchangeRateRepository).save(currencyExchangeRate1);
    }

    @Test
    void delete_currencyCodeNotFound_throwsDataNotFoundException() {
        // given
        when(currencyExchangeRateRepository.findById(currencyCode)).thenReturn(Optional.empty());

        // when, then
        assertThrows(DataNotFoundException.class, () -> currencyExchangeRateDatabaseService.delete(currencyCode));
        verify(currencyExchangeRateRepository).findById(currencyCode);
    }
}
