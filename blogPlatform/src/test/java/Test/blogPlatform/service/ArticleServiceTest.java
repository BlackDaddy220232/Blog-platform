package Test.blogPlatform.service;

import com.myproject.blogplatform.dao.ArticleRepository;
import com.myproject.blogplatform.model.entity.Article;
import com.myproject.blogplatform.service.ArticleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ArticleServiceTest {
  @Mock private ArticleRepository articleRepository;
  @InjectMocks private ArticleService articleService;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testGetAllArticles() {
    // Arrange
    List<Article> expectedArticles = new ArrayList<>();
    Article article1 = new Article();
    article1.setTitle("Article 1");
    Article article2 = new Article();
    article2.setTitle("Article 2");
    expectedArticles.add(article1);
    expectedArticles.add(article2);

    when(articleRepository.findAll()).thenReturn(expectedArticles);

    // Act
    List<Article> result = articleService.getAllArticles();

    // Assert
    assertEquals(expectedArticles, result);
    verify(articleRepository, times(1)).findAll();
  }
}
