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
        if (agreement == null) {
            throw new IllegalArgumentException("agreement cannot be null");
        }
        return AgreementDto.builder()
                .accountUuid(agreement.getAccountUuid() != null ? agreement.getAccountUuid().toString() : null)
                .productUuid(agreement.getProductUuid() != null ? agreement.getProductUuid().toString() : null)
                .interestRate(agreement.getInterestRate())
                .status(agreement.getStatus() != null ? agreement.getStatus().name() : null)
                .amount(agreement.getAmount())
                .build();
    }

    @Override
    public Agreement mapDtoToEntity(AgreementDto agreementDto) {
        if (agreementDto == null) {
            throw new IllegalArgumentException("agreementDto cannot be null");
        }
        return Agreement.builder()
                .accountUuid(agreementDto.getAccountUuid() != null ? UUID.fromString(agreementDto.getAccountUuid()) : null)
                .productUuid(agreementDto.getProductUuid() != null ? UUID.fromString(agreementDto.getProductUuid()) : null)
                .interestRate(agreementDto.getInterestRate())
                .status(agreementDto.getStatus() != null ? AgreementStatus.valueOf(agreementDto.getStatus()) : null)
                .amount(agreementDto.getAmount())
                .build();
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
