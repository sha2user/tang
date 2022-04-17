package com.project.tang.service;

import com.project.tang.vo.Result;
import com.project.tang.vo.params.ArticleCommentParam;

public interface ArticleCommentService {
    Result submitComment(ArticleCommentParam articleCommentParam);
    Result getArticleCommentById(String sid);
    Result removeCommentById(String sid);
}
