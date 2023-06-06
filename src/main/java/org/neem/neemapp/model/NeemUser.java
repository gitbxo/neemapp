package org.neem.neemapp.model;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "neem_user")
public class NeemUser {

	private @Id @GeneratedValue Long id;
	private String name;
	private String role;

	public NeemUser() {
	}

	public NeemUser(String name, String role) {
		this.name = name;
		this.role = role;
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

	public String getRole() {
		return this.role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || !(o instanceof NeemUser))
			return false;

		return this.toString().equals(((NeemUser) o).toString());
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.toString());
	}

	@Override
	public String toString() {
		return "NeemUser{" + "id=" + String.valueOf(this.id) + ", name='" + String.valueOf(this.name) + "', role='"
				+ String.valueOf(this.role) + '\'' + '}';
	}

}
