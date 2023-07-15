package org.crazymages.bankingspringproject.service.utils.mapper.impl;

import org.crazymages.bankingspringproject.dto.AgreementDto;
import org.crazymages.bankingspringproject.entity.Agreement;
import org.crazymages.bankingspringproject.entity.enums.AgreementStatus;
import org.crazymages.bankingspringproject.exception.DataNotFoundException;
import org.crazymages.bankingspringproject.service.utils.mapper.DtoMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * Component class that provides mapping functionality between Agreement and AgreementDTO objects.
 */
@Component
public class AgreementDtoMapper implements DtoMapper<Agreement, AgreementDto> {

    @Override
    public AgreementDto mapEntityToDto(Agreement agreement) {
        AgreementDto agreementDto = new AgreementDto();
        agreementDto.setUuid(String.valueOf(agreement.getUuid()));
        agreementDto.setAccountUuid(String.valueOf(agreement.getAccountUuid()));
        agreementDto.setProductUuid(String.valueOf(agreement.getProductUuid()));
        agreementDto.setInterestRate(agreement.getInterestRate());
        agreementDto.setStatus(String.valueOf(agreement.getStatus()));
        agreementDto.setAmount(agreement.getAmount());
        return agreementDto;
    }

    @Override
    public Agreement mapDtoToEntity(AgreementDto agreementDto) {
        Agreement agreement = new Agreement();
        agreement.setUuid(UUID.fromString(agreementDto.getUuid()));
        agreement.setAccountUuid(UUID.fromString(agreementDto.getAccountUuid()));
        agreement.setProductUuid(UUID.fromString(agreementDto.getProductUuid()));
        agreement.setInterestRate(agreementDto.getInterestRate());
        agreement.setStatus(AgreementStatus.valueOf(agreementDto.getStatus()));
        agreement.setAmount(agreementDto.getAmount());
        return agreement;
    }

    @Override
    public List<AgreementDto> getDtoList(List<Agreement> agreementList) {
        return Optional.ofNullable(agreementList)
                .orElseThrow(() -> new DataNotFoundException("list is null"))
                .stream()
                .filter(Objects::nonNull)
                .map(this::mapEntityToDto)
                .toList();
    }
}
