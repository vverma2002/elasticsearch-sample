package com.vik.elastic.elastic;

import java.util.Objects;

import org.springframework.data.elasticsearch.annotations.Document;

import com.vik.elastic.modal.Request;

import lombok.Getter;

@Getter
public enum IndexType {
	BOOK(Book.class),
	REQUEST(Request.class);

	private final Class<?> type;
	private final String index;

	IndexType(Class<?> type) {
		this.type = Objects.requireNonNull(type, "Type cannot be null");
		Document documentAnnotation = type.getAnnotation(Document.class);
		this.index = Objects.requireNonNull(documentAnnotation, "Document annotation not found").indexName();
	}

	public static IndexType searchByType(Class<?> type) {
		for (IndexType indexType : IndexType.values()) {
			if (indexType.getType().equals(type)) {
				return indexType;
			}
		}
		return null;
	}
}
