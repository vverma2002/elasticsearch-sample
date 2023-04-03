package com.vik.elastic;

import java.io.IOException;
import java.util.Date;
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

	@PostConstruct
	public void buildIndex() throws IOException, InterruptedException {
		System.out.println("hii");
		List<Product> prepareDataset = prepareDataset();
		
		for (int i = 0; i < 50; i++) {
			prepareDataset.addAll(prepareDataset());
		}
		
		System.out.println("saving: " + prepareDataset.size());

		productService.createProductIndexBulk(prepareDataset);
		
//		Thread.currentThread().sleep(5000);
		
		productSearchService.createProductIndexBulk(prepareDataset);

//		Thread.currentThread().sleep(5000);
		
		productSearchService.createProductIndexBulkNative(prepareDataset);
		
//		Thread.currentThread().sleep(5000);
	}

	private List<Product> prepareDataset() throws IOException {
		return CSVUtil.readCsvFile("fashion-products.csv").stream().map(record -> {
			Product p = new Product();
			p.setId(new Date().toString());
			p.setDescription(record);
			return p;
		}).collect(Collectors.toList());
	}

}
