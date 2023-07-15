package org.crazymages.bankingspringproject.service.database;

import org.crazymages.bankingspringproject.dto.AgreementDto;
import org.crazymages.bankingspringproject.entity.Agreement;

import java.util.List;
import java.util.UUID;

/**
 * A service interface for managing Agreement entities in the database.
 * It provides methods for creating, retrieving, updating, and deleting Agreement entities.
 */
public interface AgreementDatabaseService {

    /**
     * Creates a new Agreement entity in the database.
     *
     * @param agreementDto The Agreement entity to be created.
     */
    void create(AgreementDto agreementDto);

    /**
     * Retrieves all non-deleted Agreement entities from the database.
     *
     * @return A list of all non-deleted Agreement entities.
     */
    List<AgreementDto> findAllNotDeleted();

    /**
     * Retrieves all deleted Agreement entities from the database.
     *
     * @return A list of all deleted Agreement entities.
     */
    List<AgreementDto> findDeletedAgreements();

    /**
     * Retrieves an Agreement entity from the database by its UUID.
     *
     * @param uuid The UUID of the Agreement to retrieve.
     * @return The Agreement entity with the specified UUID, or null if not found.
     */
    AgreementDto findById(UUID uuid);

    /**
     * Retrieves the savings Agreement entity from the database associated with the specified client UUID.
     *
     * @param clientUuid The UUID of the client to filter by.
     * @return The savings Agreement entity associated with the specified client UUID, or null if not found.
     */
    Agreement findSavingsAgreementByClientId(UUID clientUuid);

    /**
     * Updates an Agreement entity in the database with the specified UUID.
     *
     * @param uuid                The UUID of the Agreement to update.
     * @param updatedAgreementDto The updated Agreement entity.
     */
    void update(UUID uuid, AgreementDto updatedAgreementDto);

    /**
     * Deletes an Agreement entity from the database with the specified UUID.
     *
     * @param uuid The UUID of the Agreement to delete.
     */
    void delete(UUID uuid);

    /**
     * Retrieves all Agreement DTOs from the database associated with the specified manager UUID.
     *
     * @param managerUuid The UUID of the manager to filter by.
     * @return A list of Agreement entities associated with the specified manager UUID.
     */
    List<AgreementDto> findAgreementsByManagerUuid(UUID managerUuid);

    /**
     * Retrieves all AgreementDTOs from the database associated with the specified client UUID.
     *
     * @param clientUuid The UUID of the client to filter by.
     * @return A list of AgreementDTOs associated with the specified client UUID.
     */
    List<AgreementDto> findAgreementDtoListByClientUuid(UUID clientUuid);

    /**
     * Retrieves all Agreement entities from the database associated with the specified client UUID.
     *
     * @param clientUuid The UUID of the client to filter by.
     * @return A list of Agreement entities associated with the specified client UUID.
     */
    List<Agreement> findAgreementsByClientUuid(UUID clientUuid);

}
