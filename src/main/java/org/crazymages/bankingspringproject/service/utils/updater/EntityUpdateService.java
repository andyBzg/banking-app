package org.crazymages.bankingspringproject.service.utils.updater;

public interface EntityUpdateService<E> {

     E update(E existing, E update);

}
