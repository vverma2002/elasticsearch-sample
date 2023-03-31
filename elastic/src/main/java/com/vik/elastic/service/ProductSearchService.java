package com.vik.elastic.service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.client.erhlc.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexedObjectInformation;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.core.query.StringQuery;
import org.springframework.stereotype.Service;

import com.vik.elastic.aop.TimerAnnotation;
import com.vik.elastic.modal.Product;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import co.elastic.clients.elasticsearch.core.bulk.IndexOperation;

@Service
public class ProductSearchService {

	@Autowired
	private ElasticsearchOperations elasticsearchOperations;

	@Autowired
	private ElasticsearchClient elasticsearchClient;

	private static final String PRODUCT_INDEX = "productindex";

	public String createProductIndex(Product product) {

		IndexQuery indexQuery = new IndexQueryBuilder().withId(product.getId().toString()).withObject(product).build();

		String documentId = elasticsearchOperations.index(indexQuery, IndexCoordinates.of(PRODUCT_INDEX));

		return documentId;
	}

	@TimerAnnotation
	public List<IndexedObjectInformation> createProductIndexBulk(final List<Product> products) {

		List<IndexQuery> queries = products.stream()
				.map(product -> new IndexQueryBuilder().withId(product.getId().toString()).withObject(product).build())
				.collect(Collectors.toList());

		return elasticsearchOperations.bulkIndex(queries, IndexCoordinates.of(PRODUCT_INDEX));
	}

	@TimerAnnotation
	public void createProductIndexBulkNative(final List<Product> products) throws ElasticsearchException, IOException {
		List<BulkOperation> createRequest = products.stream().map(row ->

		new BulkOperation.Builder().index(new IndexOperation.Builder<Product>().document(row)
//						.id(String.valueOf(row))
				.build()).build()

		).collect(Collectors.toList());

		BulkRequest bulkRequest = new BulkRequest.Builder().operations(createRequest).index(PRODUCT_INDEX).build();

		elasticsearchClient.bulk(bulkRequest);
	}

	// Native
//	public void findProductsByBrand(final String brandName) {
//
//		QueryBuilder queryBuilder = QueryBuilders.matchQuery("manufacturer", brandName);
//
//		Query searchQuery = new NativeSearchQueryBuilder().withQuery(queryBuilder).build();
//
//		SearchHits<Product> productHits = elasticsearchOperations.search(searchQuery, Product.class,
//				IndexCoordinates.of(PRODUCT_INDEX));
//	}

	// Native New Way
//	public void findProductsByBrand(final String brandName) {
//	Query searchQuery = new NativeSearchQueryBuilder()
//			   .withFilter(regexpQuery("title", ".*data.*"))
//			   .build();
//			SearchHits<Product> productHits = 
//			   elasticsearchOperations.search(searchQuery, Product.class, IndexCoordinates.of(PRODUCT_INDEX));
//	}

	// String by Json Search
	public SearchHits<Product> findByProductName(final String productName) {
		Query searchQuery = new StringQuery("{\"match\":{\"name\":{\"query\":\"" + productName + "\"}}}\"");

		SearchHits<Product> products = elasticsearchOperations.search(searchQuery, Product.class,
				IndexCoordinates.of(PRODUCT_INDEX));

		return products;
	}

	// Criteria Search
	public SearchHits<Product> findByProductPrice(final String productPrice) {
		Criteria criteria = new Criteria("price").greaterThan(10.0).lessThan(100.0);

		Query searchQuery = new CriteriaQuery(criteria);

		SearchHits<Product> products = elasticsearchOperations.search(searchQuery, Product.class,
				IndexCoordinates.of(PRODUCT_INDEX));

		return products;
	}

	// Native Way Multi-Field and Fuzzy Search
//	public List<Product> processSearch(final String query) {
//		log.info("Search with query {}", query);
//
//		// 1. Create query on multiple fields enabling fuzzy search
//		QueryBuilder queryBuilder = QueryBuilders.multiMatchQuery(query, "name", "description")
//				.fuzziness(Fuzziness.AUTO);
//
//		Query searchQuery = new NativeSearchQueryBuilder().withFilter(queryBuilder).build();
//
//		// 2. Execute search
//		SearchHits<Product> productHits = elasticsearchOperations.search(searchQuery, Product.class,
//				IndexCoordinates.of(PRODUCT_INDEX));
//
//		// 3. Map searchHits to product list
//		List<Product> productMatches = new ArrayList<Product>();
//		productHits.forEach(searchHit -> {
//			productMatches.add(searchHit.getContent());
//		});
//		return productMatches;
//	}

//	Fetching Suggestions with Wildcard Search
//	public List<String> fetchSuggestions(String query) {
//		QueryBuilder queryBuilder = QueryBuilders.wildcardQuery("name", query + "*");
//
//		Query searchQuery = new NativeSearchQueryBuilder().withFilter(queryBuilder).withPageable(PageRequest.of(0, 5))
//				.build();
//
//		SearchHits<Product> searchSuggestions = elasticsearchOperations.search(searchQuery, Product.class,
//				IndexCoordinates.of(PRODUCT_INDEX));
//
//		List<String> suggestions = new ArrayList<String>();
//
//		searchSuggestions.getSearchHits().forEach(searchHit -> {
//			suggestions.add(searchHit.getContent().getName());
//		});
//		return suggestions;
//	}

}
