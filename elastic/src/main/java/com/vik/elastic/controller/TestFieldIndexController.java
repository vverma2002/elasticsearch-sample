package com.vik.elastic.controller;

import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vik.elastic.modal.TestFieldIndex;

@RestController
@RequestMapping("/api/test-field")
public class TestFieldIndexController {

	private ElasticsearchOperations elasticsearchOperations;

	public TestFieldIndexController(ElasticsearchOperations elasticsearchOperations) {
		this.elasticsearchOperations = elasticsearchOperations;
	}

	@PostMapping
	public String save(@RequestBody TestFieldIndex testFieldIndex) {
		TestFieldIndex savedEntity = elasticsearchOperations.save(testFieldIndex);
		return savedEntity.getId();
	}

	@GetMapping("/{id}")
	public TestFieldIndex findById(@PathVariable("id") Long id) {
		TestFieldIndex testFieldIndex = elasticsearchOperations.get(id.toString(), TestFieldIndex.class);
		return testFieldIndex;
	}
}