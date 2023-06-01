package org.neem.neemapp.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "patient")
public class Patient {

	private @Id @GeneratedValue Long id;
	private LocalDateTime created;
	private LocalDateTime modified;

	private String name;

	@Transient
	private List<Subscription> subscriptions;

	public Patient() {
	}

	public Patient(String name) {
		this.name = name;
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

	public List<Subscription> getSubscriptions() {
		return this.subscriptions;
	}

	public void setSubscriptions(List<Subscription> subscriptions) {
		this.subscriptions = subscriptions;
	}

	/*
	 * Future: Add family information
	 * 
	 * @JoinColumn(name = "family_id", referencedColumnName = "id") private Family
	 * family;
	 * 
	 * 
	 * public Family getFamily() { return this.family; } public void
	 * setFamily(Family family) { this.family = family; }
	 */

	@Override
	public boolean equals(Object o) {

		if (this == o)
			return true;
		if (o == null || !(o instanceof Patient))
			return false;
		Patient user = (Patient) o;
		return Objects.equals(this.id, user.id) && Objects.equals(this.name, user.name);
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
