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
  private static final String EMPTY_NICKNAME = "Nickname cannot be empty";
  private static final String EMPTY_TITLE = "Title cannot be empty";

  /**
   * Creates a new author with the given nickname.
   *
   * @param nickname The nickname of the new author.
   * @return A success message indicating the author was created successfully.
   * @throws AuthorTakenException If the author with the given nickname already exists.
   * @throws IllegalArgumentException if the nickname is empty.
   */
  public String createAuthor(String nickname) {
    if (Boolean.TRUE.equals(authorRepository.existsAuthorByNickname(nickname))) {
      throw new AuthorTakenException(String.format(AUTHOR_ALREADY_EXIST_MESSAGE, nickname));
    } else {
      if (Boolean.FALSE.equals(nickname.isEmpty())) {
        Author author = new Author();
        author.setNickname(nickname);
        authorRepository.save(author);
        return String.format(AUTHOR_CREATE_MESSAGE, nickname);
      } else {
        throw new IllegalArgumentException(String.format(EMPTY_NICKNAME));
      }
    }
  }

  /**
   * Deletes the author with the given nickname.
   *
   * @param nickname The nickname of the author to be deleted.
   * @return A success message indicating the author was deleted successfully.
   */
  public String deleteAuthor(String nickname) {
    Author author = getAuthorByNickname(nickname);
    authorRepository.delete(author);
    return String.format(AUTHOR_DELETE_MESSAGE, nickname);
  }

  /**
   * Retrieves a list of articles associated with the author having the given nickname.
   *
   * @param nickname The nickname of the author whose articles are to be retrieved.
   * @return The list of articles associated with the author.
   */
  public List<Article> getArticlesByAuthor(String nickname) {
    Author author = getAuthorByNickname(nickname);
    return author.getArticles();
  }

  /**
   * Changes the nickname of an author.
   *
   * @param oldNickname The current nickname of the author.
   * @param newNickname The new nickname to be assigned to the author.
   * @return A success message indicating the nickname was changed successfully.
   * @throws AuthorNotFoundException If the new nickname already exists.
   * @throws IllegalArgumentException If the new nickname is empty.
   */
  public String changeNickname(String oldNickname, String newNickname) {
    if (Boolean.TRUE.equals(newNickname.isEmpty())) {
      throw new IllegalArgumentException(EMPTY_NICKNAME);
    }
    Author author = getAuthorByNickname(oldNickname);
    if (Boolean.FALSE.equals(authorRepository.existsAuthorByNickname(newNickname))) {
      author.setNickname(newNickname);
      authorRepository.save(author);
      return String.format(CHANGE_NICKNAME, newNickname);
    } else {
      throw new AuthorTakenException(String.format(AUTHOR_ALREADY_EXIST_MESSAGE, newNickname));
    }
  }

  /**
   * Retrieves the author with the given nickname from the database.
   *
   * @param nickname The nickname of the author to be retrieved.
   * @return The author with the given nickname.
   * @throws AuthorNotFoundException If the author with the given nickname does not exist.
   */
  private Author getAuthorByNickname(String nickname) {
    return authorRepository
        .findAuthorByNickname(nickname)
        .orElseThrow(
            () ->
                new AuthorNotFoundException(
                    String.format(AUTHOR_DOES_NOT_EXIST_MESSAGE, nickname)));
  }

  /**
   * Creates a new article with the given title and associates it with the author having the
   * provided nickname.
   *
   * @param title The title of the new article.
   * @param nickname The nickname of the author to whom the article will be associated.
   * @return A success message indicating the article was created successfully.
   * @throws ArticleTakenException If the article with the given title already exists.
   * @throws IllegalArgumentException If title of article is empty.
   */
  public String createArticle(String title, String nickname) {
    Author author = getAuthorByNickname(nickname);
    if (author.getArticleByTitle(title) != null) {
      throw new ArticleTakenException(String.format(ARTICLE_EXIST, title));
    }
    if (Boolean.TRUE.equals(title.isEmpty())) {
      throw new IllegalArgumentException(String.format(EMPTY_TITLE, title));
    } else {
      Article article = new Article();
      article.setTitle(title);
      article.setAuthor(author);
      author.addArticle(article);
      authorRepository.save(author);
      return String.format(ARTICLE_WAS_ADDED, title);
    }
  }

  /**
   * Deletes the article with the given title, associated with the author having the provided
   * nickname.
   *
   * @param title The title of the article to be deleted.
   * @param nickname The nickname of the author whose article is to be deleted.
   * @return A success message indicating the article was deleted successfully.
   * @throws ArticleNotFoundException If the article with the given title does not exist.
   */
  public String deleteArticle(String title, String nickname) {
    Author author = getAuthorByNickname(nickname);
    Article article = author.getArticleByTitle(title);
    if (article == null) {
      throw new ArticleNotFoundException(String.format(ARTICLE_DOES_NOT_EXIST, title));
    } else {
      author.getArticles().removeIf(p -> p.getTitle().equals(title));
      articleRepository.delete(article);
      authorRepository.save(author);
      return String.format(ARTICLE_WAS_DELETED, title);
    }
  }

  /**
   * Changes the title of an article associated with the author having the provided nickname.
   *
   * @param nickname The nickname of the author whose article is to be renamed.
   * @param oldTitle The current title of the article.
   * @param newTitle The new title to be assigned to the article.
   * @return A success message indicating the article was renamed successfully.
   * @throws ArticleNotFoundException If the article with the given old title does not exist, or if
   *     the new title already exists.
   * @throws ArticleTakenException If article is taken and not available
   */
  public String changeArticlesTitle(String nickname, String oldTitle, String newTitle) {
    Author author = getAuthorByNickname(nickname);
    Article article = author.getArticleByTitle(oldTitle);
    if (article == null) {
      throw new ArticleNotFoundException(String.format(ARTICLE_DOES_NOT_EXIST, oldTitle));
    }
    if (author.getArticleByTitle(newTitle) == null) {
      article.setTitle(newTitle);
      articleRepository.save(article);
      return String.format(ARTICLE_WAS_RENAMED, newTitle);
    } else {
      throw new ArticleTakenException(String.format(ARTICLE_EXIST, newTitle));
    }
  }
}
