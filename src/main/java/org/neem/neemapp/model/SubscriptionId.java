package org.neem.neemapp.model;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@SuppressWarnings("serial")
public class SubscriptionId implements Serializable {
	private UUID patientId;
	private UUID planId;

	// default constructor

	public SubscriptionId() {
		this.patientId = UUID.randomUUID();
		this.planId = UUID.randomUUID();
	}

	public SubscriptionId(UUID patientId, UUID planId) {
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

		return this.toString().equals(((SubscriptionId) o).toString());
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
