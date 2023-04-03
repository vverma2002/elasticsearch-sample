package com.vik.elastic.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.config.ElasticsearchConfigurationSupport;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(basePackages = "org.springframework.data.elasticsearch.repositories")
public class ElasticsearchConfig extends ElasticsearchConfigurationSupport {

//	@Override
//	protected FieldNamingStrategy fieldNamingStrategy() {
//		// TODO Auto-generated method stub
//		return super.fieldNamingStrategy();
//
//	}
//
//	@Override
//	public RestHighLevelClient elasticsearchClient() {
//		return RestClients.create(ClientConfiguration.create("localhost:9200")).rest();
//	}
//
//	@Bean
//	@Override
//	public ElasticsearchCustomConversions elasticsearchCustomConversions() {
//		return new ElasticsearchCustomConversions(Arrays.asList(new AddressToMap(), new MapToAddress()));
//	}
//
//	@WritingConverter
//	static class AddressToMap implements Converter<Address, Map<String, Object>> {
//
//		@Override
//		public Map<String, Object> convert(Address source) {
//
//			LinkedHashMap<String, Object> target = new LinkedHashMap<>();
//			target.put("ciudad", source.getCity());
//			// ...
//
//			return target;
//		}
//	}
//
//	@ReadingConverter
//	static class MapToAddress implements Converter<Map<String, Object>, Address> {
//
//		@Override
//		public Address convert(Map<String, Object> source) {
//
//			// ...
//			return address;
//		}
//	}
}