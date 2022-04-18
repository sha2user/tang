package com.project.tang.service;

import com.project.tang.vo.Result;
import com.project.tang.vo.params.ContentParam;

public interface ArticleBodyService {
    Result getArticleContentById(String sid);
    Result submitContent(ContentParam contentParam);
}
