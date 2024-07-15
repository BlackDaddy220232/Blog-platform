package com.myproject.blogplatform.service;

import com.myproject.blogplatform.dao.AuthorRepository;
import com.myproject.blogplatform.exception.AuthorNotFoundException;
import com.myproject.blogplatform.model.entity.Article;
import com.myproject.blogplatform.model.entity.Author;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class AuthorService {
  private AuthorRepository authorRepository;
  private static final String AUTHOR_CREATE_MESSAGE = "Author \"%s\" creat successfully";
  private static final String AUTHOR_ALREADY_EXIST_MESSAGE = "Author \"%S\" already exists";
  private static final String AUTHOR_DOES_NOT_EXIST_MESSAGE = "Author \"%s\" doesn't exist";
  private static final String AUTHOR_DELETE_MESSAGE = "Author \"%s\" has been deleted";
  private static final String CHANGE_NICKNAME="Nickname was changed on \"%s\"";

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

  private String changeNickname(String oldNickname, String newNickname) {
    Author author = getAuthorByNickname(oldNickname);
    author.setNickname(newNickname);
    return String.format(CHANGE_NICKNAME,newNickname);
  }
  private Author getAuthorByNickname(String nickname) {
    return authorRepository.findAuthorByNickname(nickname)
            .orElseThrow(() ->
                    new AuthorNotFoundException(String.format(AUTHOR_DOES_NOT_EXIST_MESSAGE, nickname)));
  }
}
