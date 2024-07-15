package com.myproject.blogplatform.service;

import com.myproject.blogplatform.dao.ArticleRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ArticleService {
    private ArticleRepository articleRepository;
}
