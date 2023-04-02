package com.vik.elastic.elastic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.vik.elastic.elastic.ElasticGenericService.HasId;

@Service
public class ElasticGenericService<T extends HasId> {

	@Autowired
	private ElasticsearchOperations elasticsearchOperations;

//	@Service
//	public class ElasticGenericService {
//	
//		@Autowired
//		private ElasticsearchOperations elasticsearchOperations;
//	
//		public String index(Book t) {
//	
//			IndexType index = IndexType.searchByType(t.getClass());
//	
//			IndexQuery indexQuery = new IndexQueryBuilder().withId(t.getId().toString()).withObject(t).build();
//	
//			String documentId = elasticsearchOperations.index(indexQuery, IndexCoordinates.of(index.getIndex()));
//	
//			return documentId;
//		}
//	}	

	public String index(T document) {
		Assert.notNull(document.getId(), "Object ID cannot be null");

		// Class<?> objectType = document.getClass();

		Class<T> objectType = (Class<T>) document.getClass();

		IndexType indexType = IndexType.searchByType(objectType);

		// Query searchQuery = new StringQuery("{\"match\":{\"name\":{\"query\":\"" +
		// productName + "\"}}}\"");

		// Criteria criteria = new Criteria("price").greaterThan(10.0).lessThan(100.0);
		// Query searchQuery = new CriteriaQuery(criteria);

//    	SearchHits<Product> products = elasticsearchOperations.search(searchQuery, Product.class,
//				IndexCoordinates.of(indexType.getIndex()));

//		Query searchQuery = new StringQuery(String.format("{\"term\":{\"_id\":\"%s\"}}", document.getId()));
//
//		boolean documentExists = elasticsearchOperations
//				.search(searchQuery, objectType, IndexCoordinates.of(indexType.getIndex())).getTotalHits() > 0;

//			T existingDocument = elasticsearchOperations.get(document.getId(), documentType,IndexCoordinates.of(indexType.getIndex()));

		boolean documentExists = elasticsearchOperations.exists(document.getId(), objectType);

		if (documentExists) {
			System.out.println("object already exists!");
			return document.getId();
		} else {
			System.out.println("creating new object..");
		}

		IndexQuery indexQuery = new IndexQueryBuilder().withId(document.getId().toString()).withObject(document)
				.build();

		String documentId = elasticsearchOperations.index(indexQuery, IndexCoordinates.of(indexType.getIndex()));

		return documentId;
	}

	public interface HasId {
		public String getId();

		public Query getCriteriaQuery();

		public boolean isSimilarDocument(Object existingdoc);

	}

//	public void index(List<T> documents) {
//		int batchSize = 2;
//		for (int i = 0; i < documents.size(); i += batchSize) {
//			// insert records in batches
//			List<T> batch = documents.subList(i, Math.min(i + batchSize, documents.size()));
//			System.out.println(batch.size());
//
//			List<IndexQuery> indexQueries = new ArrayList<>();
//			for (T book : documents) {
//				IndexQuery indexQuery = new IndexQuery();
////				        indexQuery.setIndexName("books");
////				        indexQuery.setType("_doc");
//				indexQuery.setObject(book);
//				indexQueries.add(indexQuery);
//			}
//			elasticsearchOperations.bulkIndex(indexQueries, Book.class);
//			// elasticsearchOperations.refresh("books");
//		}
//
//	}

