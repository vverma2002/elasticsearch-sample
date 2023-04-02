package com.vik.elastic.modal;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RequestList {
	private List<Request> RequestList = new ArrayList<>();
}