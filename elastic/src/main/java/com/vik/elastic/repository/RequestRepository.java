package com.vik.elastic.repository;

import java.util.List;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.vik.elastic.modal.Product;
import com.vik.elastic.modal.Request;

public interface RequestRepository extends ElasticsearchRepository<Request, String> {
//	List<Product> findByName(String name);
//
//	List<Product> findByNameContaining(String name);
//
//	List<Product> findByManufacturerAndCategory(String manufacturer, String category);
}
