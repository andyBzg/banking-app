package org.crazymages.bankingspringproject.service.database.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crazymages.bankingspringproject.entity.Agreement;
import org.crazymages.bankingspringproject.entity.enums.ProductType;
import org.crazymages.bankingspringproject.exception.DataNotFoundException;
import org.crazymages.bankingspringproject.repository.AgreementRepository;
import org.crazymages.bankingspringproject.service.database.AgreementDatabaseService;
import org.crazymages.bankingspringproject.service.utils.updater.EntityUpdateService;
import org.crazymages.bankingspringproject.service.utils.validator.ListValidator;
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
    private final ListValidator<Agreement> listValidator;

    /**
     * Creates a new Agreement entity and saves it to the database.
     *
     * @param agreement The Agreement entity to create.
     */
    @Override
    public void create(Agreement agreement) {
        agreementRepository.save(agreement);
        log.info("agreement created");
    }

    /**
     * Retrieves a list of all Agreement entities from the database.
     *
     * @return A list of all Agreement entities.
     */
    @Override
    @Transactional
    public List<Agreement> findAll() {
        log.info("retrieving list of agreements");
        List<Agreement> agreements = agreementRepository.findAll();
        return listValidator.validate(agreements);
    }

    /**
     * Retrieves a list of all not deleted Agreement entities from the database.
     *
     * @return A list of all not deleted Agreement entities.
     */
    @Override
    @Transactional
    public List<Agreement> findAllNotDeleted() {
        log.info("retrieving list of agreements");
        List<Agreement> agreements = agreementRepository.findAllNotDeleted();
        return listValidator.validate(agreements);
    }

    /**
     * Retrieves a list of all deleted Agreement entities from the database.
     *
     * @return A list of all deleted Agreement entities.
     */
    @Override
    @Transactional
    public List<Agreement> findDeletedAccounts() {
        log.info("retrieving list of deleted agreements");
        List<Agreement> deletedAgreements = agreementRepository.findAllDeleted();
        return listValidator.validate(deletedAgreements);
    }

    /**
     * Retrieves an Agreement entity from the database by its UUID.
     *
     * @param uuid The UUID of the Agreement entity to retrieve.
     * @return The Agreement entity with the specified UUID.
     * @throws DataNotFoundException if no Agreement entity is found with the specified UUID.
     */
    @Override
    public Agreement findById(UUID uuid) {
        log.info("retrieving agreement by id {}", uuid);
        return agreementRepository.findById(uuid)
                .orElseThrow(() -> new DataNotFoundException(String.valueOf(uuid)));
    }

    /**
     * Retrieves the savings Agreement entity associated with the specified client UUID.
     *
     * @param uuid The UUID of the client.
     * @return The savings Agreement entity associated with the specified client UUID.
     * @throws DataNotFoundException if no savings Agreement entity is found for the client.
     */
    @Override
    @Transactional
    public Agreement findSavingsAgreementByClientId(UUID uuid) {
        log.info("retrieving savings agreement by id {}", uuid);
        return agreementRepository.findAgreementByClientIdAndProductType(uuid, ProductType.SAVINGS_ACCOUNT)
                .orElseThrow(() -> new DataNotFoundException(String.valueOf(uuid)));
    }

    /**
     * Updates an existing Agreement entity in the database.
     *
     * @param uuid            The UUID of the Agreement to update.
     * @param agreementUpdate The updated Agreement entity.
     * @throws DataNotFoundException if no Agreement entity is found with the specified UUID.
     */
    @Override
    @Transactional
    public void update(UUID uuid, Agreement agreementUpdate) {
        Agreement agreement = agreementRepository.findById(uuid)
                .orElseThrow(() -> new DataNotFoundException(String.valueOf(uuid)));
        agreement = agreementUpdateService.update(agreement, agreementUpdate);
        agreementRepository.save(agreement);
        log.info("updated agreement id {}", uuid);
    }

    /**
     * Deletes an existing Agreement entity in the database.
     *
     * @param uuid The UUID of the Agreement to delete.
     * @throws DataNotFoundException if no Agreement entity is found with the specified UUID.
     */
    @Override
    @Transactional
    public void delete(UUID uuid) {
        Agreement agreement = agreementRepository.findById(uuid)
                .orElseThrow(() -> new DataNotFoundException(String.valueOf(uuid)));
        agreement.setDeleted(true);
        agreementRepository.save(agreement);
        log.info("deleted agreement id {}", uuid);
    }

    /**
     * Retrieves a list of Agreement entities associated with the specified manager UUID.
     *
     * @param uuid The UUID of the manager.
     * @return A list of Agreement entities associated with the specified manager UUID.
     */
    @Override
    @Transactional
    public List<Agreement> findAgreementsByManagerUuid(UUID uuid) {
        log.info("retrieving agreements by manager id {}", uuid);
        List<Agreement> agreements = agreementRepository.findAgreementsWhereManagerIdIs(uuid);
        return listValidator.validate(agreements);
    }

    /**
     * Retrieves a list of Agreement entities associated with the specified client UUID.
     *
     * @param uuid The UUID of the client.
     * @return A list of Agreement entities associated with the specified client UUID.
     */
    @Override
    @Transactional
    public List<Agreement> findAgreementsByClientUuid(UUID uuid) {
        log.info("retrieving agreements client id {}", uuid);
        List<Agreement> agreements = agreementRepository.findAgreementsWhereClientIdIs(uuid);
        return listValidator.validate(agreements);
    }
}
