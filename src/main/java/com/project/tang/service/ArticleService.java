package com.project.tang.service;

import com.project.tang.vo.Result;
import com.project.tang.vo.params.*;

public interface ArticleService {
    Result getNewArticle();
    Result getPriceArticle();
    Result gettechArticle();
    Result getDataArticle();
    Result getPicArticle();
    Result getHotArticle();
    Result getAllArticleNumber();
    Result getCurrent(PageParamSecond pageParamSecond);
    Result updateArticle(SecondArticleParam articleParam);
    Result selectArticle(String keyWord);


    Result getListByCategory(String name);
    Result removeArticle(String sid);
    Result addArticle(ArticleParam articleParam);
    Result getPage(PageParam pageParam);
    Result getHotComList();
}
