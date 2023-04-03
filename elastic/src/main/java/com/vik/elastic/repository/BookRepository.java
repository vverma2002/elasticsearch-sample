package com.vik.elastic.repository;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Highlight;
import org.springframework.data.elasticsearch.annotations.HighlightField;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.annotations.SourceFilters;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.vik.elastic.elastic.Book;

public interface BookRepository extends ElasticsearchRepository<Book, String> {

	// Return can be one of:
//	List<T>
//	Stream<T>
//	SearchHits<T>
//	List<SearchHit<T>>
//	Stream<SearchHit<T>>
//	SearchPage<T>

//	List<Product> findByName(String name);
//
//	List<Product> findByNameContaining(String name);
//
//	List<Product> findByManufacturerAndCategory(String manufacturer, String category);

	@Highlight(fields = { @HighlightField(name = "name"), @HighlightField(name = "summary") })
	SearchHits<Book> findByNameOrSummary(String text, String summary);

	@SourceFilters(includes = "name")
	SearchHits<Book> findByName(String text);

	@Query("{\"match\": {\"name\": {\"query\": \"?0\"}}}")
	Page<Book> findByName(String name, Pageable pageable);

	List<Book> findByNameAndPrice(String name, Integer price);
	// refer :
	// https://docs.spring.io/spring-data/elasticsearch/docs/current/reference/html/#elasticsearch.query-methods.criterions
	// https://docs.spring.io/spring-data/elasticsearch/docs/current/reference/html/#repository-query-keywords
	//	{
//	    "query": {
//	        "bool" : {
//	            "must" : [
//	                { "query_string" : { "query" : "?", "fields" : [ "name" ] } },
//	                { "query_string" : { "query" : "?", "fields" : [ "price" ] } }
//	            ]
//	        }
//	    }
//	}

	@Query("{\"ids\": {\"values\": ?0 }}")
	List<Book> getByIds(Collection<String> ids);
//	{
//	  "query": {
//	    "ids": {
//	      "values": ["id1", "id2", "id3"]
//	    }
//	  }
//	}

	// for scroll api
	Stream<Book> findBy();
}
