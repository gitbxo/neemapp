package org.neem.neemapp.jpa;

import org.neem.neemapp.model.InsurancePlan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanRepo extends JpaRepository<InsurancePlan, Long> {

}
