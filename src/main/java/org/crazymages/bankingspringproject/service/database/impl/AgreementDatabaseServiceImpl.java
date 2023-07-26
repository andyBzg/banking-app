package org.crazymages.bankingspringproject.service.database.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crazymages.bankingspringproject.dto.AgreementDto;
import org.crazymages.bankingspringproject.dto.mapper.agreement.AgreementWithProductDtoMapper;
import org.crazymages.bankingspringproject.entity.Agreement;
import org.crazymages.bankingspringproject.entity.enums.ProductType;
import org.crazymages.bankingspringproject.exception.DataNotFoundException;
import org.crazymages.bankingspringproject.repository.AgreementRepository;
import org.crazymages.bankingspringproject.service.database.AgreementDatabaseService;
import org.crazymages.bankingspringproject.dto.mapper.agreement.AgreementDtoMapper;
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
    private final AgreementDtoMapper agreementDtoMapper;
    private final AgreementWithProductDtoMapper agreementWithProductDtoMapper;


    @Override
    @Transactional
    public void create(AgreementDto agreementDTO) {
        Agreement agreement = agreementDtoMapper.mapDtoToEntity(agreementDTO);
        agreementRepository.save(agreement);
        log.info("agreement created");
    }

    @Override
    @Transactional
    public List<AgreementDto> findAllNotDeleted() {
        log.info("retrieving list of agreements");
        List<Agreement> agreements = agreementRepository.findAllNotDeleted();
        return agreementDtoMapper.getDtoList(agreements);
    }

    @Override
    @Transactional
    public List<AgreementDto> findDeletedAgreements() {
        log.info("retrieving list of deleted agreements");
        List<Agreement> deletedAgreements = agreementRepository.findAllDeleted();
        return agreementDtoMapper.getDtoList(deletedAgreements);
    }

    @Override
    @Transactional
    public AgreementDto findById(String agreementUuid) {
        if (agreementUuid == null) {
            throw new IllegalArgumentException();
        }
        UUID uuid = UUID.fromString(agreementUuid);
        log.info("retrieving agreement by id {}", uuid);
        return agreementDtoMapper.mapEntityToDto(
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
    public void update(String agreementUuid, AgreementDto updatedAgreementDto) {
        if (agreementUuid == null || updatedAgreementDto == null) {
            throw new IllegalArgumentException();
        }
        UUID uuid = UUID.fromString(agreementUuid);
        Agreement updatedAgreement = agreementDtoMapper.mapDtoToEntity(updatedAgreementDto);
        Agreement agreement = agreementRepository.findById(uuid)
                .orElseThrow(() -> new DataNotFoundException(String.valueOf(uuid)));
        agreement = agreementUpdateService.update(agreement, updatedAgreement);
        agreementRepository.save(agreement);
        log.info("updated agreement id {}", uuid);
    }

    @Override
    @Transactional
    public void delete(String agreementUuid) {
        if (agreementUuid == null) {
            throw new IllegalArgumentException();
        }
        UUID uuid = UUID.fromString(agreementUuid);
        Agreement agreement = agreementRepository.findById(uuid)
                .orElseThrow(() -> new DataNotFoundException(String.valueOf(uuid)));
        agreement.setDeleted(true);
        agreementRepository.save(agreement);
        log.info("deleted agreement id {}", uuid);
    }

    @Override
    @Transactional
    public List<AgreementDto> findAgreementsByManagerUuid(String managerUuid) {
        if (managerUuid == null) {
            throw new IllegalArgumentException();
        }
        UUID uuid = UUID.fromString(managerUuid);
        log.info("retrieving agreements by manager id {}", managerUuid);
        List<Agreement> agreements = agreementRepository.findAgreementsWhereManagerIdIs(uuid);
        return agreementDtoMapper.getDtoList(agreements);
    }

    @Override
    @Transactional
    public List<AgreementDto> findAgreementDtoListByClientUuid(String clientUuid) {
        if (clientUuid == null) {
            throw new IllegalArgumentException();
        }
        UUID uuid = UUID.fromString(clientUuid);
        log.info("retrieving agreements client id {}", clientUuid);
        List<Agreement> agreements = agreementRepository.findAgreementsWhereClientIdIs(uuid)
                .stream()
                .filter(a -> !a.isDeleted())
                .toList();
        log.info("{}", agreements);
        return agreementWithProductDtoMapper.getDtoList(agreements);
    }

    @Override
    @Transactional
    public List<Agreement> findAgreementsByClientUuid(UUID clientUuid) {
        log.info("retrieving agreements client id {}", clientUuid);
        return agreementRepository.findAgreementsWhereClientIdIs(clientUuid);
    }
}
