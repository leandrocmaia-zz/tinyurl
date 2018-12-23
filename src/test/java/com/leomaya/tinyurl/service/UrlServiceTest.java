package com.leomaya.tinyurl.service;

import com.leomaya.tinyurl.exception.UrlAlreadyExistsException;
import com.leomaya.tinyurl.model.Url;
import com.leomaya.tinyurl.repository.UrlRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Date;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UrlServiceTest {

    UrlService urlService;

    @Mock
    UrlRepository urlRepository;


    @Before
    public void setUp() {
        urlService = new UrlService(urlRepository);
    }


    @Test
    public void test_tinyurl_creation() throws UrlAlreadyExistsException {
        String originalUrl = "http://google.com";
        Url res = createUrl(originalUrl);
        assertEquals(originalUrl, res.getOriginalUrl());
        assertTrue(res.getTinyUrl() != null);

    }

    private Url createUrl(String originalUrl) throws UrlAlreadyExistsException {
        when(urlRepository.findByOriginalUrl(any())).thenReturn(Optional.empty());
        when(urlRepository.save(any())).thenReturn(
            Url.builder()
                .createdAt(new Date())
                .originalUrl(originalUrl)
                .tinyUrl("tinyurl")
                .build()
        );

        return urlService.insert(originalUrl);
    }

    @Test
    public void test_tinyurl_find() throws UrlAlreadyExistsException {
        String originalUrl = "http://google.com";
        createUrl(originalUrl);

        when(urlService.find(originalUrl)).thenReturn(
            Optional.of(
                Url.builder()
                .createdAt(new Date())
                .originalUrl(originalUrl)
                .tinyUrl("tinyurl")
                .build()
            )
        );

        Optional<Url> newlyCreatedUrl = urlService.find(originalUrl);
        assertTrue(newlyCreatedUrl.isPresent());
        newlyCreatedUrl.ifPresent(u -> assertEquals(originalUrl, u.getOriginalUrl()));
    }
}
