package org.neem.neemapp.jpa;

import java.util.UUID;

import org.neem.neemapp.model.InsurancePlan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InsurancePlanRepo extends JpaRepository<InsurancePlan, UUID> {

}
