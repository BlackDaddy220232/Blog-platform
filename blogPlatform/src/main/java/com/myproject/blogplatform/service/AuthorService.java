package com.myproject.blogplatform.service;

import com.myproject.blogplatform.dao.ArticleRepository;
import com.myproject.blogplatform.dao.AuthorRepository;
import com.myproject.blogplatform.exception.ArticleNotFoundException;
import com.myproject.blogplatform.exception.AuthorNotFoundException;
import com.myproject.blogplatform.model.entity.Article;
import com.myproject.blogplatform.model.entity.Author;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class AuthorService {
  private AuthorRepository authorRepository;
  private ArticleRepository articleRepository;
  @Autowired
  public void setAuthorRepository(AuthorRepository authorRepository){
    this.authorRepository=authorRepository;
  }
  @Autowired
  public void setArticleRepository(ArticleRepository articleRepository){
    this.articleRepository=articleRepository;
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
  private static final String ARTICLE_WAS_RENAMED="Article \"%s\" was renamed";

  public String createAuthor(String nickname) {
    if (authorRepository.existsAuthorByNickname(nickname)) {
      throw new AuthorNotFoundException(String.format(AUTHOR_ALREADY_EXIST_MESSAGE, nickname));
    } else {
      Author author = new Author();
      author.setNickname(nickname);
      authorRepository.save(author);
      return String.format(AUTHOR_CREATE_MESSAGE, nickname);
    }
  }

  public String deleteAuthor(String nickname) {
    Author author = getAuthorByNickname(nickname);
    authorRepository.delete(author);
    return String.format(AUTHOR_DELETE_MESSAGE, nickname);
  }

  public List<Article> getArticlesByAuthor(String nickname) {
    Author author = getAuthorByNickname(nickname);
    return author.getArticles();
  }

  public String changeNickname(String oldNickname, String newNickname) {
    Author author = getAuthorByNickname(oldNickname);
    author.setNickname(newNickname);
    return String.format(CHANGE_NICKNAME, newNickname);
  }

  private Author getAuthorByNickname(String nickname) {
    return authorRepository
        .findAuthorByNickname(nickname)
        .orElseThrow(
            () ->
                new AuthorNotFoundException(
                    String.format(AUTHOR_DOES_NOT_EXIST_MESSAGE, nickname)));
  }

  public String createAtricle(String title, String nickname) {
    Author author = getAuthorByNickname(nickname);
    if (author.getArticleByTitle(title)!=null) {
      throw new ArticleNotFoundException(String.format(ARTICLE_EXIST, title));
    } else {
      Article article = new Article();
      article.setTitle(title);
      author.addArticle(article);
      authorRepository.save(author);
      return String.format(ARTICLE_WAS_ADDED, title);
    }
  }

  public String deleteArticle(String title, String nickname) {
    Author author = getAuthorByNickname(nickname);
    if (author.getArticleByTitle(title)==null) {
      throw new ArticleNotFoundException(String.format(ARTICLE_DOES_NOT_EXIST, title));
    } else {
      author.getArticles().removeIf(article -> article.getTitle().equals(title));
      authorRepository.save(author);
      return String.format(ARTICLE_WAS_DELETED, title);
    }
  }
  public String changeArticlesTitle(String nickname,String oldTitle, String newTitle){
    Author author = getAuthorByNickname(nickname);
    Article article=author.getArticleByTitle(oldTitle);
    if (article==null) {
      throw new ArticleNotFoundException(String.format(ARTICLE_DOES_NOT_EXIST, oldTitle));
    } else {
      article.setTitle(newTitle);
      articleRepository.save(article);
      return String.format(ARTICLE_WAS_RENAMED,newTitle);
    }
  }
}
