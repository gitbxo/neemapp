package org.neem.neemapp.api;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.neem.neemapp.api.NeemAppException.InvalidValueException;
import org.neem.neemapp.jpa.InsurancePlanRepo;
import org.neem.neemapp.jpa.SubscriptionRepo;
import org.neem.neemapp.model.SubscriptionId;
import org.neem.neemapp.model.InsurancePlan.MedicalType;

public class Subscription {
	// update deductibles, will have json data:
	/*
	 * { "plan_id": 123, "deductible": 300, "overrides": { "ortho": 1000 } }
	 */

	private String patientId;

	private InsurancePlan plan;

	private LocalDate coverageStartDate;

	private LocalDate coverageEndDate;

	private int usedDeductible;

	private Map<MedicalType, Integer> usedOverrides;

	public Subscription(String patientId, InsurancePlan plan, LocalDate start, LocalDate end, int usedDeductible,
			Map<MedicalType, Integer> usedOverrides) {
		this.patientId = patientId;
		this.plan = plan;
		this.setCoverageStartDate(start);
		this.setCoverageEndDate(end);
		this.usedDeductible = usedDeductible;
		this.usedOverrides = usedOverrides;
	}

	public String getPatientId() {
		return this.patientId;
	}

	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}

	public InsurancePlan getPlan() {
		return this.plan;
	}

	public void setPlan(InsurancePlan plan) {
		this.plan = plan;
	}

	public LocalDate getCoverageStartDate() {
		return coverageStartDate;
	}

	public void setCoverageStartDate(LocalDate coverageStartDate) {
		this.coverageStartDate = coverageStartDate;
	}

	public LocalDate getCoverageEndDate() {
		return coverageEndDate;
	}

	public void setCoverageEndDate(LocalDate coverageEndDate) {
		this.coverageEndDate = coverageEndDate;
	}

	public int getUsedDeductible() {
		return this.usedDeductible;
	}

	public void setUsedDeductible(int usedDeductible) {
		this.usedDeductible = usedDeductible;
	}

	public int getRemainingDeductible() {
		return (this.plan == null ? 0 : this.plan.getDeductible()) - this.usedDeductible;
	}

	public Map<MedicalType, Integer> getUsedOverrides() {
		return this.usedOverrides;
	}

	public Map<MedicalType, Integer> getRemainingOverrides() {
		if (this.plan == null) {
			return null;
		}
		return org.neem.neemapp.model.InsurancePlan.computeRemainingEnumMap(this.plan.getOverrides(),
				this.getUsedOverrides());
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
				+ String.valueOf(this.plan) + "', coverageStart='" + String.valueOf(this.getCoverageStartDate())
				+ "', coverageEnd='" + String.valueOf(this.getCoverageEndDate()) + "', usedOverrides='"
				+ org.neem.neemapp.model.InsurancePlan.buildOverridesFromEnumMap(this.getUsedOverrides())
				+ "', usedDeductible=" + String.valueOf(this.usedDeductible) + '}';
	}

	public static Subscription buildSubscription(org.neem.neemapp.model.Subscription db_subscription,
			InsurancePlan plan) {
		return new Subscription(String.valueOf(db_subscription.getPatientId()), plan,
				db_subscription.getCoverageStartDate(), db_subscription.getCoverageEndDate(),
				db_subscription.getUsedDeductible(), db_subscription.getUsedOverridesEnumMap());
	}

	public static Subscription findByPatientIdAndPlan(SubscriptionRepo subscriptionRepo, String patient_id,
			InsurancePlan plan) {
		Optional<org.neem.neemapp.model.Subscription> opt_subscription = subscriptionRepo
				.findById(new SubscriptionId(UUID.fromString(patient_id), UUID.fromString(plan.getId())));
		if (opt_subscription.isEmpty() || opt_subscription.get() == null) {
			return null;
		}

		return buildSubscription(opt_subscription.get(), plan);
	}

	public static Subscription findByPatientIdAndPlanId(SubscriptionRepo subscriptionRepo, InsurancePlanRepo planRepo,
			String patient_id, String plan_id) {
		InsurancePlan plan = InsurancePlan.findByPlanId(planRepo, plan_id);
		return findByPatientIdAndPlan(subscriptionRepo, patient_id, plan);
	}

	public static Subscription createSubscription(SubscriptionRepo subscriptionRepo, String patient_id,
			InsurancePlan plan, Subscription subscription) {
		Optional<org.neem.neemapp.model.Subscription> opt_subscription = subscriptionRepo
				.findById(new SubscriptionId(UUID.fromString(patient_id), UUID.fromString(plan.getId())));
		if (!opt_subscription.isEmpty()) {
			throw new InvalidValueException("Duplicate Subscription");
		}

		String subscription_overrides = org.neem.neemapp.model.InsurancePlan
				.buildOverridesFromEnumMap(Subscription.checkSubscription(subscription).getUsedOverrides());
		org.neem.neemapp.model.Subscription db_subscription = new org.neem.neemapp.model.Subscription();

		db_subscription.setPatientId(UUID.fromString(patient_id));
		db_subscription.setPlanId(UUID.fromString(plan.getId()));
		db_subscription.setCoverageStartDate(subscription.getCoverageStartDate());
		db_subscription.setCoverageEndDate(subscription.getCoverageEndDate());
		db_subscription.setUsedDeductible(subscription.getUsedDeductible());
		db_subscription
				.setUsedOverrides(org.neem.neemapp.model.InsurancePlan.buildOverridesStrMap(subscription_overrides));
		db_subscription.setCreatedTime(LocalDateTime.now());
		db_subscription.setModifiedTime(LocalDateTime.now());
		subscriptionRepo.saveAndFlush(db_subscription);

		return buildSubscription(db_subscription, plan);
	}

	public static Subscription createSubscription(SubscriptionRepo subscriptionRepo, InsurancePlanRepo planRepo,
			String patient_id, String plan_id, Subscription subscription) {
		InsurancePlan plan = InsurancePlan.findByPlanId(planRepo, plan_id);
		subscription.setPlan(plan);
		return createSubscription(subscriptionRepo, patient_id, plan, subscription);
	}

	public static Subscription updateSubscription(SubscriptionRepo subscriptionRepo, String patient_id,
			InsurancePlan plan, Subscription subscription) {
		Optional<org.neem.neemapp.model.Subscription> opt_subscription = subscriptionRepo
				.findById(new SubscriptionId(UUID.fromString(patient_id), UUID.fromString(plan.getId())));
		if (opt_subscription.isEmpty() || opt_subscription.get() == null) {
			return null;
		}
		org.neem.neemapp.model.Subscription db_subscription = opt_subscription.get();
		String subscription_overrides = org.neem.neemapp.model.InsurancePlan
				.buildOverridesFromEnumMap(Subscription.checkSubscription(subscription).getUsedOverrides());
		if (!String.valueOf(subscription.getCoverageStartDate())
				.equals(String.valueOf(db_subscription.getCoverageStartDate()))
				|| !String.valueOf(subscription.getCoverageEndDate())
						.equals(String.valueOf(db_subscription.getCoverageEndDate()))
				|| subscription.getUsedDeductible() != db_subscription.getUsedDeductible()
				|| !subscription_overrides.equals(db_subscription.getUsedOverridesStr())) {

			db_subscription.setCoverageStartDate(subscription.getCoverageStartDate());
			db_subscription.setCoverageEndDate(subscription.getCoverageEndDate());
			db_subscription.setUsedDeductible(subscription.getUsedDeductible());
			db_subscription.setUsedOverrides(
					org.neem.neemapp.model.InsurancePlan.buildOverridesStrMap(subscription_overrides));
			db_subscription.setModifiedTime(LocalDateTime.now());
			subscriptionRepo.saveAndFlush(db_subscription);
		}

		return buildSubscription(db_subscription, plan);
	}

	public static Subscription updateSubscription(SubscriptionRepo subscriptionRepo, InsurancePlanRepo planRepo,
			String patient_id, String plan_id, Subscription subscription) {
		InsurancePlan plan = InsurancePlan.findByPlanId(planRepo, plan_id);
		subscription.setPlan(plan);
		return updateSubscription(subscriptionRepo, patient_id, plan, subscription);
	}

	private static Subscription checkSubscription(Subscription subscription) {
		if (subscription == null || subscription.getPlan() == null) {
			throw new InvalidValueException("Invalid value for Subscription");
		}

		if (subscription.getCoverageStartDate() == null
				|| subscription.getCoverageStartDate().compareTo(subscription.getPlan().getPlanStartDate()) < 0) {
			throw new InvalidValueException("Invalid value for coverageStartDate");
		}

		if (subscription.getCoverageEndDate() == null
				|| subscription.getCoverageEndDate().compareTo(subscription.getCoverageStartDate()) <= 0
				|| subscription.getCoverageEndDate().compareTo(subscription.getPlan().getPlanEndDate()) > 0) {
			throw new InvalidValueException("Invalid value for coverageEndDate");
		}

		if (subscription.getRemainingDeductible() < 0) {
			throw new InvalidValueException("Invalid value for deductible");
		}

		String remaining_overrides = org.neem.neemapp.model.InsurancePlan
				.buildOverridesFromEnumMap(subscription.getRemainingOverrides());
		if (remaining_overrides.contains("-")) {
			throw new InvalidValueException("Invalid value for override");
		}

		return subscription;
	}

}
