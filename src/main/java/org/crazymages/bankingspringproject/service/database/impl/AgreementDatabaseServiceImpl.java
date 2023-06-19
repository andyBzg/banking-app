package org.crazymages.bankingspringproject.service.database.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crazymages.bankingspringproject.entity.Account;
import org.crazymages.bankingspringproject.entity.Agreement;
import org.crazymages.bankingspringproject.exception.DataNotFoundException;
import org.crazymages.bankingspringproject.repository.AgreementRepository;
import org.crazymages.bankingspringproject.service.database.AgreementDatabaseService;
import org.crazymages.bankingspringproject.service.database.updater.EntityUpdateService;
import org.crazymages.bankingspringproject.service.database.updater.impl.AgreementUpdateServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AgreementDatabaseServiceImpl implements AgreementDatabaseService {

    private final AgreementRepository agreementRepository;
    private final EntityUpdateService<Agreement> agreementUpdateService;


    @Override
    public void create(Agreement agreement) {
        agreementRepository.save(agreement);
        log.info("agreement created");
    }

    @Override
    public List<Agreement> findAll() {
        log.info("retrieving list of agreements");
        return agreementRepository.findAll();
    }

    @Override
    public Agreement findById(UUID uuid) {
        log.info("retrieving agreement by id {}", uuid);
        return agreementRepository.findById(uuid)
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
        return agreementRepository.findAgreementsWhereManagerIdIs(uuid);
    }

    @Override
    @Transactional
    public List<Agreement> findAgreementsByClientUuid(UUID uuid) {
        log.info("retrieving agreements client id {}", uuid);
        return agreementRepository.findAgreementsWhereClientIdIs(uuid);
    }
}
