package com.vik.elastic.elastic;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.Query;

import com.vik.elastic.elastic.ElasticGenericService.HasId;

import lombok.Builder;
import lombok.Data;

@Document(indexName = "book-indice")
@Data
@Builder
public class Book implements HasId {
	@Id
	private String id;

	@Field(type = FieldType.Text, name = "name")
	private String name;

	@Field(type = FieldType.Double, name = "price")
	private Double price;

	@Field(type = FieldType.Keyword, name = "category")
	private String category;

	@Field(type = FieldType.Text, name = "desc")
	private String description;

	@Field(type = FieldType.Text, name = "summary")
	private String summary;

	@Field(type = FieldType.Keyword, name = "manufacturer")
	private String publisher;

	@Field(type = FieldType.Nested, includeInParent = true)
	private List<Author> authors;

	@Override
	public Query getCriteriaQuery() {
		Criteria nameCriteria = Criteria.where("name").is(name);
		Criteria categoryCriteria = Criteria.where("category").is(category);
		Criteria finalCriteria = nameCriteria.and(categoryCriteria);

		Query criteriaQuery = new CriteriaQuery(finalCriteria);
		return criteriaQuery;
	}

	@Data
	public static class Author {
		@Id
		private String id;

		@Field(type = FieldType.Text, name = "name")
		private String authName;

	}

	@Override
	public boolean isSimilarDocument(Object existingBook) {
		if (existingBook instanceof Book) {
			Book oldBook = (Book) existingBook;
			System.out.println("oldBook:" + oldBook.getPrice() + ": now price" + price);
			if (oldBook.getPrice() >= price) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

}
