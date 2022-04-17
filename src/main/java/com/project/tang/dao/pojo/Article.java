package com.project.tang.dao.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

@Data
public class Article {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    private Integer commentCounts;
    private Long createDate;
    private String title;
    private Integer viewCounts;
    private String author;
    private String categoryName;
}
