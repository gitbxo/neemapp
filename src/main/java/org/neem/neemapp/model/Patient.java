package org.neem.neemapp.model;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "patient")
public class Patient {

  private @Id @GeneratedValue Long id;
  @Column(name = "name")
  private String name;

  public Patient() {}

  public Patient(String name) {
    this.name = name;
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

  /*
   * Future: Add family information
  @JoinColumn(name = "family_id", referencedColumnName = "id")
  private Family family;
  

  public Family getFamily() {
	  return this.family;
  }
  public void setFamily(Family family) {
	  this.family = family;
  }
   */

  @Override
  public boolean equals(Object o) {

    if (this == o)
      return true;
    if (o == null || !(o instanceof Patient))
      return false;
    Patient user = (Patient) o;
    return Objects.equals(this.id, user.id)
		&& Objects.equals(this.name, user.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.id, this.name);
  }

  @Override
  public String toString() {
    return "Patient{" + "id=" + this.id + ", name='" + this.name + '\'' + '}';
  }

}
