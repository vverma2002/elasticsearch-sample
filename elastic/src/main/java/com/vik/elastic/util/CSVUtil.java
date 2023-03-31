package com.vik.elastic.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StringUtils;

public class CSVUtil {

	public static List<String> readCsvFile(String fileName) throws IOException {
		List<String> dataList = new ArrayList<>();
		ClassPathResource resource = new ClassPathResource(fileName);
		BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()));
		String line;
		while ((line = reader.readLine()) != null) {
			String value = StringUtils.trimWhitespace(line);
			dataList.add(value);
		}
		return dataList;
	}
}
