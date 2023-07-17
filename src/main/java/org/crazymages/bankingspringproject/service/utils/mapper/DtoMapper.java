package org.crazymages.bankingspringproject.service.utils.mapper;

import java.util.List;


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

    /**
     * Maps a list of entity objects to a list of DTO objects.
     *
     * @param entityList The list of entity objects to be mapped.
     * @return The mapped list of DTO objects.
     */
    List<D> getDtoList(List<E> entityList);
}
