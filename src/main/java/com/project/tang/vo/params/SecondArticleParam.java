package com.project.tang.vo.params;

import lombok.Data;

@Data
public class SecondArticleParam {
    private String id;
    private String title;
    private String author;
    private String categoryName;
    private String createDate;
    private String viewCounts;
    private String commentCounts;
}
