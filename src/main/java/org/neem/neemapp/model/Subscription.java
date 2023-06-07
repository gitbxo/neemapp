package org.neem.neemapp.model;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import com.vladmihalcea.hibernate.type.json.JsonStringType;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;

@Entity
@IdClass(SubscriptionId.class)
@Table(name = "subscription")
@TypeDefs({ @TypeDef(name = "json", typeClass = JsonStringType.class),
		@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class) })
public class Subscription {

	private @Id Long patientId;

	private @Id Long planId;

	private LocalDateTime created;

	private LocalDateTime modified;

	private int usedDeductible;

	@Type(type = "jsonb")
	@Column(name = "used_overrides", columnDefinition = "json")
	private HashMap<String, Integer> usedOverrides;

	public Subscription() {
	}

	public Subscription(Patient patient, InsurancePlan plan) {
		this.patientId = patient.getId();
		this.planId = plan.getId();
		this.usedDeductible = 0;
		this.usedOverrides = null;
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
		return this.usedDeductible;
	}

	public void setUsedDeductible(int usedDeductible) {
		this.usedDeductible = usedDeductible;
	}

	public HashMap<String, Integer> getUsedOverrides() {
		return this.usedOverrides;
	}

	public void setUsedOverrides(HashMap<String, Integer> usedOverrides) {
		this.usedOverrides = usedOverrides;
	}

	public String getUsedOverridesStr() {
		return InsurancePlan.buildOverridesFromStrMap(this.getUsedOverrides());
	}

	public Map<InsurancePlan.MedicalType, Integer> getUsedOverridesMap() {
		Map<InsurancePlan.MedicalType, Integer> usedOverrides = InsurancePlan
				.buildOverridesEnumMap(this.getUsedOverridesStr());

		return usedOverrides;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || !(o instanceof Subscription))
			return false;

		return this.toString().equals(((Subscription) o).toString());
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.toString());
	}

	@Override
	public String toString() {
		return "Subscription{" + " patientId='" + String.valueOf(this.patientId) + "', planId='"
				+ String.valueOf(this.planId) + "', usedOverrides='" + this.getUsedOverridesStr() + "', usedDeductible="
				+ String.valueOf(this.usedDeductible) + '}';
	}

}
