package org.crazymages.bankingspringproject.service.utils.mapper;

import org.crazymages.bankingspringproject.dto.AgreementDTO;
import org.crazymages.bankingspringproject.entity.Agreement;
import org.crazymages.bankingspringproject.exception.DataNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Component class that provides mapping functionality between Agreement and AgreementDTO objects.
 */
@Component
public class AgreementDTOMapper {

    /**
     * Maps an Agreement object to an AgreementDTO object.
     *
     * @param agreement The Agreement object to be mapped.
     * @return The mapped AgreementDTO object.
     */
    public AgreementDTO mapToAgreementDTO(Agreement agreement) {
        return new AgreementDTO(
                agreement.getUuid(),
                agreement.getAccountUuid(),
                agreement.getProductUuid(),
                agreement.getInterestRate(),
                agreement.getStatus(),
                agreement.getAmount()
        );
    }

    /**
     * Maps an AgreementDTO object to an Agreement object.
     *
     * @param agreementDTO The AgreementDTO object to be mapped.
     * @return The mapped Agreement object.
     */
    public Agreement mapToAgreement(AgreementDTO agreementDTO) {
        Agreement agreement = new Agreement();
        agreement.setUuid(agreementDTO.getUuid());
        agreement.setAccountUuid(agreementDTO.getAccountUuid());
        agreement.setProductUuid(agreementDTO.getProductUuid());
        agreement.setInterestRate(agreementDTO.getInterestRate());
        agreement.setStatus(agreementDTO.getStatus());
        agreement.setAmount(agreementDTO.getAmount());
        return agreement;
    }

    /**
     * Maps a list of Agreement objects to a list of AgreementDTO objects.
     *
     * @param agreementList The list of Agreement objects to be mapped.
     * @return The list of mapped AgreementDTO objects.
     * @throws DataNotFoundException If the input agreementList is null.
     */
    public List<AgreementDTO> getListOfAgreementDTOs(List<Agreement> agreementList) {
        return Optional.ofNullable(agreementList)
                .orElseThrow(() -> new DataNotFoundException("list is null"))
                .stream()
                .filter(Objects::nonNull)
                .map(this::mapToAgreementDTO)
                .toList();
    }
}
