package org.crazymages.bankingspringproject.service.utils.mapper.impl;

import org.crazymages.bankingspringproject.dto.AgreementDTO;
import org.crazymages.bankingspringproject.entity.Agreement;
import org.crazymages.bankingspringproject.exception.DataNotFoundException;
import org.crazymages.bankingspringproject.service.utils.mapper.DTOMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Component class that provides mapping functionality between Agreement and AgreementDTO objects.
 */
@Component
public class AgreementDTOMapper implements DTOMapper<Agreement, AgreementDTO> {

    @Override
    public AgreementDTO mapEntityToDto(Agreement agreement) {
        return new AgreementDTO(
                agreement.getUuid(),
                agreement.getAccountUuid(),
                agreement.getProductUuid(),
                agreement.getInterestRate(),
                agreement.getStatus(),
                agreement.getAmount()
        );
    }

    @Override
    public Agreement mapDtoToEntity(AgreementDTO agreementDTO) {
        Agreement agreement = new Agreement();
        agreement.setUuid(agreementDTO.getUuid());
        agreement.setAccountUuid(agreementDTO.getAccountUuid());
        agreement.setProductUuid(agreementDTO.getProductUuid());
        agreement.setInterestRate(agreementDTO.getInterestRate());
        agreement.setStatus(agreementDTO.getStatus());
        agreement.setAmount(agreementDTO.getAmount());
        return agreement;
    }

    @Override
    public List<AgreementDTO> getListOfDTOs(List<Agreement> agreementList) {
        return Optional.ofNullable(agreementList)
                .orElseThrow(() -> new DataNotFoundException("list is null"))
                .stream()
                .filter(Objects::nonNull)
                .map(this::mapEntityToDto)
                .toList();
    }
}
