package org.neem.neemapp.jpa;

import java.util.List;

import org.neem.neemapp.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionRepo extends JpaRepository<Subscription, Long> {

	List<Subscription> findByPatientId(Long patientId);

}
