package com.vik.elastic.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.vik.elastic.aop.TimerAnnotation;
import com.vik.elastic.modal.Product;
import com.vik.elastic.repository.ProductRepository;

@Service
public class ProductService {

	@Autowired
	ProductRepository productRepository;

	public Page<Product> getAll(Pageable pageable) {
		return productRepository.findAll(pageable);
	}

	@TimerAnnotation
	public void createProductIndexBulk(final List<Product> products) {
		productRepository.saveAll(products);
	}

	public Product createProductIndex(final Product product) {
		productRepository.save(product);
		return product;
	}
}
