package com.vik.elastic.modal;

import java.time.LocalDate;

//import com.taqniat.coral.util.CountryUtil;
//
//import jakarta.persistence.EmbeddedId;
//import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Identification {
//	private Long parentId;
//	private String id;
//	@EmbeddedId
	private CompositeID id;
	private String identificationType; // Allways // PassportNumber
	private String identifier; // passport number
	private String countryCode; // 2 letter code
//	private String countryName;
	private String fullName;
	private String givenName;
	private String surName;
	private LocalDate dateOfBirth;

//	public String getCountryName() {
////		return CountryUtil.getCountryNameByCode(countryCode);
//	}
}
