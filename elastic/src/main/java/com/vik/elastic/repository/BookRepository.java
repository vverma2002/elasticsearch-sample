package com.vik.elastic.repository;

import org.springframework.data.elasticsearch.annotations.Highlight;
import org.springframework.data.elasticsearch.annotations.HighlightField;
import org.springframework.data.elasticsearch.annotations.SourceFilters;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.vik.elastic.elastic.Book;

public interface BookRepository extends ElasticsearchRepository<Book, String> {
//	List<Product> findByName(String name);
//
//	List<Product> findByNameContaining(String name);
//
//	List<Product> findByManufacturerAndCategory(String manufacturer, String category);

	@Highlight(fields = { @HighlightField(name = "name"), @HighlightField(name = "summary") })
	SearchHits<Book> findByNameOrSummary(String text, String summary);

	@SourceFilters(includes = "name")
	SearchHits<Book> findByName(String text);
}
