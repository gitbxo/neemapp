package org.neem.neemapp.api;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.neem.neemapp.jpa.InsurancePlanRepo;
import org.neem.neemapp.model.InsurancePlan.MedicalType;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

public class InsurancePlan {
	// update deductibles, will have json data:
	/*
	 * { "plan_id": 123, "deductible": 300, "overrides": { "ortho": 1000 } }
	 */

	private Long id;

	private String name;

	private String description;

	private int deductible;

	private Map<MedicalType, Integer> overrides;

	public InsurancePlan() {
		this.name = "New Insurance Plan";
		this.description = "New Insurance Plan";
		this.deductible = 300;
	}

	public InsurancePlan(Long id, String name, String description, int deductible,
			Map<MedicalType, Integer> overrides) {
		this.id = id;
		this.name = name;
		this.description = description;
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
		InsurancePlan plan = (InsurancePlan) o;
		return Objects.equals(this.id, plan.id) && Objects.equals(this.name, plan.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.id, this.name, this.deductible, (this.overrides == null ? "" : this.overrides));
	}

	@Override
	public String toString() {
		return "InsurancePlan{" + "id=" + this.id + ", name='" + this.name + '\'' + '}';
	}

	public static InsurancePlan findByPlanId(InsurancePlanRepo planRepo, Long plan_id) {
		Optional<org.neem.neemapp.model.InsurancePlan> opt_plan = planRepo.findById(plan_id);
		if (opt_plan.isEmpty() || opt_plan.get() == null) {
			return null;
		}
		org.neem.neemapp.model.InsurancePlan db_plan = opt_plan.get();
		return new InsurancePlan(db_plan.getId(), db_plan.getName(), db_plan.getDescription(), db_plan.getDeductible(),
				db_plan.getOverridesMap());
	}

	public static InsurancePlan updatePlan(InsurancePlanRepo planRepo, Long plan_id, InsurancePlan plan) {
		Optional<org.neem.neemapp.model.InsurancePlan> opt_plan = planRepo.findById(plan_id);
		if (opt_plan.isEmpty() || opt_plan.get() == null) {
			return null;
		}
		org.neem.neemapp.model.InsurancePlan db_plan = opt_plan.get();
		db_plan.setName(plan.getName());
		db_plan.setDescription(plan.getDescription());
		db_plan.setDeductible(plan.getDeductible());
		db_plan.setOverrides(org.neem.neemapp.model.InsurancePlan.buildOverridesFromMap(plan.getOverrides()));
		planRepo.saveAndFlush(db_plan);

		return new InsurancePlan(db_plan.getId(), db_plan.getName(), db_plan.getDescription(), db_plan.getDeductible(),
				db_plan.getOverridesMap());
	}

	public static class PlanNotFoundException extends RuntimeException {

		private static final long serialVersionUID = 101L;

		PlanNotFoundException(Long id) {
			super("Could not find plan " + id);
		}
	}

	@ControllerAdvice
	public static class PlanNotFoundAdvice {

		@ResponseBody
		@ExceptionHandler(PlanNotFoundException.class)
		@ResponseStatus(HttpStatus.NOT_FOUND)
		String notFoundHandler(PlanNotFoundException ex) {
			return ex.getMessage();
		}
	}
}