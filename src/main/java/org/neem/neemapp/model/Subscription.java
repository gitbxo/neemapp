package org.neem.neemapp.model;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "subscription")
public class Subscription {

	private @Id @GeneratedValue Long id;
	private LocalDateTime created;
	private LocalDateTime modified;

	private Long patientId;

	private Long planId;

	private int usedDeductible;

	private String usedOverrides;

	public Subscription() {
	}

	public Subscription(Patient patient, InsurancePlan plan) {
		this.patientId = patient.getId();
		this.planId = plan.getId();
		this.usedDeductible = 0;
		this.usedOverrides = "";
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDateTime getCreatedTime() {
		return this.created;
	}

	public void setCreatedTime(LocalDateTime created) {
		this.created = created;
	}

	public LocalDateTime getModifiedTime() {
		return this.modified;
	}

	public void setModifiedTime(LocalDateTime modified) {
		this.modified = modified;
	}

	public long getPatientId() {
		return this.patientId;
	}

	public void setPatientId(long patientId) {
		this.patientId = patientId;
	}

	public long getPlanId() {
		return this.planId;
	}

	public void setPlanId(long planId) {
		this.planId = planId;
	}

	public int getUsedDeductible() {
		// return planRepo.findById(this.planId).get().getDeductible() -
		// this.usedDeductible;
		return this.usedDeductible;
	}

	public void setUsedDeductible(int usedDeductible) {
		this.usedDeductible = usedDeductible;
	}

	public String getUsedOverrides() {
		return this.usedOverrides;
	}

	public void setUsedOverrides(String usedOverrides) {
		this.usedOverrides = usedOverrides;
	}

	public Map<InsurancePlan.MedicalType, Integer> getUsedOverridesMap() {
		Map<InsurancePlan.MedicalType, Integer> usedOverrides = InsurancePlan.getOverridesMap(this.usedOverrides);

		return usedOverrides;
	}

	@Override
	public boolean equals(Object o) {

		if (this == o)
			return true;
		if (o == null || !(o instanceof Subscription))
			return false;
		Subscription subscription = (Subscription) o;
		return Objects.equals(this.id, subscription.id) && Objects.equals(this.patientId, subscription.patientId)
				&& Objects.equals(this.planId, subscription.planId)
				&& (this.usedDeductible == subscription.usedDeductible)
				&& Objects.equals(this.usedOverrides, subscription.usedOverrides);
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.id, this.patientId, this.planId, this.usedDeductible);
	}

	@Override
	public String toString() {
		return "Subscription{" + "id=" + this.id + "," + " patient='" + String.valueOf(this.patientId) + " plan='"
				+ String.valueOf(this.planId) + '\'' + '}';
	}

}
