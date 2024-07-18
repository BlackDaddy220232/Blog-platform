package com.myproject.blogplatform.exception;

public class ArticleTakenException extends RuntimeException {
  public ArticleTakenException(String message) {
    super(message);
  }
}
