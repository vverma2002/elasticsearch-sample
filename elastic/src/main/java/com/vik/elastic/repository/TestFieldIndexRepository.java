package com.vik.elastic.repository;

import org.springframework.data.elasticsearch.annotations.Highlight;
import org.springframework.data.elasticsearch.annotations.HighlightField;
import org.springframework.data.elasticsearch.annotations.SourceFilters;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.vik.elastic.modal.TestFieldIndex;

public interface TestFieldIndexRepository extends ElasticsearchRepository<TestFieldIndex, String> {
//	List<Product> findByName(String name);
//
//	List<Product> findByNameContaining(String name);
//
//	List<Product> findByManufacturerAndCategory(String manufacturer, String category);

	@Highlight(fields = { @HighlightField(name = "name") })
	@SourceFilters(includes = "id")
	SearchHits<TestFieldIndex> findByName(String text);
}
