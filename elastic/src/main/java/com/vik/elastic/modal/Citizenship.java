package com.vik.elastic.modal;

//import com.taqniat.coral.util.CountryUtil;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Citizenship {
//	private Long parentId;
//	private String id;
//	@EmbeddedId
	private CompositeID id;
	private String countryCode; // 2 letter code
//	private String countryName;

//	public String getCountryName() {
////		return CountryUtil.getCountryNameByCode(countryCode);
//	}
}
