package org.neem.neemapp.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import org.hibernate.annotations.Type;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;

@Entity
@Table(name = "insurance_plan")
public class InsurancePlan {
	// update deductibles, will have json data:
	/*
	 * { "plan_id": 123, "deductible": 300, "overrides": { "ortho": 1000 } }
	 */

	public enum MedicalType {
		basic, preventive, ortho, major, other
	}

	@Id
	@GeneratedValue
	@Column(columnDefinition = "uuid", updatable = false)
	private UUID id;

	private LocalDateTime created;
	private LocalDateTime modified;
	private LocalDate planStartDate;
	private LocalDate planEndDate;

	private String name;

	private String description;

	private int deductible;

	@Type(JsonBinaryType.class)
	@Column(name = "overrides")
	private HashMap<String, Integer> overrides;

	public InsurancePlan() {
		this.name = "New Insurance Plan";
		this.description = "New Insurance Plan";
		this.deductible = 300;
		this.overrides = null;
	}

	public InsurancePlan(String name, String description, int deductible) {
		this.name = name;
		this.description = description;
		this.deductible = deductible;
		this.overrides = null;
	}

	public UUID getId() {
		return this.id;
	}

	public void setId(UUID id) {
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

	public LocalDate getPlanStartDate() {
		return this.planStartDate;
	}

	public void setPlanStartDate(LocalDate planStartDate) {
		this.planStartDate = planStartDate;
	}

	public LocalDate getPlanEndDate() {
		return this.planEndDate;
	}

	public void setPlanEndDate(LocalDate planEndDate) {
		this.planEndDate = planEndDate;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getDeductible() {
		return this.deductible;
	}

	public void setDeductible(int deductible) {
		this.deductible = deductible;
	}

	public HashMap<String, Integer> getOverrides() {
		return this.overrides;
	}

	public void setOverrides(HashMap<String, Integer> overrides) {
		this.overrides = overrides;
	}

	public String getOverridesStr() {
		return InsurancePlan.buildOverridesFromStrMap(this.getOverrides());
	}

	public Map<InsurancePlan.MedicalType, Integer> getOverridesEnumMap() {
		return InsurancePlan.buildOverridesEnumMap(this.getOverridesStr());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || !(o instanceof InsurancePlan))
			return false;

		return this.toString().equals(((InsurancePlan) o).toString());
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.toString());
	}

	@Override
	public String toString() {
		return "InsurancePlan{" + "id=" + String.valueOf(this.id) + ", name='" + String.valueOf(this.name)
				+ "', description='" + String.valueOf(this.description) + "', planStart='"
				+ String.valueOf(this.planStartDate) + "', planEnd='" + String.valueOf(this.planEndDate)
				+ "', overrides='" + String.valueOf(this.overrides) + "', deductible=" + String.valueOf(this.deductible)
				+ '}';
	}

	/*
	 * No Overrides = "" Has Overrides: "basic:10,ortho:20"
	 */

	public static String buildOverridesFromStrMap(Map<String, Integer> overridesMap) {
		if (overridesMap == null) {
			return "";
		}
		boolean appendComma = false;
		StringBuilder sb = new StringBuilder();
		for (MedicalType t : MedicalType.values()) {
			if (!overridesMap.containsKey(t.name())) {
				continue;
			}

			if (appendComma) {
				sb.append(",");
			} else {
				appendComma = true;
			}
			sb.append(t.name()).append(":").append(String.valueOf(overridesMap.get(t.name())));
		}
		return sb.toString();
	}

	public static HashMap<String, Integer> buildOverridesStrMap(String overrides) {
		HashMap<String, Integer> overridesMap = new HashMap<>();
		if (overrides == null) {
			return overridesMap;
		}
		for (MedicalType t : MedicalType.values()) {
			String[] s_split = overrides.split(t.name() + ":");
			if (s_split.length == 2) {
				String[] next_split = s_split[1].split(",");
				overridesMap.put(t.name(), Integer.valueOf(next_split[0]));
			}
		}

		return overridesMap;
	}

	public static String buildOverridesFromEnumMap(Map<MedicalType, Integer> overridesMap) {
		if (overridesMap == null) {
			return "";
		}
		boolean appendComma = false;
		StringBuilder sb = new StringBuilder();
		for (MedicalType t : MedicalType.values()) {
			if (!overridesMap.containsKey(t)) {
				continue;
			}

			if (appendComma) {
				sb.append(",");
			} else {
				appendComma = true;
			}
			sb.append(t.name()).append(":").append(String.valueOf(overridesMap.get(t)));
		}
		return sb.toString();
	}

	public static Map<MedicalType, Integer> buildOverridesEnumMap(String overrides) {
		Map<MedicalType, Integer> overridesMap = new HashMap<>();
		if (overrides == null) {
			return overridesMap;
		}
		for (MedicalType t : MedicalType.values()) {
			String[] s_split = overrides.split(t.name() + ":");
			if (s_split.length == 2) {
				String[] next_split = s_split[1].split(",");
				overridesMap.put(t, Integer.valueOf(next_split[0]));
			}
		}

		return overridesMap;
	}

	public static Map<MedicalType, Integer> computeRemainingEnumMap(Map<MedicalType, Integer> overrides,
			Map<MedicalType, Integer> usedOverrides) {
		Map<MedicalType, Integer> overridesMap = new HashMap<>();
		if (overrides == null) {
			return overridesMap;
		}
		for (MedicalType t : MedicalType.values()) {
			if (!overrides.containsKey(t))
				continue;
			if (usedOverrides != null && usedOverrides.containsKey(t)) {
				overridesMap.put(t, overrides.get(t) - usedOverrides.get(t));
			} else {
				overridesMap.put(t, overrides.get(t));
			}
		}

		return overridesMap;
	}
}
