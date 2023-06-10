package org.neem.neemapp.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

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

	private @Id UUID patientId;

	private @Id UUID planId;

	private LocalDateTime created;

	private LocalDateTime modified;

	private LocalDate coverageStartDate;

	private LocalDate coverageEndDate;

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

	public LocalDate getCoverageStartDate() {
		return this.coverageStartDate;
	}

	public void setCoverageStartDate(LocalDate coverageStartDate) {
		this.coverageStartDate = coverageStartDate;
	}

	public LocalDate getCoverageEndDate() {
		return this.coverageEndDate;
	}

	public void setCoverageEndDate(LocalDate coverageEndDate) {
		this.coverageEndDate = coverageEndDate;
	}

	public UUID getPatientId() {
		return this.patientId;
	}

	public void setPatientId(UUID patientId) {
		this.patientId = patientId;
	}

	public UUID getPlanId() {
		return this.planId;
	}

	public void setPlanId(UUID planId) {
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

	public Map<InsurancePlan.MedicalType, Integer> getUsedOverridesEnumMap() {
		return InsurancePlan.buildOverridesEnumMap(this.getUsedOverridesStr());
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
				+ String.valueOf(this.planId) + "', coverageStart='" + String.valueOf(this.coverageStartDate)
				+ "', coverageEnd='" + String.valueOf(this.coverageEndDate) + "', usedOverrides='"
				+ this.getUsedOverridesStr() + "', usedDeductible=" + String.valueOf(this.usedDeductible) + '}';
	}

}
