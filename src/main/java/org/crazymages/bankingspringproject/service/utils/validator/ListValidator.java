package org.crazymages.bankingspringproject.service.utils.validator;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

@Component
public class ListValidator<E> {

    public List<E> validate(List<E> list) {
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        return list;
    }
}
