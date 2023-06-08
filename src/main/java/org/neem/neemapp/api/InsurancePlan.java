package org.neem.neemapp.api;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.neem.neemapp.api.NeemAppException.InvalidValueException;
import org.neem.neemapp.jpa.InsurancePlanRepo;
import org.neem.neemapp.model.InsurancePlan.MedicalType;

public class InsurancePlan {
	// update deductibles, will have json data:
	/*
	 * { "plan_id": 123, "deductible": 300, "overrides": { "ortho": 1000 } }
	 */

	private Long id;

	private String name;

	private String description;

	private LocalDate planStartDate;

	private LocalDate planEndDate;

	private int deductible;

	private Map<MedicalType, Integer> overrides;

	public InsurancePlan() {
		this.name = "New Insurance Plan";
		this.description = "New Insurance Plan";
		this.deductible = 300;
	}

	public InsurancePlan(Long id, String name, String description, LocalDate start, LocalDate end, int deductible,
			Map<MedicalType, Integer> overrides) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.planStartDate = start;
		this.planEndDate = end;
		this.deductible = deductible;
		this.overrides = overrides;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public int getDeductible() {
		return this.deductible;
	}

	public void setDeductible(int deductible) {
		this.deductible = deductible;
	}

	public Map<MedicalType, Integer> getOverrides() {
		return this.overrides;
	}

	public void setOverrides(Map<MedicalType, Integer> overrides) {
		this.overrides = overrides;
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
				+ "', overrides='" + org.neem.neemapp.model.InsurancePlan.buildOverridesFromEnumMap(this.overrides)
				+ "', deductible=" + String.valueOf(this.deductible) + '}';
	}

	public static InsurancePlan buildInsurancePlan(org.neem.neemapp.model.InsurancePlan db_plan) {
		return new InsurancePlan(db_plan.getId(), db_plan.getName(), db_plan.getDescription(),
				db_plan.getPlanStartDate(), db_plan.getPlanEndDate(), db_plan.getDeductible(),
				db_plan.getOverridesEnumMap());
	}

	public static InsurancePlan findByPlanId(InsurancePlanRepo planRepo, Long plan_id) {
		Optional<org.neem.neemapp.model.InsurancePlan> opt_plan = planRepo.findById(plan_id);
		if (opt_plan.isEmpty() || opt_plan.get() == null) {
			return null;
		}

		return buildInsurancePlan(opt_plan.get());
	}

	public static InsurancePlan updatePlan(InsurancePlanRepo planRepo, Long plan_id, InsurancePlan plan) {
		Optional<org.neem.neemapp.model.InsurancePlan> opt_plan = planRepo.findById(plan_id);
		if (opt_plan.isEmpty() || opt_plan.get() == null) {
			return null;
		}
		org.neem.neemapp.model.InsurancePlan db_plan = opt_plan.get();
		if (plan.equals(buildInsurancePlan(db_plan))) {
			return plan;
		}

		String plan_overrides = org.neem.neemapp.model.InsurancePlan
				.buildOverridesFromEnumMap(InsurancePlan.checkInsurancePlan(plan).getOverrides());
		db_plan.setName(plan.getName());
		db_plan.setDescription(plan.getDescription());
		db_plan.setPlanStartDate(plan.getPlanStartDate());
		db_plan.setPlanEndDate(plan.getPlanEndDate());
		db_plan.setDeductible(plan.getDeductible());
		db_plan.setOverrides(org.neem.neemapp.model.InsurancePlan.buildOverridesStrMap(plan_overrides));
		db_plan.setModifiedTime(LocalDateTime.now());
		planRepo.saveAndFlush(db_plan);

		return buildInsurancePlan(db_plan);
	}

	private static InsurancePlan checkInsurancePlan(InsurancePlan plan) {
		if (plan == null) {
			throw new InvalidValueException("Invalid value for InsurancePlan");
		}

		if (plan.getPlanStartDate() == null) {
			throw new InvalidValueException("Invalid value for planStartDate");
		}

		if (plan.getPlanEndDate() == null || plan.getPlanEndDate().compareTo(plan.getPlanStartDate()) <= 0) {
			throw new InvalidValueException("Invalid value for planEndDate");
		}

		if (plan.getDeductible() < 0) {
			throw new InvalidValueException("Invalid value for deductible");
		}

		String plan_overrides = org.neem.neemapp.model.InsurancePlan.buildOverridesFromEnumMap(plan.getOverrides());
		if (plan_overrides.contains("-")) {
			throw new InvalidValueException("Invalid value for override");
		}

		return plan;
	}

}
