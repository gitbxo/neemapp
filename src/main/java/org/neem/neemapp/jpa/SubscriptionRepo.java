package org.neem.neemapp.jpa;

import org.neem.neemapp.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionRepo extends JpaRepository<Subscription, Long> {

}
