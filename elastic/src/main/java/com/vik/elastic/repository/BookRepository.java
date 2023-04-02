package com.vik.elastic.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.vik.elastic.elastic.Book;

public interface BookRepository extends ElasticsearchRepository<Book, String> {
//	List<Product> findByName(String name);
//
//	List<Product> findByNameContaining(String name);
//
//	List<Product> findByManufacturerAndCategory(String manufacturer, String category);
}
