package com.myproject.blogplatform.service;

import com.myproject.blogplatform.dao.ArticleRepository;
import com.myproject.blogplatform.dao.AuthorRepository;
import com.myproject.blogplatform.exception.ArticleNotFoundException;
import com.myproject.blogplatform.exception.ArticleTakenException;
import com.myproject.blogplatform.exception.AuthorNotFoundException;
import com.myproject.blogplatform.exception.AuthorTakenException;
import com.myproject.blogplatform.model.entity.Article;
import com.myproject.blogplatform.model.entity.Author;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@Transactional
public class AuthorService {
  private AuthorRepository authorRepository;
  private ArticleRepository articleRepository;

  @Autowired
  public void setAuthorRepository(AuthorRepository authorRepository) {
    this.authorRepository = authorRepository;
  }

  @Autowired
  public void setArticleRepository(ArticleRepository articleRepository) {
    this.articleRepository = articleRepository;
  }

  private static final String AUTHOR_CREATE_MESSAGE = "Author \"%s\" creat successfully";
  private static final String AUTHOR_ALREADY_EXIST_MESSAGE = "Author \"%S\" already exists";
  private static final String AUTHOR_DOES_NOT_EXIST_MESSAGE = "Author \"%s\" doesn't exist";
  private static final String AUTHOR_DELETE_MESSAGE = "Author \"%s\" has been deleted";
  private static final String CHANGE_NICKNAME = "Nickname was changed on \"%s\"";
  private static final String ARTICLE_EXIST = "Article \"%s\" already exist";
  private static final String ARTICLE_DOES_NOT_EXIST = "Article \"%s\" doesn't exist";
  private static final String ARTICLE_WAS_ADDED = "Article \"%s\" was added";
  private static final String ARTICLE_WAS_DELETED = "Article \"%s\" was deleted";
  private static final String ARTICLE_WAS_RENAMED = "Article \"%s\" was renamed";
  private static final String EMPTY_FIELD = "Fields cannot be empty";

  public String createAuthor(String nickname) {
    if (Boolean.TRUE.equals(authorRepository.existsAuthorByNickname(nickname))) {
      throw new AuthorTakenException(String.format(AUTHOR_ALREADY_EXIST_MESSAGE, nickname));
    }
    validateInput(nickname);
    Author author = new Author();
    author.setNickname(nickname);
    authorRepository.save(author);
    return String.format(AUTHOR_CREATE_MESSAGE, nickname);
  }

  public String deleteAuthor(String nickname) {
    Author author = getAuthorByNickname(nickname);
    authorRepository.delete(author);
    return String.format(AUTHOR_DELETE_MESSAGE, nickname);
  }

  public List<Article> getArticlesByAuthor(String nickname) {
    return getAuthorByNickname(nickname).getArticles();
  }

  public String changeNickname(String oldNickname, String newNickname) {
    validateInput(oldNickname, newNickname);
    Author author = getAuthorByNickname(oldNickname);
    if (Boolean.TRUE.equals(authorRepository.existsAuthorByNickname(newNickname))) {
      throw new AuthorTakenException(String.format(AUTHOR_ALREADY_EXIST_MESSAGE, newNickname));
    }
    author.setNickname(newNickname);
    authorRepository.save(author);
    return String.format(CHANGE_NICKNAME, newNickname);
  }

  public String createArticle(String title, String nickname) {
    validateInput(title, nickname);
    Author author = getAuthorByNickname(nickname);
    if (author.getArticleByTitle(title) != null) {
      throw new ArticleTakenException(String.format(ARTICLE_EXIST, title));
    }
    Article article = new Article();
    article.setTitle(title);
    article.setAuthor(author);
    author.addArticle(article);
    authorRepository.save(author);
    return String.format(ARTICLE_WAS_ADDED, title);
  }

  public String deleteArticle(String title, String nickname) {
    Author author = getAuthorByNickname(nickname);
    Article article = author.getArticleByTitle(title);
    if (article == null) {
      throw new ArticleNotFoundException(String.format(ARTICLE_DOES_NOT_EXIST, title));
    }
    author.getArticles().removeIf(p -> p.getTitle().equals(title));
    articleRepository.delete(article);
    authorRepository.save(author);
    return String.format(ARTICLE_WAS_DELETED, title);
  }

  public String changeArticlesTitle(String nickname, String oldTitle, String newTitle) {
    validateInput(nickname, oldTitle, newTitle);
    Author author = getAuthorByNickname(nickname);
    Article article = getArticleByTitle(author, oldTitle);
    if (author.getArticleByTitle(newTitle) != null) {
      throw new ArticleTakenException(String.format(ARTICLE_EXIST, newTitle));
    }
    article.setTitle(newTitle);
    articleRepository.save(article);
    return String.format(ARTICLE_WAS_RENAMED, newTitle);
  }

  private void validateInput(String... inputs) {
    if (Boolean.TRUE.equals(Arrays.stream(inputs).anyMatch(String::isEmpty))) {
      throw new IllegalArgumentException(EMPTY_FIELD);
    }
  }

  private Author getAuthorByNickname(String nickname) {
    return authorRepository
        .findAuthorByNickname(nickname)
        .orElseThrow(
            () ->
                new AuthorNotFoundException(
                    String.format(AUTHOR_DOES_NOT_EXIST_MESSAGE, nickname)));
  }

  private Article getArticleByTitle(Author author, String title) {
    return articleRepository
        .findArticleByAuthorAndTitle(author, title)
        .orElseThrow(
            () -> new ArticleNotFoundException(String.format(ARTICLE_DOES_NOT_EXIST, title)));
  }
}
