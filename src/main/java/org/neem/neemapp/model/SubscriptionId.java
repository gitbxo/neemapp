package org.neem.neemapp.model;

import java.io.Serializable;
import java.util.Objects;

@SuppressWarnings("serial")
public class SubscriptionId implements Serializable {
	private Long patientId;
	private Long planId;

	// default constructor

	public SubscriptionId() {
		this.patientId = -1L;
		this.planId = -1L;
	}

	public SubscriptionId(Long patientId, Long planId) {
		this.patientId = patientId;
		this.planId = planId;
	}

	// equals() and hashCode()

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || !(o instanceof SubscriptionId))
			return false;
		SubscriptionId subscription = (SubscriptionId) o;
		return this.toString().equals(subscription.toString());
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.toString());
	}

	@Override
	public String toString() {
		return "SubscriptionId{" + "patientId=" + String.valueOf(this.patientId) + " planId="
				+ String.valueOf(this.planId) + '}';
	}

}
