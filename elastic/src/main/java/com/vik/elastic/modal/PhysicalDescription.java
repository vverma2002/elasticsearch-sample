package com.vik.elastic.modal;

import java.time.LocalDate;

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
public class PhysicalDescription {
//	private Long parentId;
//	private String id;
//	@EmbeddedId
	private CompositeID id;
	private LocalDate dateOfBirth;
	private String gender; // male female or null
}
