package com.vik.elastic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;

import com.vik.elastic.aop.TimerAnnotation;
import com.vik.elastic.elastic.Book;
import com.vik.elastic.elastic.ElasticGenericService;
import com.vik.elastic.modal.Request;
import com.vik.elastic.repository.RequestRepository;
import com.vik.elastic.util.RandomRequestGenerator;

@SpringBootTest
class ElasticApplicationTests {

	@Autowired
	RequestRepository repo;

	@Autowired
	private ElasticsearchOperations elasticsearchOperations;

	@Autowired
	ElasticGenericService elasticGenericService;

	// @Test
	void singleIndexTest() {
		Book book = IntStream.of(1).mapToObj(i -> createBook(i)).findFirst().orElse(null);

		elasticGenericService.index(book);
	}

	// @Test
	void bulkIndexTest() {

		List<Book> books = IntStream.range(13, 14).mapToObj(i -> createBook(i)).collect(Collectors.toList());
		elasticGenericService.index(books);
	}

	@Test
	void bulkIndexTestRequest() {
		long start = 0;
		long end = 1;
		List<Request> requests = RandomRequestGenerator.generateRequestList(start, end);
		System.out.println("Size : " + requests.size());
//	
//		Request requestRaw = Request.builder()
//				// .id(UUID.randomUUID().toString())
//				.requestId(123L).designatedActionCode("dummy").ownerProducer("dummy").classification("dummy")
//				.disseminationControls("dummy").identificationId(456L).personId(789L).sequenceNumber(1L).tag("dummy")
//				.tsanof("dummy").tsasel("dummy").identityDesignatedActionCode("add")
//				// .identityLastUpdated(Instant.now())
//				.build();
//
//		System.out.println(requestRaw);
////
//		requests = Arrays.asList(requestRaw);
	
		int batchSize = 1000;
		for (int i = 0; i < requests.size(); i += batchSize) {
			// insert records in batches
			List<Request> batch = requests.subList(i, Math.min(i + batchSize, requests.size()));
			System.out.println(batch.size());

			List<IndexQuery> indexQueries = new ArrayList<>();
			List<UpdateQuery> updateQueries = new ArrayList<>();

			requests.forEach(request -> {

				Request match = elasticsearchOperations.get(request.getId(), request.getClass());

				if (match == null) {
		//			System.out.println("No match Found, will Insert");
					IndexQuery indexQuery = new IndexQuery();
					indexQuery.setId(request.getId());
					indexQuery.setObject(request);
					indexQueries.add(indexQuery);
				} else {
					System.out.println(match.getId() + ", match found!, Lets check the seq");

					System.out.println("Comparing current and matching seq->" + request.getSequenceNumber() + ":"
							+ match.getSequenceNumber());
					if (request.getSequenceNumber() > match.getSequenceNumber()) {
						System.out.println("Its a NEW seq! Lets compare fields and update!");

						Map<String, Object> updatedFields = Map.of("designatedActionCode",
								"designatedActionCode" + new Date(), "sequenceNumber", request.getSequenceNumber());

						UpdateQuery.Builder updateQuery = UpdateQuery.builder(request.getId());
						// updateQuery.withUpsert(Document.from(updatedFields));
						updateQuery.withDocument(Document.from(updatedFields));
						// updateQuery.withDocAsUpsert(true);
						updateQueries.add(updateQuery.build());
					} else {
						System.out.println("Its a OLD seq! Skipping!");
					}
				}

			});

			if (indexQueries.size() > 0)
				elasticsearchOperations.bulkIndex(indexQueries, Request.class);
			if (updateQueries.size() > 0)
				elasticsearchOperations.bulkUpdate(updateQueries, Request.class);

//			elasticGenericService.index(requests);
		}
	}

	private Book createBook(int id) {

		// Create a new Book object
		Book book = Book.builder().id("Book-" + id).name("The Great Gatsby").price(65.99).category("Fiction")
				.description("A classic novel about the Jazz Age").publisher("Scribner").build();

		// Add authors to the Book object
		Book.Author author1 = new Book.Author();
		author1.setId("456");
		author1.setAuthName("F. Scott Fitzgerald");

		Book.Author author2 = new Book.Author();
		author2.setId("789");
		author2.setAuthName("Ernest Hemingway");

		book.setAuthors(Arrays.asList(author1, author2));

		return book;

	}

	@Test
	void contextLoads() {
	}

//	@Test
	void testBatchGenerateRequests() {

		List<String> children = IntStream.range(0, 10).mapToObj(i -> "Object " + i).collect(Collectors.toList());

		int batchSize = 2;
		for (int i = 0; i < children.size(); i += batchSize) {
			// insert records in batches
			List<String> batch = children.subList(i, Math.min(i + batchSize, children.size()));
			System.out.println(batch);
		}
	}

	private static final long START_ID = 1;
	private static final long END_ID = 2;

	// @Test
	void testGenerateRequests() {
		List<Request> requests = RandomRequestGenerator.generateRequestList(START_ID, END_ID);
		System.out.println("Size : " + requests.size());
		Assertions.assertEquals(requests.size() - 1, END_ID - START_ID);
		// repo.saveAll(requests);
		// bulkIndex(requests);
//		
		int batchSize = 25000;
		for (int i = 0; i < requests.size(); i += batchSize) {
			// insert records in batches
			List<Request> batch = requests.subList(i, Math.min(i + batchSize, requests.size()));
			// System.out.println(batch);
			bulkIndex(batch);
		}

	}

	@TimerAnnotation
	public void bulkIndex(List<Request> books) {
		List<IndexQuery> indexQueries = new ArrayList<>();
		for (Request book : books) {
			IndexQuery indexQuery = new IndexQuery();
//	        indexQuery.setIndexName("books");
//	        indexQuery.setType("_doc");
			indexQuery.setObject(book);
			indexQueries.add(indexQuery);
		}
		elasticsearchOperations.bulkIndex(indexQueries, Request.class);
		// elasticsearchOperations.refresh("books");
	}
}
