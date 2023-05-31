package org.neem.neemapp.model;

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

	private Long patient_id;

	private Long plan_id;

	private int used_deductible;

	private String used_overrides;

	public Subscription() {
	}

	public Subscription(Patient patient, InsurancePlan plan) {
		this.patient_id = patient.getId();
		this.plan_id = plan.getId();
		this.used_deductible = 0;
		this.used_overrides = "";
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getPatientId() {
		return this.patient_id;
	}

	public void setPatientId(long patient_id) {
		this.patient_id = patient_id;
	}

	public long getPlanId() {
		return this.plan_id;
	}

	public void setPlanId(long plan_id) {
		this.plan_id = plan_id;
	}

	public int getUsedDeductible() {
		// return planRepo.findById(this.plan_id).get().getDeductible() -
		// this.used_deductible;
		return this.used_deductible;
	}

	public void setUsedDeductible(int used_deductible) {
		this.used_deductible = used_deductible;
	}

	public String getUsedOverrides() {
		return this.used_overrides;
	}

	public void setUsedOverrides(String used_overrides) {
		this.used_overrides = used_overrides;
	}

	public Map<InsurancePlan.MedicalType, Integer> getUsedOverridesMap() {
		Map<InsurancePlan.MedicalType, Integer> usedOverrides = InsurancePlan.getOverridesMap(this.used_overrides);

		return usedOverrides;
	}

	@Override
	public boolean equals(Object o) {

		if (this == o)
			return true;
		if (o == null || !(o instanceof Subscription))
			return false;
		Subscription subscription = (Subscription) o;
		return Objects.equals(this.id, subscription.id) && Objects.equals(this.patient_id, subscription.patient_id)
				&& Objects.equals(this.plan_id, subscription.plan_id)
				&& (this.used_deductible == subscription.used_deductible)
				&& Objects.equals(this.used_overrides, subscription.used_overrides);
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.id, this.patient_id, this.plan_id, this.used_deductible);
	}

	@Override
	public String toString() {
		return "Subscription{" + "id=" + this.id + "," + " patient='" + String.valueOf(this.patient_id) + " plan='"
				+ String.valueOf(this.plan_id) + '\'' + '}';
	}

}
