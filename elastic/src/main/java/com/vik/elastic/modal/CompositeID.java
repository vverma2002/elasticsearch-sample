package com.vik.elastic.modal;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
//@Embeddable
public class CompositeID implements Serializable {
	private String id;
//	@ManyToOne(optional = false)
//	@JoinColumn(name = "parent_id", nullable = false)
//	@Exclude
//	private Book parent;
	private Long parent_id;
}
