package com.myproject.blogplatform.dao;

import com.myproject.blogplatform.model.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface AuthorRepository extends JpaRepository<Author,Long> {
    Optional<Author> findAuthorByNickname(String author);

    Boolean existsAuthorByNickname(String nickname);
}
