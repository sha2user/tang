package com.project.tang.vo.params;

import lombok.Data;

@Data
public class ArticleCommentParam {
    private String content;
    private String articleId;
    private String username;
}
