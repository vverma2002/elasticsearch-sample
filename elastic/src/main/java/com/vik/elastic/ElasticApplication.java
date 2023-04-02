package com.vik.elastic;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.vik.elastic.modal.Product;
import com.vik.elastic.service.ProductSearchService;
import com.vik.elastic.service.ProductService;
import com.vik.elastic.util.CSVUtil;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class ElasticApplication {

	public static void main(String[] args) {
		SpringApplication.run(ElasticApplication.class, args);
	}

	@Autowired
	ProductService productService;

	@Autowired
	ProductSearchService productSearchService;

//	@PostConstruct
	public void buildIndex() throws IOException {
		System.out.println("hii");
		List<Product> prepareDataset = prepareDataset();
		prepareDataset.addAll(prepareDataset());
		prepareDataset.addAll(prepareDataset());
		prepareDataset.addAll(prepareDataset());
		prepareDataset.addAll(prepareDataset());
		prepareDataset.addAll(prepareDataset());
		prepareDataset.addAll(prepareDataset());
		prepareDataset.addAll(prepareDataset());
		prepareDataset.addAll(prepareDataset());
		prepareDataset.addAll(prepareDataset());
		prepareDataset.addAll(prepareDataset());
		prepareDataset.addAll(prepareDataset());
		prepareDataset.addAll(prepareDataset());
		prepareDataset.addAll(prepareDataset());
		prepareDataset.addAll(prepareDataset());
		prepareDataset.addAll(prepareDataset());
		prepareDataset.addAll(prepareDataset());
		prepareDataset.addAll(prepareDataset());
		prepareDataset.addAll(prepareDataset());
		prepareDataset.addAll(prepareDataset());
		prepareDataset.addAll(prepareDataset());
		prepareDataset.addAll(prepareDataset());
		prepareDataset.addAll(prepareDataset());
		prepareDataset.addAll(prepareDataset());
		System.out.println("saving: " + prepareDataset.size());

		productService.createProductIndexBulk(prepareDataset);

		productSearchService.createProductIndexBulk(prepareDataset);

		productSearchService.createProductIndexBulkNative(prepareDataset);
	}

	private List<Product> prepareDataset() throws IOException {
		return CSVUtil.readCsvFile("fashion-products.csv").stream().map(record -> {
			Product p = new Product();
			p.setDescription(record);
			return p;
		}).collect(Collectors.toList());
	}

}
