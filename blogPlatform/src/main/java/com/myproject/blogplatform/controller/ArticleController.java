package com.myproject.blogplatform.controller;

import com.myproject.blogplatform.model.entity.Article;
import com.myproject.blogplatform.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/article")
public class ArticleController {
  ArticleService articleService;

  @Autowired
  public void setArticleService(ArticleService articleService) {
    this.articleService = articleService;
  }

  @GetMapping("/getAllArticles")
  public List<Article> getAllArticles() {
    return articleService.getAllArticles();
  }
}