	public void index(List<T> documents) {

		IndexType indexType = null;
		if (documents != null && documents.size() > 0) {

			indexType = IndexType.searchByType(documents.get(0).getClass());
		}

		int batchSize = 2;
		for (int i = 0; i < documents.size(); i += batchSize) {
			// insert records in batches
			List<T> batch = documents.subList(i, Math.min(i + batchSize, documents.size()));
			System.out.println(batch.size());

			List<IndexQuery> indexQueries = new ArrayList<>();
			List<UpdateQuery> updateQueries = new ArrayList<>();

			for (T document : batch) {
				Assert.notNull(document.getId(), "Object ID cannot be null");

				// Class<?> documentType = document.getClass();
				Class<T> documentType = (Class<T>) document.getClass();

//	                Query query = QueryBuilders.idsQueryAsQuery(List.of(document.getId()));
//	                SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(query).build();
//	                List<T> existingDocuments = elasticsearchOperations.search(searchQuery, documentType, IndexCoordinates.of(indexType.getIndex())).get().map(item -> (T) item.getContent()).collect(Collectors.toList());

				// Criteria criteria = new Criteria("price").greaterThan(10.0).lessThan(100.0);
				// Query searchQuery = new CriteriaQuery(criteria);

//	            	SearchHits<Product> products = elasticsearchOperations.search(searchQuery, Product.class,
//	        				IndexCoordinates.of(indexType.getIndex()));

//	        		Query searchQuery = new StringQuery(String.format("{\"term\":{\"_id\":\"%s\"}}", document.getId()));
//
//	        		boolean existingDocuments = elasticsearchOperations
//	        				.search(searchQuery, documentType, IndexCoordinates.of(indexType.getIndex())).getTotalHits() > 0;

//	                

				boolean documentExists = elasticsearchOperations.exists(document.getId(), documentType);
//	                

				if (documentExists) {
				
					
					System.out.println(document.getId() + " already exists! Skipping");
					continue;

				} else {
					System.out.println("id doens exis , lets try to search based on other criteria..");

//	        			System.out.println(existingDocument);
//	        			 List<T> existingDocuments = elasticsearchOperations.search(query, documentType, IndexCoordinates.of(indexType.getIndex())).get().map(item -> (T) item.getContent()).collect(Collectors.toList());

					// System.out.println(existingDocuments);

//	        			Criteria criteria = new Criteria("name").is("FF").and("category").is("gg");
//	        			Query query = new CriteriaQuery(criteria);

					Query query = document.getCriteriaQuery();
					SearchHit<T> existingDocument = elasticsearchOperations.searchOne(query, documentType);

					if (existingDocument != null) {
						System.out.println("found a match, lets check the seq");

						T existingdoc = existingDocument.getContent();

						if (document.isSimilarDocument(existingdoc)) {
							System.out.println("same seq/price only! Skipping");
							continue;
						} else {
							System.out.println("price diff, lets update..");
				            Map<String, Object> updatedFields = Map.of("price", (((Book) document).getPrice() ));
				            UpdateQuery updateQuery = UpdateQuery.builder(existingdoc.getId())
				                    .withDocument(Document.from(updatedFields)).build();
				            updateQueries.add(updateQuery);
							continue;
						}

					}

					System.out.println("creating new object..");
					IndexQuery indexQuery = new IndexQuery();
					indexQuery.setId(document.getId());
					indexQuery.setObject(document);
					indexQueries.add(indexQuery);
				}

			}

//	                if (existingDocuments.isEmpty()) {
//	                    IndexQuery indexQuery = new IndexQuery();
//	                    indexQuery.setId(document.getId());
//	                    indexQuery.setObject(document);
//	                    indexQueries.add(indexQuery);
//	                } else {
//	                    T existingDocument = existingDocuments.get(0);
//	                    if (document instanceof Book && existingDocument instanceof Book) {
//	                        Book newBook = (Book) document;
//	                        Book existingBook = (Book) existingDocument;
//	                        if (newBook.getPrice() > existingBook.getPrice()) {
//	                            Map<String, Object> updatedFields = Map.of("price", newBook.getPrice());
//	                            UpdateQuery updateQuery = UpdateQuery.builder(document.getId())
//	                                    .withDocument(Document.from(updatedFields))
//	                                    .build();
//	                            updateQueries.add(updateQuery);
//	                        }
//	                    }
//	                }
			if (indexQueries.size() > 0)
				elasticsearchOperations.bulkIndex(indexQueries, IndexCoordinates.of(indexType.getIndex()));
			if (updateQueries.size() > 0)
				elasticsearchOperations.bulkUpdate(updateQueries, IndexCoordinates.of(indexType.getIndex()));
		}
	}

	 private Long getSequenceNumber(String id) {
	        String[] parts = id.split("_");
	        return Long.parseLong(parts[2]);
	    }
	
}
