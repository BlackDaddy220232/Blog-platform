package com.myproject.blogplatform.service;

import com.myproject.blogplatform.dao.ArticleRepository;
import com.myproject.blogplatform.model.entity.Article;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class ArticleService {
    private ArticleRepository articleRepository;
    @Autowired
    public void setArticleRepository(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }
    /**
     * This method retrieves all the articles stored in the article repository.
     *
     * @return a list of all the articles
     */
    public List<Article> getAllArticles(){
        return articleRepository.findAll();
    }
}
