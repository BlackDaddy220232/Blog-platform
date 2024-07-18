package com.myproject.blogplatform.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "authors")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Author {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "nickname")
  private String nickname;

  private int countArticle;

  @OneToMany(
      mappedBy = "author",
      fetch = FetchType.EAGER,
      cascade = {
        CascadeType.DETACH,
        CascadeType.REFRESH,
        CascadeType.MERGE,
        CascadeType.PERSIST,
        CascadeType.REMOVE
      })
  @JsonIgnore
  private List<Article> articles;

  public Article getArticleByTitle(String title) {
    return articles.stream().filter(p -> p.getTitle().equals(title)).findFirst().orElse(null);
  }

  public void addArticle(Article article) {
    articles.add(article);
  }
}
