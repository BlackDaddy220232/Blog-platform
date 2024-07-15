package com.myproject.blogplatform.controller;

import com.myproject.blogplatform.dao.AuthorRepository;
import com.myproject.blogplatform.service.AuthorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/authors")
public class AuthorController {
    private AuthorService authorService;
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
}
