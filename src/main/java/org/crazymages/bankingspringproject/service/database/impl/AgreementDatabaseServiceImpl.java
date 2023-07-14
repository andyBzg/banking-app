package org.crazymages.bankingspringproject.service.database.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crazymages.bankingspringproject.dto.AgreementDTO;
import org.crazymages.bankingspringproject.entity.Agreement;
import org.crazymages.bankingspringproject.entity.enums.ProductType;
import org.crazymages.bankingspringproject.exception.DataNotFoundException;
import org.crazymages.bankingspringproject.repository.AgreementRepository;
import org.crazymages.bankingspringproject.service.database.AgreementDatabaseService;
import org.crazymages.bankingspringproject.service.utils.mapper.impl.AgreementDTOMapper;
import org.crazymages.bankingspringproject.service.utils.updater.EntityUpdateService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * A service implementation for managing Agreement entities in the database.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AgreementDatabaseServiceImpl implements AgreementDatabaseService {

    private final AgreementRepository agreementRepository;
    private final EntityUpdateService<Agreement> agreementUpdateService;
    private final AgreementDTOMapper agreementDTOMapper;


    @Override
    @Transactional
    public void create(AgreementDTO agreementDTO) {
        Agreement agreement = agreementDTOMapper.mapDtoToEntity(agreementDTO);
        agreementRepository.save(agreement);
        log.info("agreement created");
    }

    @Override
    @Transactional
    public List<AgreementDTO> findAllNotDeleted() {
        log.info("retrieving list of agreements");
        List<Agreement> agreements = agreementRepository.findAllNotDeleted();
        return agreementDTOMapper.getListOfDTOs(agreements);
    }

    @Override
    @Transactional
    public List<AgreementDTO> findDeletedAgreements() {
        log.info("retrieving list of deleted agreements");
        List<Agreement> deletedAgreements = agreementRepository.findAllDeleted();
        return agreementDTOMapper.getListOfDTOs(deletedAgreements);
    }

    @Override
    @Transactional
    public AgreementDTO findById(UUID uuid) {
        log.info("retrieving agreement by id {}", uuid);
        return agreementDTOMapper.mapEntityToDto(
                agreementRepository.findById(uuid)
                .orElseThrow(() -> new DataNotFoundException(String.valueOf(uuid))));
    }

    @Override
    @Transactional
    public Agreement findSavingsAgreementByClientId(UUID uuid) {
        log.info("retrieving savings agreement by id {}", uuid);
        return agreementRepository.findAgreementByClientIdAndProductType(uuid, ProductType.SAVINGS_ACCOUNT)
                .orElseThrow(() -> new DataNotFoundException(String.valueOf(uuid)));
    }

    @Override
    @Transactional
    public void update(UUID uuid, AgreementDTO updatedAgreementDTO) {
        Agreement updatedAgreement = agreementDTOMapper.mapDtoToEntity(updatedAgreementDTO);
        Agreement agreement = agreementRepository.findById(uuid)
                .orElseThrow(() -> new DataNotFoundException(String.valueOf(uuid)));
        agreement = agreementUpdateService.update(agreement, updatedAgreement);
        agreementRepository.save(agreement);
        log.info("updated agreement id {}", uuid);
    }

    @Override
    @Transactional
    public void delete(UUID uuid) {
        Agreement agreement = agreementRepository.findById(uuid)
                .orElseThrow(() -> new DataNotFoundException(String.valueOf(uuid)));
        agreement.setDeleted(true);
        agreementRepository.save(agreement);
        log.info("deleted agreement id {}", uuid);
    }

    @Override
    @Transactional
    public List<AgreementDTO> findAgreementsByManagerUuid(UUID managerUuid) {
        log.info("retrieving agreements by manager id {}", managerUuid);
        List<Agreement> agreements = agreementRepository.findAgreementsWhereManagerIdIs(managerUuid);
        return agreementDTOMapper.getListOfDTOs(agreements);
    }

    @Override
    @Transactional
    public List<AgreementDTO> findAgreementDTOsByClientUuid(UUID clientUuid) {
        log.info("retrieving agreements client id {}", clientUuid);
        List<Agreement> agreements = agreementRepository.findAgreementsWhereClientIdIs(clientUuid);
        return agreementDTOMapper.getListOfDTOs(agreements);
    }

    @Override
    @Transactional
    public List<Agreement> findAgreementsByClientUuid(UUID clientUuid) {
        log.info("retrieving agreements client id {}", clientUuid);
        return agreementRepository.findAgreementsWhereClientIdIs(clientUuid);
    }
}
