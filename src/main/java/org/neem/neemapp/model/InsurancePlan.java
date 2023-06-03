package org.neem.neemapp.model;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

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

	private @Id @GeneratedValue Long id;
	private LocalDateTime created;
	private LocalDateTime modified;

	private String name;

	private String description;

	private int deductible;

	private String overrides;

	public InsurancePlan() {
		this.name = "New Insurance Plan";
		this.description = "New Insurance Plan";
		this.deductible = 300;
		this.overrides = "";
	}

	public InsurancePlan(String name, String description, int deductible) {
		this.name = name;
		this.description = description;
		this.deductible = deductible;
		this.overrides = "";
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

	public String getOverrides() {
		return this.overrides;
	}

	public void setOverrides(String overrides) {
		this.overrides = overrides;
	}

	public Map<InsurancePlan.MedicalType, Integer> getOverridesMap() {
		return InsurancePlan.getOverridesMap(this.overrides);
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
		return "InsurancePlan{" + "id=" + String.valueOf(this.id) + ", name='" + String.valueOf(this.name) + "', description='"
				+ String.valueOf(this.description) + "', overrides='" + String.valueOf(this.overrides)
				+ "', deductible=" + String.valueOf(this.deductible) + '}';
	}

	/*
	 * No Overrides = "" Has Overrides: "basic:10,ortho:20"
	 */

	public static String buildOverridesFromMap(Map<MedicalType, Integer> overridesMap) {
		if (overridesMap == null) {
			return "";
		}
		boolean appendComma = false;
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<MedicalType, Integer> e : overridesMap.entrySet()) {
			if (appendComma) {
				sb.append(",");
			} else {
				appendComma = true;
			}
			sb.append(e.getKey().name()).append(":").append(e.getValue().toString());
		}
		return sb.toString();
	}

	public static Map<MedicalType, Integer> getOverridesMap(String overrides) {
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
}
