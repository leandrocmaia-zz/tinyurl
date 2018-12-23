package com.leomaya.tinyurl.repository;

import com.leomaya.tinyurl.model.Url;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UrlRepository extends JpaRepository<Url, Long> {

    Optional<Url> findByOriginalUrl(String originalUrl);

    Optional<Url> findByTinyUrl(String tinyUrl);

    @Query(value = "select next_val as id_val from url_seq", nativeQuery = true)
    Long getNextSeqId();

}
