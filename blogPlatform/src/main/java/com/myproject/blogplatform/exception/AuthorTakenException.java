package com.myproject.blogplatform.exception;

public class AuthorTakenException extends RuntimeException {
    public AuthorTakenException(String message){
        super(message);
    }
}
