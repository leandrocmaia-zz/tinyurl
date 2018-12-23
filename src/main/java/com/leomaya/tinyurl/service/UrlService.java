package com.leomaya.tinyurl.service;

import com.leomaya.tinyurl.exception.InvalidUrlException;
import com.leomaya.tinyurl.exception.UrlAlreadyExistsException;
import com.leomaya.tinyurl.model.Url;
import com.leomaya.tinyurl.repository.UrlRepository;
import com.leomaya.tinyurl.util.Base62Converter;
import com.leomaya.tinyurl.util.UrlValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class UrlService {

    UrlRepository urlRepository;


    @Autowired
    public UrlService(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }


    public Url insert(String originalUrl) throws UrlAlreadyExistsException {

        if (!UrlValidator.INSTANCE.validate(originalUrl)) {
            throw new InvalidUrlException();
        }
        urlRepository.findByOriginalUrl(originalUrl).ifPresent(url -> {
            throw new UrlAlreadyExistsException();
        });

        String token = Base62Converter.INSTANCE.createUniqueID(urlRepository.getNextSeqId());

        Url url = Url.builder()
                    .originalUrl(originalUrl)
                    .tinyUrl(token)
                    .createdAt(new Date())
                    .build();

        return urlRepository.save(url);
    }

    public Optional<Url> find(String tinyUrl) {
        return urlRepository.findByTinyUrl(tinyUrl);
    }


}
