package com.example.es.entity.search.document;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocaleEntity {
    @Field(type = FieldType.Keyword)
    private String en;
    @Field(type = FieldType.Keyword)
    private String ar;
}
