package com.leomaya.tinyurl.controller;

import com.leomaya.tinyurl.exception.UrlAlreadyExistsException;
import com.leomaya.tinyurl.model.Url;
import com.leomaya.tinyurl.service.UrlService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RestController
public class UrlController {

    UrlService urlService;

    private final Counter redirects = Metrics.counter("redirects.count");
    private final Counter notFound = Metrics.counter("notfound.count");

    @Autowired
    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }


    @PutMapping(value = "/url",
                consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
                produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Url createUrl(@RequestBody Input input) throws UrlAlreadyExistsException {
        return urlService.insert(input.getOriginalUrl());
    }

    @GetMapping(value = "/{url}", produces = "text/html")
    public ModelAndView get(@PathVariable("url") String tinyUrl, HttpServletRequest request) {
        Optional<Url> url = urlService.find(tinyUrl);
        if (url.isPresent()) {
            redirects.increment();
            return new ModelAndView(new RedirectView(url.get().getOriginalUrl(), true));
        } else {
            ModelMap model = new ModelMap();
            model.put("uri", request.getRequestURL());
            notFound.increment();
            return new ModelAndView("404", model);
        }
    }


    @Getter
    static class Input {
        String originalUrl;
    }


}
