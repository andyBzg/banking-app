package org.crazymages.bankingspringproject.service.database.impl;

import org.crazymages.bankingspringproject.dto.exchange_rate.CurrencyExchangeRateDto;
import org.crazymages.bankingspringproject.entity.CurrencyExchangeRate;
import org.crazymages.bankingspringproject.exception.DataNotFoundException;
import org.crazymages.bankingspringproject.repository.CurrencyExchangeRateRepository;
import org.crazymages.bankingspringproject.dto.exchange_rate.mapper.CurrencyExchangeRateDtoMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CurrencyExchangeRateDatabaseServiceImplTest {

    @Mock
    CurrencyExchangeRateRepository currencyExchangeRateRepository;
    @Mock
    CurrencyExchangeRateDtoMapper currencyExchangeRateDTOMapper;

    @InjectMocks
    CurrencyExchangeRateDatabaseServiceImpl currencyExchangeRateDatabaseService;

    @Captor
    private ArgumentCaptor<CurrencyExchangeRate> argumentCaptor;

    CurrencyExchangeRate currencyExchangeRate1;
    CurrencyExchangeRate currencyExchangeRate2;
    CurrencyExchangeRateDto currencyExchangeRateDto1;
    CurrencyExchangeRateDto currencyExchangeRateDto2;
    String currencyCode;
    Integer id;

    @BeforeEach
    void setUp() {
        currencyExchangeRate1 = new CurrencyExchangeRate();
        currencyExchangeRate2 = new CurrencyExchangeRate();
        currencyExchangeRateDto1 = new CurrencyExchangeRateDto();
        currencyExchangeRateDto2 = new CurrencyExchangeRateDto();
        currencyCode = "EUR";
        id = 1;
    }

    @Test
    void create_currencyIsNotPresentInDatabase_success() {
        // given
        currencyExchangeRate1.setCurrencyCode("EUR");
        currencyExchangeRate1.setExchangeRate(BigDecimal.valueOf(1.2));
        when(currencyExchangeRateRepository.findByCurrencyCode(currencyCode)).thenReturn(Optional.empty());

        // when
        currencyExchangeRateDatabaseService.create(currencyExchangeRate1);

        // then
        verify(currencyExchangeRateRepository, times(1)).save(currencyExchangeRate1);
    }

    @Test
    void create_currencyAlreadyPresentInDatabase_performEntityUpdate() {
        // given
        currencyExchangeRate1.setCurrencyCode(currencyCode);
        currencyExchangeRate1.setExchangeRate(BigDecimal.valueOf(1.2));
        currencyExchangeRate2.setCurrencyCode(currencyCode);
        currencyExchangeRate2.setExchangeRate(BigDecimal.valueOf(2.5));
        when(currencyExchangeRateRepository.findByCurrencyCode(currencyCode))
                .thenReturn(Optional.ofNullable(currencyExchangeRate1));

        // when
        currencyExchangeRateDatabaseService.create(currencyExchangeRate2);

        // then
        verify(currencyExchangeRateRepository).save(currencyExchangeRate2);
        verify(currencyExchangeRateRepository).save(argumentCaptor.capture());
        verifyNoMoreInteractions(currencyExchangeRateRepository);
        CurrencyExchangeRate expectedArgument = argumentCaptor.getValue();
        assertEquals(expectedArgument.getExchangeRate(), currencyExchangeRate2.getExchangeRate());
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
        List<CurrencyExchangeRateDto> expected = List.of(currencyExchangeRateDto1, currencyExchangeRateDto2);
        List<CurrencyExchangeRate> currencyExchangeRates = List.of(currencyExchangeRate1, currencyExchangeRate2);
        when(currencyExchangeRateRepository.findAllNotDeleted()).thenReturn(currencyExchangeRates);
        when(currencyExchangeRateDTOMapper.mapEntityToDto(currencyExchangeRate1)).thenReturn(currencyExchangeRateDto1);
        when(currencyExchangeRateDTOMapper.mapEntityToDto(currencyExchangeRate2)).thenReturn(currencyExchangeRateDto2);

        // when
        List<CurrencyExchangeRateDto> actual = currencyExchangeRateDatabaseService.findAllRates();

        // then
        assertEquals(expected, actual);
        verify(currencyExchangeRateRepository).findAllNotDeleted();
        verify(currencyExchangeRateDTOMapper, times(2)).mapEntityToDto(any(CurrencyExchangeRate.class));
    }

    @Test
    void findById_success() {
        // given
        CurrencyExchangeRate expected = currencyExchangeRate1;
        when(currencyExchangeRateRepository.findById(id))
                .thenReturn(Optional.ofNullable(currencyExchangeRate1));

        // when
        CurrencyExchangeRate actual = currencyExchangeRateDatabaseService.findById(id);

        // then
        assertEquals(expected, actual);
        verify(currencyExchangeRateRepository).findById(id);
    }

    @Test
    void findById_currencyCodeNotFound_throwsDataNotFoundException() {
        // given
        when(currencyExchangeRateRepository.findById(id)).thenReturn(Optional.empty());

        // when, then
        assertThrows(DataNotFoundException.class, () -> currencyExchangeRateDatabaseService.findById(id));
        verify(currencyExchangeRateRepository).findById(id);
    }

    @Test
    void update_success() {
        // given
        CurrencyExchangeRate rateToUpdate = currencyExchangeRate1;
        CurrencyExchangeRate updatedRate = currencyExchangeRate2;
        when(currencyExchangeRateRepository.findById(id)).thenReturn(Optional.ofNullable(rateToUpdate));

        // when
        currencyExchangeRateDatabaseService.update(id, updatedRate);

        // then
        verify(currencyExchangeRateRepository).findById(id);
        verify(currencyExchangeRateRepository).save(updatedRate);
    }

    @Test
    void update_rateIsDeleted_entity() {
        // given
        CurrencyExchangeRate rateToUpdate = currencyExchangeRate1;
        CurrencyExchangeRate updatedRate = currencyExchangeRate2;
        rateToUpdate.setDeleted(true);
        when(currencyExchangeRateRepository.findById(id)).thenReturn(Optional.of(rateToUpdate));

        // when
        currencyExchangeRateDatabaseService.update(id, updatedRate);

        // then
        verify(currencyExchangeRateRepository).findById(id);
        verify(currencyExchangeRateRepository, times(0)).save(any(CurrencyExchangeRate.class));
    }

    @Test
    void update_currencyCodeNotFound_throwsDataNotFoundException() {
        // given
        CurrencyExchangeRate updatedRate = currencyExchangeRate2;
        when(currencyExchangeRateRepository.findById(id)).thenReturn(Optional.empty());

        // when, then
        assertThrows(DataNotFoundException.class,
                () -> currencyExchangeRateDatabaseService.update(id, updatedRate));
        verify(currencyExchangeRateRepository).findById(id);
    }

    @Test
    void delete_success() {
        // given
        when(currencyExchangeRateRepository.findById(id))
                .thenReturn(Optional.ofNullable(currencyExchangeRate1));

        // when
        currencyExchangeRateDatabaseService.delete(id);

        // then
        assertTrue(currencyExchangeRate1.isDeleted());
        verify(currencyExchangeRateRepository).findById(id);
        verify(currencyExchangeRateRepository).save(currencyExchangeRate1);
    }

    @Test
    void delete_currencyCodeNotFound_throwsDataNotFoundException() {
        // given
        when(currencyExchangeRateRepository.findById(id)).thenReturn(Optional.empty());

        // when, then
        assertThrows(DataNotFoundException.class, () -> currencyExchangeRateDatabaseService.delete(id));
        verify(currencyExchangeRateRepository).findById(id);
    }
}
