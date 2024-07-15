package com.myproject.blogplatform.controller;

import com.myproject.blogplatform.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/authors")
public class AuthorController {
    private AuthorService authorService;
    @Autowired
    public void setAuthorService(AuthorService authorService) {
        this.authorService = authorService;
    }

    @PostMapping("/createAuthor")
    public ResponseEntity<Object> createAuthor(@RequestParam String nickname){
        return ResponseEntity.ok(authorService.createAuthor(nickname));
    }
    @DeleteMapping("/deleteAuthor")
    public ResponseEntity<Object> deleteAuthor(@RequestParam String nickname) {
        return ResponseEntity.ok(authorService.deleteAuthor(nickname));
    }
    @GetMapping("/getAuthorArticles")
    public ResponseEntity<Object> getAuthorsArticles(@RequestParam String nickname){
        return ResponseEntity.ok(authorService.getArticlesByAuthor(nickname));
    }
    @GetMapping("/changeNickname")
    public ResponseEntity<Object> changeNickname(@RequestParam String oldNickname, String newNickname){
        return ResponseEntity.ok(authorService.changeNickname(oldNickname, newNickname));
    }
    @PostMapping("/createArticle")
    public ResponseEntity<Object> createArticle(@RequestParam String nickname, String title){
        return ResponseEntity.ok(authorService.createAtricle(title,nickname));
    }
    @DeleteMapping("/deleteArticle")
    public ResponseEntity<Object> deleteArticle(@RequestParam String nickname,String title){
        return ResponseEntity.ok(authorService.deleteArticle(nickname, title));
    }
    @PatchMapping("/changeArticle")
    public ResponseEntity<Object> changeTitle(@RequestParam String nickname, String oldTitle, String newTitle){
        return ResponseEntity.ok(authorService.changeArticlesTitle(nickname, oldTitle, newTitle));

    }

}
