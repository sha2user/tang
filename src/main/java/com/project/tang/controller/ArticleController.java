package com.project.tang.controller;

import com.project.tang.service.ArticleBodyService;
import com.project.tang.service.ArticleCommentService;
import com.project.tang.service.ArticleService;
import com.project.tang.service.CategoryService;
import com.project.tang.vo.Result;
import com.project.tang.vo.params.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;
    @Autowired
    private ArticleBodyService articleBodyService;
    @Autowired
    private ArticleCommentService articleCommentService;
    @Autowired
    private CategoryService categoryService;

    @GetMapping("newArticle")
    public Result getNewArticle(){
        return articleService.getNewArticle();
    }

    @GetMapping("priceArticle")
    public Result getPriceArticle(){
        return articleService.getPriceArticle();
    }

    @GetMapping("contentArticle/{id}")
    public Result getArticleContentById(@PathVariable("id")String sid){
        return articleBodyService.getArticleContentById(sid);
    }
    @GetMapping("techArticle")
    public Result gettechArticle(){
        return articleService.gettechArticle();
    }
    @GetMapping("dataArticle")
    public Result getDataArticle(){
        return articleService.getDataArticle();
    }
    @GetMapping("picArticle")
    public Result getPicArticle(){
        return articleService.getPicArticle();
    }
    @GetMapping("hotArticle")
    public Result getHotArticle(){
        return articleService.getHotArticle();
    }
    @GetMapping("allArticleNumber")
    public Result getAllArticleNumber(){
        return articleService.getAllArticleNumber();
    }
    @PostMapping("getCurrent")
    public Result getCurrent(@RequestBody PageParamSecond pageParamSecond){
        return articleService.getCurrent(pageParamSecond);
    }
    @PostMapping("updateArticle")
    public Result updateArticle(@RequestBody SecondArticleParam articleParam){
        return articleService.updateArticle(articleParam);
    }
    @GetMapping("selectArticle/{keyWord}")
    public Result selectArticle(@PathVariable("keyWord")String keyWord){
        return articleService.selectArticle(keyWord);
    }



    @PostMapping("submitComment")
    public Result submitComment(@RequestBody ArticleCommentParam articleCommentParam){
        return articleCommentService.submitComment(articleCommentParam);
    }
    @GetMapping("articleComment/{id}")
    public Result getArticleCommentById(@PathVariable("id")String sid){
        return articleCommentService.getArticleCommentById(sid);
    }
    @GetMapping("removeComment/{id}")
    public Result removeCommentById(@PathVariable("id")String sid){
        return articleCommentService.removeCommentById(sid);
    }

    @GetMapping("categoryList")
    public Result getCategoryList(){
        return categoryService.getCategoryList();
    }

    @GetMapping("listCategory/{name}")
    public Result getListByCategory(@PathVariable("name")String name){
        return articleService.getListByCategory(name);
    }

    @GetMapping("removeArticle/{id}")
    public Result removeArticle(@PathVariable("id")String sid){
        return articleService.removeArticle(sid);
    }

    @PostMapping("addArticle")
    public Result addArticle(@RequestBody ArticleParam articleParam){
        return articleService.addArticle(articleParam);
    }

    @PostMapping("getPage")
    public Result getPage(@RequestBody PageParam pageParam){
        return articleService.getPage(pageParam);
    }

    @GetMapping("hotComList")
    public Result getHotComList(){
        return articleService.getHotComList();
    }

    @PostMapping("submitContent")
    public Result submitContent(@RequestBody ContentParam contentParam){
        return articleBodyService.submitContent(contentParam);
    }

}
