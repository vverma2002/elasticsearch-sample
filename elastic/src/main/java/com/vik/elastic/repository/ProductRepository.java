package com.vik.elastic.repository;

import java.util.List;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.vik.elastic.modal.Product;

public interface ProductRepository extends ElasticsearchRepository<Product, String> {
	List<Product> findByName(String name);

	List<Product> findByNameContaining(String name);

	List<Product> findByManufacturerAndCategory(String manufacturer, String category);
}
