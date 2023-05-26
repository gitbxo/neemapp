package org.neem.neemapp.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "plan")
public class InsurancePlan {
	// update deductibles, will have json data:
	/*
	 * { "plan_id" = 123, "deductible"= 300, { "ortho" = 1000 } 
	 */
	
  public enum MedicalType {
	  basic,
	  preventive,
	  ortho,
	  major,
	  other
  }

  private @Id @GeneratedValue Long id;
  @Column(name = "name")
  private String name;

  @Column(name = "description")
  private String description;

  @Column(name = "deductible")
  private int deductible;

  @Column(name = "overrides")
  private String overrides;

  public InsurancePlan() {
	  this.name = "New Insurance Plan";
	  this.description = "New Insurance Plan";
	  this.deductible = 300;
  }

  public InsurancePlan(String name, String description, int deductible) {
	    this.name = name;
	    this.description = description;
	    this.deductible = deductible;
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


  public int getDeductible() {
	  return this.deductible;
  }
  public void setDeductible(int deductible) {
	  this.deductible = deductible;
  }

  public Map<MedicalType, Integer> getOverrides() {
	  return InsurancePlan.getOverrides(this.overrides);
  }

  public void setOverrides(MedicalType usage_type, int usage_deductible) {
	  if (usage_deductible < 0) {
		  return;
	  }
	  Map<MedicalType, Integer> overridesMap = InsurancePlan.getOverrides(this.overrides);
	  overridesMap.put(usage_type, usage_deductible);
	  this.overrides = InsurancePlan.createOverrides(overridesMap);
  }

@Override
  public boolean equals(Object o) {

    if (this == o)
      return true;
    if (o == null || !(o instanceof Patient))
      return false;
    InsurancePlan plan = (InsurancePlan) o;
    return Objects.equals(this.id, plan.id)
		&& Objects.equals(this.name, plan.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.id, this.name, this.deductible,
    		(this.overrides == null ? "" : this.overrides));
  }

  @Override
  public String toString() {
    return "InsurancePlan{" + "id=" + this.id + ", name='" + this.name + '\'' + '}';
  }

  /*
   * No Overrides = ""
   * Has Overrides:
   * "basic:10,ortho:20"
   */

  public static String createOverrides(Map<MedicalType, Integer> overridesMap) {
	  if (overridesMap == null) {
		  return "";
	  }
	  boolean appendComma = false;
	  StringBuilder sb = new StringBuilder();
	  for (Map.Entry<MedicalType, Integer> e : overridesMap.entrySet()) {
		  sb.append(e.getKey().name())
		  	.append(":")
		  	.append(e.getValue().toString());
		  if (appendComma) {
			  sb.append(",");
		  } else {
			  appendComma = true;
		  }
	  }
	  return sb.toString();
  }

  public static Map<MedicalType, Integer> getOverrides(String overrides) {
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
