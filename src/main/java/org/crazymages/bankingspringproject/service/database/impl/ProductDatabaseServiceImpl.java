package org.crazymages.bankingspringproject.service.database.impl;

import lombok.RequiredArgsConstructor;
import org.crazymages.bankingspringproject.repository.ProductRepository;
import org.crazymages.bankingspringproject.service.database.ProductDatabaseService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductDatabaseServiceImpl implements ProductDatabaseService {

    private final ProductRepository productRepository;

}
