package org.neem.neemapp.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;


@Entity
@Table(name = "subscription")
public class Subscription {

  private @Id @GeneratedValue Long id;

  @JoinColumn(name = "patient_id", referencedColumnName = "id")
  private Patient patient;

  @JoinColumn(name = "plan_id", referencedColumnName = "id")
  private InsurancePlan plan;

  @Column(name = "used_deductible")
  private int used_deductible;

  @Column(name = "used_overrides")
  private String used_overrides;

  public Subscription() {
  }

  public Subscription(Patient patient, InsurancePlan plan) {
	    this.patient = patient;
	    this.plan = plan;
	    this.used_deductible = 0;
	    this.used_overrides = "";
  }

  public Long getId() {
	  return this.id;
  }
  public void setId(Long id) {
	  this.id = id;
  }

  public Patient getPatient() {
	  return this.patient;
  }
  public void setPatient(Patient patient) {
	  this.patient = patient;
  }

  public InsurancePlan getPlan() {
	  return this.plan;
  }
  public void setPatient(InsurancePlan plan) {
	  this.plan = plan;
  }

  public int getDeductible() {
	  return this.plan.getDeductible() - this.used_deductible;
  }
  public void setUsedDeductible(int used_deductible) {
	  this.used_deductible = used_deductible;
  }

  public Map<InsurancePlan.MedicalType, Integer> getOverrides() {
	  Map<InsurancePlan.MedicalType, Integer> planOverrides = this.plan.getOverrides();
	  Map<InsurancePlan.MedicalType, Integer> userOverrides = InsurancePlan.getOverrides(this.used_overrides);

	  
	  return new HashMap<InsurancePlan.MedicalType, Integer>();
  }

  @Override
  public boolean equals(Object o) {

    if (this == o)
      return true;
    if (o == null || !(o instanceof Subscription))
      return false;
    Subscription subscription = (Subscription) o;
    return Objects.equals(this.id, subscription.id)
    		&& Objects.equals(this.patient, subscription.patient)
    		&& Objects.equals(this.plan, subscription.plan)
    		&& this.used_deductible == subscription.used_deductible
    		&& Objects.equals(this.used_overrides, subscription.used_overrides);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.id, this.patient, this.plan, this.used_deductible);
  }

  @Override
  public String toString() {
    return "InsurancePlan{" + "id=" + this.id + ","
    		+ " patient='" + 
    		(this.patient == null || this.patient.getName() == null ? "" : this.patient.getName())
    		+ " plan='" + 
    		(this.plan == null || this.plan.getName() == null ? "" : this.plan.getName())
    		+ '\'' + '}';
  }

}
