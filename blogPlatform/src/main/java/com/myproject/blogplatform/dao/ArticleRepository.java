package com.myproject.blogplatform.dao;

import com.myproject.blogplatform.model.entity.Article;
import com.myproject.blogplatform.model.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
  Optional<Article> findArticleByAuthorAndTitle(Author author, String title);

  Boolean existsArticleByTitle(String article);
}
