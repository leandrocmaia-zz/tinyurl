package com.leomaya.tinyurl.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason= "Url is invalid.")
public class InvalidUrlException extends RuntimeException {}
