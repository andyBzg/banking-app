package org.crazymages.bankingspringproject.service.utils.updater;

/**
 * An interface representing an entity update service.
 * It provides a method to update an existing entity with new data.
 *
 * @param <E> The type of entity being updated.
 */
public interface EntityUpdateService<E> {

     /**
      * Updates an existing entity with new data.
      *
      * @param existing The existing entity to be updated.
      * @param update The entity containing the updated data.
      * @return The updated entity.
      */
     E update(E existing, E update);

     /**

      Updates the properties of an existing entity with new data.
      @param existing The existing entity whose properties need to be updated.
      @param update The entity containing the updated property values.
      */
     E updateProperties(E existing, E update);

}
