package com.fbo.financaspessoais.repositories;

import com.fbo.financaspessoais.model.BillingRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillingRepository extends JpaRepository<BillingRecord, Long> {
}
