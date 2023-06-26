package org.crazymages.bankingspringproject.service.database.updater;

public interface EntityUpdateService<E> {

     E update(E existing, E update);

}
