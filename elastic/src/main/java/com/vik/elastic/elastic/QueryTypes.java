package com.vik.elastic.elastic;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.RuntimeField;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.SearchHitsIterator;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.core.query.StringQuery;

import com.vik.elastic.modal.Product;

import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;

public class QueryTypes {
//	Query is an interface and Spring Data Elasticsearch provides three implementations: CriteriaQuery, StringQuery and NativeQuery

	@Autowired
	private static ElasticsearchOperations operations;

	public static void Criteria() {
		{
			Criteria criteria = new Criteria("price").is(42.0);
			Query query = new CriteriaQuery(criteria);
		}
		{
			Criteria criteria = new Criteria("price").greaterThan(42.0).lessThan(34.0);
			Query query = new CriteriaQuery(criteria);
		}
		{
			Criteria criteria = new Criteria("lastName").is("Miller")
					.subCriteria(new Criteria().or("firstName").is("John").or("firstName").is("Jack"));
			Query query = new CriteriaQuery(criteria);
			query.addSourceFilter(
					new FetchSourceFilter(new String[] { "field1", "field2" }, new String[] { "field3" }));
		}
		{
			RuntimeField runtimeField = new RuntimeField("priceWithTax", "double", "emit(doc['price'].value * 1.19)");
			Query query = new CriteriaQuery(new Criteria("priceWithTax").greaterThanEqual(16.5));
			query.addRuntimeField(runtimeField);

			SearchHits<Product> searchHits = operations.search(query, Product.class);
		}

	}

	public static void StringQuery() {
		{
			Query query = new StringQuery("{ \"match\": { \"firstname\": { \"query\": \"Jack\" } } } ");
			SearchHits<Product> searchHits = operations.search(query, Product.class);
		}
	}

	public static void NativeQuery() {
		{
			Pageable pageable = Pageable.ofSize(10);
			String firstName = "Vik";

		//// @formatter:off
		Query query = NativeQuery.builder()
				.withAggregation("lastNames", Aggregation.of(a -> a
					.terms(ta -> ta.field("last-name").size(10))))
				.withQuery(q -> q
					.match(m -> m
						.field("firstName")
						.query(firstName)
					)
				)
//				.withFields("message")
				.withPageable(pageable)
				.build();
		

		SearchHits<Product> searchHits = operations.search(query, Product.class);
		}
		
		{
			String firstName = "Vik";

			FetchSourceFilter fetchSourceFilter = new FetchSourceFilter( new String[] { "field1", "field2" },new String[] { "field3" });

			Query query = NativeQuery.builder().withQuery(q -> q
					.match(m -> m
							.field("firstName")
							.query(firstName)
						))
					.withPageable(PageRequest.of(0, 10)).withSourceFilter(fetchSourceFilter).build();
			SearchHits<Product> searchHits = operations.search(query, Product.class);
		}
		
		{
			
			IndexCoordinates index = IndexCoordinates.of("sample-index");

			String documentId ="1";
			
			Query query = NativeQuery.builder()
				.withQuery(q -> q
					.matchAll(ma -> ma))
//				.withFields("message")				
				.withFilter( q -> q
					.bool(b -> b
						.must(m -> m
							.term(t -> t
								.field("id")
								.value(documentId))
						)))
				.build();

			SearchHits<Product> sampleEntities = operations.search(query, Product.class, index);

		}
		
		{
			IndexCoordinates index = IndexCoordinates.of("sample-index");

			Query searchQuery = NativeQuery.builder()
			    .withQuery(q -> q
			        .matchAll(ma -> ma))
			    .withFields("message")
			    .withPageable(PageRequest.of(0, 10))
			    .build();

			SearchHitsIterator<Product> stream = operations.searchForStream(searchQuery, Product.class,
			index);

			List<Product> sampleEntities = new ArrayList<>();
			while (stream.hasNext()) {
			  sampleEntities.add(stream.next().getContent());
			}

			stream.close();
		}
		// @formatter:on
	}
}
