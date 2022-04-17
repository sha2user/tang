package com.project.tang.dao.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

@Data
public class ArticleComment {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    private String content;
    private Long createDate;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long articleId;
    private String username;
}
