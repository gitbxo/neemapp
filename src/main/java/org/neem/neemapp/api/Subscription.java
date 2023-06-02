package org.neem.neemapp.api;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.neem.neemapp.jpa.SubscriptionRepo;
import org.neem.neemapp.model.InsurancePlan.MedicalType;
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

	private Long id;

	private Long patientId;

	private Long planId;

	private int usedDeductible;

	private Map<MedicalType, Integer> usedOverrides;

	public Subscription(Long id, long patientId, long planId, int usedDeductible,
			Map<MedicalType, Integer> usedOverrides) {
		this.id = id;
		this.patientId = patientId;
		this.planId = planId;
		this.usedDeductible = usedDeductible;
		this.usedOverrides = usedOverrides;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getPatientId() {
		return this.patientId;
	}

	public void setPatientId(Long patientId) {
		this.patientId = patientId;
	}

	public Long getPlanId() {
		return this.planId;
	}

	public void setPlanId(Long planId) {
		this.planId = planId;
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
		Subscription subscription = (Subscription) o;
		return Objects.equals(this.id, subscription.id) && Objects.equals(this.patientId, subscription.patientId)
				&& Objects.equals(this.planId, subscription.planId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.id, this.patientId, this.planId,
				(this.usedOverrides == null ? "" : this.usedOverrides));
	}

	@Override
	public String toString() {
		return "Subscription{" + "id=" + this.id + ", patientId=" + this.patientId + ", planId=" + this.planId + '}';
	}

	public static Subscription findByPatientIdAndPlanId(SubscriptionRepo subscriptionRepo, Long patient_id,
			Long plan_id) {
		List<org.neem.neemapp.model.Subscription> subscription_list = subscriptionRepo
				.findByPatientIdAndPlanId(patient_id, plan_id);
		if (subscription_list == null || subscription_list.isEmpty()) {
			return null;
		}
		org.neem.neemapp.model.Subscription db_subscription = subscription_list.get(0);
		return new Subscription(db_subscription.getId(), db_subscription.getPatientId(), db_subscription.getPlanId(),
				db_subscription.getUsedDeductible(), db_subscription.getUsedOverridesMap());
	}

	public static Subscription updateSubscription(SubscriptionRepo subscriptionRepo, Long patient_id, Long plan_id,
			Subscription subscription) {
		List<org.neem.neemapp.model.Subscription> subscription_list = subscriptionRepo
				.findByPatientIdAndPlanId(patient_id, plan_id);
		if (subscription_list == null || subscription_list.isEmpty()) {
			return null;
		}
		org.neem.neemapp.model.Subscription db_subscription = subscription_list.get(0);
		String subscription_overrides = org.neem.neemapp.model.InsurancePlan
				.buildOverridesFromMap(subscription.getUsedOverrides());
		if (db_subscription.getUsedDeductible() != subscription.getUsedDeductible()
				|| !subscription_overrides.equals(db_subscription.getUsedOverrides())) {

			db_subscription.setUsedDeductible(subscription.getUsedDeductible());
			db_subscription.setUsedOverrides(subscription_overrides);
			db_subscription.setModifiedTime(LocalDateTime.now(ZoneId.of("UTC")));
			subscriptionRepo.saveAndFlush(db_subscription);
		}

		return new Subscription(db_subscription.getId(), db_subscription.getPatientId(), db_subscription.getPlanId(),
				db_subscription.getUsedDeductible(), db_subscription.getUsedOverridesMap());
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
