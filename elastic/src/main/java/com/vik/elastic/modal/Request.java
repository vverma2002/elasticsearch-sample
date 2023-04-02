package com.vik.elastic.modal;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.core.query.Query;

import com.vik.elastic.elastic.ElasticGenericService.HasId;

import lombok.AccessLevel;
//import jakarta.persistence.CascadeType;
//import jakarta.persistence.Entity;
//import jakarta.persistence.FetchType;
//import jakarta.persistence.Id;
//import jakarta.persistence.JoinColumn;
//import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//@Entity
@Data
@NoArgsConstructor

@Builder
@Document(indexName = "request-index")
@AllArgsConstructor
public class Request implements HasId {

//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	@Id
	private String id;
	private Long requestId;
	private String designatedActionCode;
	private String ownerProducer;
	private String classification;
	private String disseminationControls;
	private Long identificationId; // TCS ID
	private Long personId; // NUIN
	private Long sequenceNumber; // version No of REcord
	private String tag; // Tag Boolean if tagged
	private String tsanof; // TSA No Fly Boolean
	private String tsasel; // TSA SelectEE Boolean
	private String identityDesignatedActionCode; // Add or modify
	private Instant identityLastUpdated;

	@Id
	public String getId() {
		return String.format("%d_%d", identificationId, personId); 
	}

//	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//	@JoinColumn(name = "parent_id", updatable = false)
	private List<Citizenship> citizenshipList = new ArrayList<>();

//	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//	@JoinColumn(name = "parent_id", updatable = false)
	private List<Identification> identificationList = new ArrayList<>();

//	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//	@JoinColumn(name = "parent_id", updatable = false)
	private List<Name> nameList = new ArrayList<>();

//	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//	@JoinColumn(name = "parent_id", updatable = false)
	private List<PhysicalDescription> physicalDescriptionList = new ArrayList<>();

	@Override
	public Query getCriteriaQuery() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isSimilarDocument(Object existingdoc) {
		// TODO Auto-generated method stub
		return false;
	}
}