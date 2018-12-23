package com.leomaya.tinyurl.controller;

import com.leomaya.tinyurl.controller.UrlController;
import com.leomaya.tinyurl.model.Url;
import com.leomaya.tinyurl.repository.UrlRepository;
import net.minidev.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UrlControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UrlRepository urlRepository;

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void should_not_create_when_empty_input() throws Exception {

        mvc.perform(
            put("/url")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(status().is4xxClientError());

    }

    @Test
    public void should_create_with_valid_input() throws Exception {

        JSONObject input = new JSONObject();
        input.put("originalUrl", "http://google.com");
        mvc.perform(
            put("/url")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
            .content(input.toString()))
            .andExpect(status().isOk());

    }

    @Test
    public void should_not_create_with_duplicate_url() throws Exception {


        JSONObject input = new JSONObject();
        input.put("originalUrl", "http://google.com");

        when(urlRepository.findByOriginalUrl(any())).thenReturn(Optional.empty());

        when(urlRepository.save(any())).thenReturn(
            Url.builder()
                .createdAt(new Date())
                .originalUrl(input.getAsString("originalUrl"))
                .tinyUrl("tinyurl")
                .build()
        );

        mvc.perform(
            put("/url")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(input.toString()))
            .andExpect(status().isOk());

        when(urlRepository.findByOriginalUrl(any())).thenReturn(Optional.of(Url.builder().originalUrl("test").build()));

        JSONObject input2 = new JSONObject();
        input2.put("originalUrl", "http://google.com");
        mvc.perform(
            put("/url")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(input2.toString()))
            .andExpect(status().isConflict());

    }

    @Test
    public void should_redirect_tinyurl_to_originalurl() throws Exception {
        when(urlRepository.findByTinyUrl(any())).thenReturn(Optional.of(Url.builder().originalUrl("http://google.com").build()));
        mvc.perform(
            get("/tInYUrL"))
            .andExpect(redirectedUrl("http://google.com"));
    }

    @Test
    public void should_not_create_invalid_url() throws Exception {
        JSONObject input = new JSONObject();
        input.put("originalUrl", "invalid://google.com");
        mvc.perform(
            put("/url")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(input.toString()))
            .andExpect(status().isBadRequest());

    }
}
