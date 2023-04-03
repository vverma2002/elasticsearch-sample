package com.vik.elastic.modal;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

import lombok.Data;
import lombok.NoArgsConstructor;


//FieldTypes
//https://www.elastic.co/guide/en/elasticsearch/reference/current/mapping-types.html

	
@Document(indexName = "productindex")
//@Setting(replicas = 3, shards = 1)
//@Setting(replicas = 0, shards = 3)
@Setting(replicas = 0, shards = 1)
@Data
@NoArgsConstructor
public class Product {
	@Id
	private String id;

	@Field(type = FieldType.Text, name = "name")
	private String name;

	@Field(type = FieldType.Double, name = "price")
	private Double price;

	@Field(type = FieldType.Integer, name = "quantity")
	private Integer quantity;

	@Field(type = FieldType.Keyword, name = "category")
	private String category;

	@Field(type = FieldType.Object, name = "desc", enabled = false)
	private String description;

	@Field(type = FieldType.Keyword, name = "manufacturer")
	private String manufacturer;
	
//    @Field(type = FieldType.Nested, includeInParent = true)
//    private List<Author> authors;

}
