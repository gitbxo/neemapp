package org.neem.neemapp.jpa;

import java.util.List;
import java.util.UUID;

import org.neem.neemapp.model.Subscription;
import org.neem.neemapp.model.SubscriptionId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionRepo extends JpaRepository<Subscription, SubscriptionId> {

	List<Subscription> findByPatientId(UUID patientId);

}
