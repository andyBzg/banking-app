package org.crazymages.bankingspringproject.service.database.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crazymages.bankingspringproject.entity.Agreement;
import org.crazymages.bankingspringproject.entity.enums.ProductType;
import org.crazymages.bankingspringproject.exception.DataNotFoundException;
import org.crazymages.bankingspringproject.repository.AgreementRepository;
import org.crazymages.bankingspringproject.service.database.AgreementDatabaseService;
import org.crazymages.bankingspringproject.service.utils.updater.EntityUpdateService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
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


    @Override
    @Transactional
    public void create(Agreement agreement) {
        agreementRepository.save(agreement);
        log.info("agreement created");
    }

    @Override
    @Transactional
    public List<Agreement> findAll() {
        log.info("retrieving list of agreements");
        List<Agreement> agreements = agreementRepository.findAll();
        return checkListForNull(agreements);
    }

    @Override
    @Transactional
    public List<Agreement> findAllNotDeleted() {
        log.info("retrieving list of agreements");
        List<Agreement> agreements = agreementRepository.findAllNotDeleted();
        return checkListForNull(agreements);
    }

    @Override
    @Transactional
    public List<Agreement> findDeletedAccounts() {
        log.info("retrieving list of deleted agreements");
        List<Agreement> deletedAgreements = agreementRepository.findAllDeleted();
        return checkListForNull(deletedAgreements);
    }

    @Override
    @Transactional
    public Agreement findById(UUID uuid) {
        log.info("retrieving agreement by id {}", uuid);
        return agreementRepository.findById(uuid)
                .orElseThrow(() -> new DataNotFoundException(String.valueOf(uuid)));
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
    public void update(UUID uuid, Agreement agreementUpdate) {
        Agreement agreement = agreementRepository.findById(uuid)
                .orElseThrow(() -> new DataNotFoundException(String.valueOf(uuid)));
        agreement = agreementUpdateService.update(agreement, agreementUpdate);
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
    public List<Agreement> findAgreementsByManagerUuid(UUID uuid) {
        log.info("retrieving agreements by manager id {}", uuid);
        List<Agreement> agreements = agreementRepository.findAgreementsWhereManagerIdIs(uuid);
        return checkListForNull(agreements);
    }

    @Override
    @Transactional
    public List<Agreement> findAgreementsByClientUuid(UUID uuid) {
        log.info("retrieving agreements client id {}", uuid);
        List<Agreement> agreements = agreementRepository.findAgreementsWhereClientIdIs(uuid);
        return checkListForNull(agreements);
    }

    private List<Agreement> checkListForNull(List<Agreement> list) {
        return list == null ? Collections.emptyList() : list;
    }
}
