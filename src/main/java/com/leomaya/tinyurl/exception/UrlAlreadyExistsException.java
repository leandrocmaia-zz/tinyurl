package com.leomaya.tinyurl.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason= "Url already exists.")
public class UrlAlreadyExistsException extends RuntimeException {}
