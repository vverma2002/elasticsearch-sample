package com.example.es.entity.search.document;

import com.example.es.annotation.search.IndexVersion;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IndexVersion("v1")
@Mapping(mappingPath = "/mapping/users.json")
@Setting(settingPath = "/setting/users.json")
@Document(indexName = "users", createIndex = false)
public class UserEntity implements Entity {
    @Id
    private Long id;

    @MultiField(mainField = @Field(type = FieldType.Text),
            otherFields = @InnerField(type = FieldType.Keyword, suffix = "key"))
    private String nameEn;

    @MultiField(mainField = @Field(type = FieldType.Text),
            otherFields = @InnerField(type = FieldType.Keyword, suffix = "key"))
    private String nameAr;

    @Field(type = FieldType.Text)
    private String nameEnCopy;

    @Field(type = FieldType.Keyword)
    private String gender;

    @JsonFormat(pattern = "dd-MM-yyyy")
    @Field(type = FieldType.Date)
    private LocalDate dateOfBirth;

    @Field(type = FieldType.Integer)
    private Integer favouriteNumber;

    @Field(type = FieldType.Nested)
    private LocaleEntity placeOfBirth;
}
