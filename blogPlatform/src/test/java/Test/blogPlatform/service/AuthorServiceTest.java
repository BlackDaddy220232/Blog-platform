package Test.blogPlatform.service;

import com.myproject.blogplatform.dao.ArticleRepository;
import com.myproject.blogplatform.dao.AuthorRepository;
import com.myproject.blogplatform.exception.ArticleNotFoundException;
import com.myproject.blogplatform.exception.ArticleTakenException;
import com.myproject.blogplatform.exception.AuthorNotFoundException;
import com.myproject.blogplatform.exception.AuthorTakenException;
import com.myproject.blogplatform.model.entity.Article;
import com.myproject.blogplatform.model.entity.Author;
import com.myproject.blogplatform.service.AuthorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

 class AuthorServiceTest {
    @Mock
    private AuthorRepository authorRepository;
    @Mock
    private ArticleRepository articleRepository;
    @InjectMocks
    private AuthorService authorService;

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
    private static final String EMPTY_NICKNAME="Nickname cannot be empty";
    private static final String EMPTY_TITLE="Title cannot be empty";
    private static final String EXISTING_NICKNAME = "existingNickname";
    private static final String NOT_EXISTING_NICKNAME = "notExistingNickname";
    private static final String NOT_EXISTING_TITLE="notExistingTitle";
    private static final String EXISTING_TITLE="existingTitle";

    private Author author;
    private Article article1, article2;
    List<Article> articles;
    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
        initialization();

    }
    public void initialization(){
        author = new Author();
        author.setNickname(EXISTING_NICKNAME);
        article1 = new Article();
        article2 = new Article();
        articles= new ArrayList<>();
        article1.setTitle(EXISTING_TITLE);
        article2.setTitle("art2");
        article1.setAuthor(author);
        article2.setAuthor(author);
        articles.add(article1);
        articles.add(article2);
        author.setArticles(articles);
    }
    @Test
    void testCreateAuthorWithExistingNickname() {

        when(authorRepository.existsAuthorByNickname(NOT_EXISTING_NICKNAME)).thenReturn(true);

        // Act and Assert
        AuthorTakenException exception = assertThrows(AuthorTakenException.class, () -> {
            authorService.createAuthor(NOT_EXISTING_NICKNAME);
        });
        assertEquals(String.format(AUTHOR_ALREADY_EXIST_MESSAGE, NOT_EXISTING_NICKNAME), exception.getMessage());
        verify(authorRepository, times(1)).existsAuthorByNickname(NOT_EXISTING_NICKNAME);
        verifyNoMoreInteractions(authorRepository);
    }
    @Test
    void testCreateAuthorWithEmptyNickname() {
        when(authorRepository.existsAuthorByNickname("")).thenReturn(false);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            authorService.createAuthor("");
        });
        assertEquals(EMPTY_NICKNAME, exception.getMessage());
        verify(authorRepository, never()).save(any(Author.class));
    }
    @Test
    void testCreateAuthorWithValidNickname() {
        when(authorRepository.existsAuthorByNickname(EXISTING_NICKNAME)).thenReturn(false);
        String message = authorService.createAuthor(EXISTING_NICKNAME);

        assertEquals(String.format(AUTHOR_CREATE_MESSAGE, EXISTING_NICKNAME), message);
        verify(authorRepository, times(1)).existsAuthorByNickname(EXISTING_NICKNAME);
        verify(authorRepository, times(1)).save(any(Author.class));
    }
    @Test
     void testDeleteAuthorWithValidNickname() {
        when(authorRepository.findAuthorByNickname(EXISTING_NICKNAME)).thenReturn(Optional.of(author));
        String result = authorService.deleteAuthor(EXISTING_NICKNAME);

        verify(authorRepository, times(1)).findAuthorByNickname(EXISTING_NICKNAME);
        verify(authorRepository, times(1)).delete(author);
        assertEquals(String.format(AUTHOR_DELETE_MESSAGE, EXISTING_NICKNAME), result);
    }
    @Test
    void testDeleteAuthorWithNotValidNickname(){
        when(authorRepository.findAuthorByNickname(NOT_EXISTING_NICKNAME)).thenReturn(Optional.empty());
        AuthorNotFoundException exception=assertThrows(AuthorNotFoundException.class, ()->authorService.deleteAuthor(NOT_EXISTING_NICKNAME));
        assertEquals(String.format(AUTHOR_DOES_NOT_EXIST_MESSAGE,NOT_EXISTING_NICKNAME),exception.getMessage());
    }
    @Test
    void testGetArticleByAuthorWithValidNickname(){
        when(authorRepository.findAuthorByNickname(EXISTING_NICKNAME)).thenReturn(Optional.of(author));

        List<Article> result = authorService.getArticlesByAuthor(EXISTING_NICKNAME);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertSame(articles, result);
    }
    @Test
    void testGetArticleByAuthorWithNotValidNickname(){
        when(authorRepository.findAuthorByNickname(NOT_EXISTING_NICKNAME)).thenReturn(Optional.empty());

        AuthorNotFoundException exception=assertThrows(AuthorNotFoundException.class, ()->authorService.getArticlesByAuthor(NOT_EXISTING_NICKNAME));
        assertEquals(String.format(AUTHOR_DOES_NOT_EXIST_MESSAGE,NOT_EXISTING_NICKNAME),exception.getMessage());
    }
    @Test
    void testChangeNicknameValidNickname() {
        when(authorRepository.existsAuthorByNickname(NOT_EXISTING_NICKNAME)).thenReturn(false);
        when(authorRepository.findAuthorByNickname(EXISTING_NICKNAME)).thenReturn(Optional.of(author));

        // Act
        String result = authorService.changeNickname(EXISTING_NICKNAME, NOT_EXISTING_NICKNAME);

        // Assert
        verify(authorRepository, times(1)).existsAuthorByNickname(NOT_EXISTING_NICKNAME);
        verify(authorRepository, times(1)).save(author);
        assertEquals(String.format(CHANGE_NICKNAME, NOT_EXISTING_NICKNAME), result);
    }
    @Test
    void testChangeNicknameWithEmptyNickname() {
        when(authorRepository.existsAuthorByNickname("")).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> {
            authorService.changeNickname(EXISTING_NICKNAME, "");
        }, "EMPTY_NICKNAME");
    }
    @Test
    void testChangeNicknameWithExistingNickname() {
        when(authorRepository.existsAuthorByNickname(EXISTING_NICKNAME)).thenReturn(true);
        when(authorRepository.findAuthorByNickname(EXISTING_NICKNAME)).thenReturn(Optional.of(author));

        assertThrows(AuthorTakenException.class, ()->{
            authorService.changeNickname(EXISTING_NICKNAME,EXISTING_NICKNAME);
        },String.format(AUTHOR_ALREADY_EXIST_MESSAGE,EXISTING_NICKNAME));
    }
    @Test
    void testCreateArticleWithExistingTitle() {

        when(authorRepository.findAuthorByNickname(EXISTING_NICKNAME)).thenReturn(Optional.of(author));
        // Act and Assert
        assertThrows(ArticleTakenException.class, ()->{
            authorService.createArticle(EXISTING_TITLE,EXISTING_NICKNAME);
        },String.format(ARTICLE_EXIST,EXISTING_TITLE));
    }
    @Test
    void testCreateArticleWithEmptyTitle() {
        // Arrange
        when(authorRepository.findAuthorByNickname(EXISTING_NICKNAME)).thenReturn(Optional.of(author));

        // Act and Assert
        assertThrows(IllegalArgumentException.class, ()->{
            authorService.createArticle("",EXISTING_NICKNAME);
        },String.format(EMPTY_TITLE));
    }

  @Test
  void testCreateArticleSuccessfully() {
    when(authorRepository.findAuthorByNickname(EXISTING_NICKNAME)).thenReturn(Optional.of(author));

    // Act
    String result = authorService.createArticle(NOT_EXISTING_TITLE, EXISTING_NICKNAME);

    // Assert
    assertEquals(String.format(ARTICLE_WAS_ADDED, NOT_EXISTING_TITLE), result);
    assertNotNull(author.getArticleByTitle(NOT_EXISTING_TITLE));
    verify(authorRepository, times(1)).save(author);
        }
    @Test
    void testDeleteArticleWithNonExistingTitle() {
        // Arrange
        when(authorRepository.findAuthorByNickname(EXISTING_NICKNAME)).thenReturn(Optional.of(author));

        // Act and Assert
        assertThrows(ArticleNotFoundException.class, ()->{
            authorService.deleteArticle(NOT_EXISTING_TITLE,EXISTING_NICKNAME);
        },String.format(ARTICLE_DOES_NOT_EXIST,NOT_EXISTING_TITLE));
    }
    @Test
    void testDeleteArticleSuccessfully() {
        when(authorRepository.findAuthorByNickname(EXISTING_NICKNAME)).thenReturn(Optional.of(author));

        // Act
        String result = authorService.deleteArticle(EXISTING_TITLE, EXISTING_NICKNAME);

        // Assert
        assertEquals(String.format(ARTICLE_WAS_DELETED, EXISTING_TITLE), result);
        assertNull(author.getArticleByTitle(EXISTING_TITLE));
        verify(articleRepository, times(1)).delete(article1);
        verify(authorRepository, times(1)).save(author);
    }
    @Test
    void testChangeArticleTitleWithNonExistingArticle() {
        when(authorRepository.findAuthorByNickname(EXISTING_NICKNAME)).thenReturn(Optional.of(author));

        // Act and Assert
        assertThrows(ArticleNotFoundException.class, ()->{
            authorService.changeArticlesTitle(EXISTING_NICKNAME,NOT_EXISTING_TITLE,EXISTING_NICKNAME);
        },String.format(ARTICLE_DOES_NOT_EXIST,NOT_EXISTING_TITLE));
    }
    @Test
    void testChangeArticleTitleWithExistingNewTitle() {
        // Arrange
        when(authorRepository.findAuthorByNickname(EXISTING_NICKNAME)).thenReturn(Optional.of(author));

        // Act and Assert
        assertThrows(ArticleTakenException.class, ()->{
            authorService.changeArticlesTitle(EXISTING_NICKNAME,EXISTING_TITLE,EXISTING_TITLE);
        },String.format(ARTICLE_EXIST,EXISTING_TITLE));
    }
    @Test
    void testChangeArticleTitleSuccessfully() {
        when(authorRepository.findAuthorByNickname(EXISTING_NICKNAME)).thenReturn(Optional.of(author));

        // Act
        String result = authorService.changeArticlesTitle(EXISTING_NICKNAME, EXISTING_TITLE, NOT_EXISTING_TITLE);

        // Assert
       assertEquals(String.format(ARTICLE_WAS_RENAMED, NOT_EXISTING_TITLE), result);
       assertNull(author.getArticleByTitle(EXISTING_TITLE));
       assertNotNull(author.getArticleByTitle(NOT_EXISTING_TITLE));
       verify(articleRepository, times(1)).save(article1);
    }
}
