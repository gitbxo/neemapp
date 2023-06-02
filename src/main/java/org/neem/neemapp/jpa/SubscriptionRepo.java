package org.neem.neemapp.jpa;

import java.util.List;

import org.neem.neemapp.model.Subscription;
import org.neem.neemapp.model.SubscriptionId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionRepo extends JpaRepository<Subscription, SubscriptionId> {

	List<Subscription> findByPatientId(Long patientId);
	List<Subscription> findByPatientIdAndPlanId(Long patientId, Long planId);

}
