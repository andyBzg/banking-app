package org.crazymages.bankingspringproject.service.utils.validator;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
 * A class representing a validator for lists.
 * Allows checking and validating lists.
 *
 * @param <E> The type of elements in the list.
 */
@Component
public class ListValidator<E> {

    /**
     * Checks and validates the list.
     * If the list is empty or null, it returns an empty list.
     * Otherwise, it returns the original list without any changes.
     *
     * @param list The list to validate.
     * @return An empty list if the input list is empty, otherwise the original list unchanged.
     */
    public List<E> validate(List<E> list) {
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        return list;
    }
}
