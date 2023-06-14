package org.crazymages.bankingspringproject.service.database.impl;

import lombok.RequiredArgsConstructor;
import org.crazymages.bankingspringproject.entity.Agreement;
import org.crazymages.bankingspringproject.exception.DataNotFoundException;
import org.crazymages.bankingspringproject.repository.AgreementRepository;
import org.crazymages.bankingspringproject.service.database.AgreementDatabaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AgreementDatabaseServiceImpl implements AgreementDatabaseService {

    private final AgreementRepository agreementRepository;


    @Override
    public void create(Agreement agreement) {
        agreementRepository.save(agreement);
    }

    @Override
    public List<Agreement> findAll() {
        return agreementRepository.findAll();
    }

    @Override
    public Agreement findById(UUID uuid) {
        Optional<Agreement> agreementOptional = agreementRepository.findById(uuid);
        return agreementOptional.orElseThrow(() -> new DataNotFoundException(String.valueOf(uuid)));
    }

    @Override
    @Transactional
    public Agreement update(UUID uuid, Agreement agreementUpdate) {
        Optional<Agreement> agreementOptional = agreementRepository.findById(uuid);
        if (agreementOptional.isPresent()) {
            Agreement agreement = agreementOptional.get();
            agreement.setAccount(agreementUpdate.getAccount());
            agreement.setProduct(agreementUpdate.getProduct());
            agreement.setInterestRate(agreementUpdate.getInterestRate());
            agreement.setStatus(agreementUpdate.getStatus());
            agreement.setSum(agreementUpdate.getSum());
            agreementRepository.save(agreement);
        }
        return agreementOptional.orElseThrow(() -> new DataNotFoundException(String.valueOf(uuid)));
    }

    @Override
    public void delete(UUID uuid) {
        agreementRepository.deleteById(uuid);
    }
}
