package org.neem.neemapp.model;

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
// import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.persistence.AttributeConverter;
import javax.persistence.Column;
// import javax.persistence.Column;
// import javax.persistence.Convert;
// import javax.persistence.Column;
// import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import com.vladmihalcea.hibernate.type.json.JsonStringType;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// import org.hibernate.annotations.Type;
// import org.hibernate.annotations.TypeDef;

@Entity
@IdClass(SubscriptionId.class)
@Table(name = "subscription")
// @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
@TypeDefs({ @TypeDef(name = "json", typeClass = JsonStringType.class),
		@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class) })
public class Subscription {

	public static class MapConverter implements AttributeConverter<Map<String, Object>, String> {

		private static final ObjectMapper mapper = new ObjectMapper();
		{
			mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
			mapper.registerModule(new JavaTimeModule());
			mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
			mapper.enable(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS);
		}

		@Override
		public String convertToDatabaseColumn(Map<String, Object> map) {

			String jsonString = null;
			try {
				if (map != null && map.size() > 0)
					jsonString = mapper.writeValueAsString(map);
			} catch (final JsonProcessingException e) {
			}

			return jsonString;
		}

		@SuppressWarnings("unchecked")
		@Override
		public Map<String, Object> convertToEntityAttribute(String jsonString) {

			Map<String, Object> data = null;
			try {
				if (jsonString != null && jsonString.length() > 0)
					data = mapper.readValue(jsonString, Map.class);
			} catch (final IOException e) {
			}

			return data;
		}
	}

	@SuppressWarnings("serial")
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class OverrideInfo implements Serializable {
		public OverrideInfo(String value) {
			this.overrideItem = "";
			this.overrideValue = 0;
			if (value == null || !value.contains(":")) {
				return;
			}

			String[] split = value.split(":");
			if (split.length == 2) {
				this.overrideItem = split[0];
				this.overrideValue = Integer.valueOf(split[1]);
			}
		}

		@SuppressWarnings("unused")
		private String overrideItem;

		@SuppressWarnings("unused")
		private int overrideValue;
	}

	private @Id Long patientId;

	private @Id Long planId;

	private LocalDateTime created;

	private LocalDateTime modified;

	private int usedDeductible;

	private String usedOverrides;

	// @Type(type = "jsonb")
	// @Column(columnDefinition = "jsonb")
	// private List<OverrideInfo> jsonOverrides;

	// @Convert(converter = PgJsonbToMapConverter.class)

	@Type(type = "jsonb")
	@Column(columnDefinition = "json")
	private HashMap<String, Integer> jsonOverrides;

	// @Convert(converter = MapConverter.class)
	// private HashMap<String, Object> jsonOverrides;

	public Subscription() {
	}

	public Subscription(Patient patient, InsurancePlan plan) {
		this.patientId = patient.getId();
		this.planId = plan.getId();
		this.usedDeductible = 0;
		this.usedOverrides = "";
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

	public long getPatientId() {
		return this.patientId;
	}

	public void setPatientId(long patientId) {
		this.patientId = patientId;
	}

	public long getPlanId() {
		return this.planId;
	}

	public void setPlanId(long planId) {
		this.planId = planId;
	}

	public int getUsedDeductible() {
		return this.usedDeductible;
	}

	public void setUsedDeductible(int usedDeductible) {
		this.usedDeductible = usedDeductible;
	}

	public String getUsedOverrides() {
		return this.usedOverrides;
	}

	public void setUsedOverrides(String usedOverrides) {
		this.usedOverrides = usedOverrides;
	}

	public HashMap<String, Integer> getJsonOverrides() {
		return this.jsonOverrides;
	}

	public void setJsonOverrides(HashMap<String, Integer> jsonOverrides) {
		this.jsonOverrides = jsonOverrides;
	}

	public Map<InsurancePlan.MedicalType, Integer> getUsedOverridesMap() {
		Map<InsurancePlan.MedicalType, Integer> usedOverrides = InsurancePlan.getOverridesMap(this.usedOverrides);

		return usedOverrides;
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
		return "Subscription{" + " patientId='" + String.valueOf(this.patientId) + "', planId='"
				+ String.valueOf(this.planId) + "', jsonOverrides='" + String.valueOf(this.jsonOverrides)
				+ "', usedOverrides='" + String.valueOf(this.usedOverrides) + "', usedDeductible="
				+ String.valueOf(this.usedDeductible) + '}';
	}

}
