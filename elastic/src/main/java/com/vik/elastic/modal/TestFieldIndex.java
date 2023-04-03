package com.vik.elastic.modal;

import java.util.Date;

import org.springframework.data.annotation.AccessType;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.annotation.Transient;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.domain.Range;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;
import org.springframework.data.elasticsearch.annotations.WriteOnlyProperty;
import org.springframework.data.elasticsearch.annotations.WriteTypeHint;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//FieldTypes
//https://www.elastic.co/guide/en/elasticsearch/reference/current/mapping-types.html


@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "test-field-index",writeTypeHint = WriteTypeHint.DEFAULT)
@TypeAlias("test_table")   // hint "_class" : "test_table"   
@Setting(replicas = 0, shards = 1)
public class TestFieldIndex {

	@Id
	private String id;

	private String name;

	@Transient
	@Field(type = FieldType.Text)
	private String transientText;

	@ReadOnlyProperty
	@Field(type = FieldType.Text)
	private String readOnlyText;

	@WriteOnlyProperty
	@Field(type = FieldType.Text)
	private String writeOnlyText;

	@Field(type = FieldType.Date)
	private String createDate;

	@Field(type = FieldType.Integer_Range)
	private Range<Integer> validAge;

	@Field(type = FieldType.Date_Range)
	private Range<Date> validDate;

	//Non-field-backed properties
	@Field(type = FieldType.Keyword)
	@WriteOnlyProperty
	@AccessType(AccessType.Type.PROPERTY)
	public String getProperty() {
		return "some value that is calculated here";
	}

}
