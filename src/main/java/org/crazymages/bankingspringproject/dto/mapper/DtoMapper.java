package org.crazymages.bankingspringproject.dto.mapper;

/**
 * An interface for mapping between entity and DTO objects.
 *
 * @param <E> Type of the entity object.
 * @param <D> Type of the DTO object.
 */
public interface DtoMapper<E, D> {

    /**
     * Maps an entity object to a DTO object.
     *
     * @param entity The entity object to be mapped.
     * @return The mapped DTO object.
     */
    D mapEntityToDto(E entity);

    /**
     * Maps a DTO object to an entity object.
     *
     * @param entityDto The DTO object to be mapped.
     * @return The mapped entity object.
     */
    E mapDtoToEntity(D entityDto);
}
