package org.neem.neemapp.api;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.neem.neemapp.jpa.InsurancePlanRepo;
import org.neem.neemapp.jpa.SubscriptionRepo;
import org.neem.neemapp.model.InsurancePlan.MedicalType;
import org.neem.neemapp.model.SubscriptionId;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

public class Subscription {
	// update deductibles, will have json data:
	/*
	 * { "plan_id": 123, "deductible": 300, "overrides": { "ortho": 1000 } }
	 */

	private Long patientId;

	private InsurancePlan plan;

	private int usedDeductible;

	private Map<MedicalType, Integer> usedOverrides;

	public Subscription(long patientId, InsurancePlan plan, int usedDeductible,
			Map<MedicalType, Integer> usedOverrides) {
		this.patientId = patientId;
		this.plan = plan;
		this.usedDeductible = usedDeductible;
		this.usedOverrides = usedOverrides;
	}

	public Long getPatientId() {
		return this.patientId;
	}

	public void setPatientId(Long patientId) {
		this.patientId = patientId;
	}

	public InsurancePlan getPlan() {
		return this.plan;
	}

	public void setPlan(InsurancePlan plan) {
		this.plan = plan;
	}

	public int getUsedDeductible() {
		return this.usedDeductible;
	}

	public void setUsedDeductible(int usedDeductible) {
		this.usedDeductible = usedDeductible;
	}

	public Map<MedicalType, Integer> getUsedOverrides() {
		return this.usedOverrides;
	}

	public void setUsedOverrides(Map<MedicalType, Integer> usedOverrides) {
		this.usedOverrides = usedOverrides;
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
		return "Subscription{" + " patientId='" + String.valueOf(this.patientId) + "', plan='"
				+ String.valueOf(this.plan) + "', usedOverrides='"
				+ org.neem.neemapp.model.InsurancePlan.buildOverridesFromMap(this.usedOverrides) + "', usedDeductible="
				+ String.valueOf(this.usedDeductible) + '}';
	}

	public static Subscription buildSubscription(org.neem.neemapp.model.Subscription db_subscription,
			InsurancePlan plan) {
		return new Subscription(db_subscription.getPatientId(), plan, db_subscription.getUsedDeductible(),
				db_subscription.getUsedOverridesMap());
	}

	public static Subscription findByPatientIdAndPlan(SubscriptionRepo subscriptionRepo, Long patient_id,
			InsurancePlan plan) {
		Optional<org.neem.neemapp.model.Subscription> opt_subscription = subscriptionRepo
				.findById(new SubscriptionId(patient_id, plan.getId()));
		if (opt_subscription.isEmpty() || opt_subscription.get() == null) {
			return null;
		}

		return buildSubscription(opt_subscription.get(), plan);
	}

	public static Subscription findByPatientIdAndPlanId(SubscriptionRepo subscriptionRepo, InsurancePlanRepo planRepo,
			Long patient_id, Long plan_id) {
		InsurancePlan plan = InsurancePlan.findByPlanId(planRepo, plan_id);
		return findByPatientIdAndPlan(subscriptionRepo, patient_id, plan);
	}

	public static Subscription updateSubscription(SubscriptionRepo subscriptionRepo, Long patient_id,
			InsurancePlan plan, Subscription subscription) {
		Optional<org.neem.neemapp.model.Subscription> opt_subscription = subscriptionRepo
				.findById(new SubscriptionId(patient_id, plan.getId()));
		if (opt_subscription.isEmpty() || opt_subscription.get() == null) {
			return null;
		}
		org.neem.neemapp.model.Subscription db_subscription = opt_subscription.get();
		String subscription_overrides = org.neem.neemapp.model.InsurancePlan
				.buildOverridesFromMap(subscription.getUsedOverrides());
		if (db_subscription.getUsedDeductible() != subscription.getUsedDeductible()
				|| !subscription_overrides.equals(db_subscription.getUsedOverrides())) {

			db_subscription.setUsedDeductible(subscription.getUsedDeductible());
			db_subscription.setUsedOverrides(subscription_overrides);
			db_subscription.setModifiedTime(LocalDateTime.now());
			subscriptionRepo.saveAndFlush(db_subscription);
		}

		return buildSubscription(db_subscription, plan);
	}

	public static Subscription updateSubscription(SubscriptionRepo subscriptionRepo, InsurancePlanRepo planRepo,
			Long patient_id, Long plan_id, Subscription subscription) {
		InsurancePlan plan = InsurancePlan.findByPlanId(planRepo, plan_id);
		return updateSubscription(subscriptionRepo, patient_id, plan, subscription);
	}

	public static class SubscriptionNotFoundException extends RuntimeException {

		private static final long serialVersionUID = 101L;

		SubscriptionNotFoundException(Long patient_id, Long plan_id) {
			super("Could not find subscription for ( " + patient_id + " , " + plan_id + " )");
		}
	}

	@ControllerAdvice
	public static class SubscriptionNotFoundAdvice {

		@ResponseBody
		@ExceptionHandler(SubscriptionNotFoundException.class)
		@ResponseStatus(HttpStatus.NOT_FOUND)
		String notFoundHandler(SubscriptionNotFoundException ex) {
			return ex.getMessage();
		}
	}
}
