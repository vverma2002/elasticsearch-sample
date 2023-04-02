package com.vik.elastic.modal;

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
public class Name {
//	private Long parentId;
//	private String id;
//	@EmbeddedId
	private CompositeID id;
	private String fullName;
	private String givenName;
	private String surName;
}
