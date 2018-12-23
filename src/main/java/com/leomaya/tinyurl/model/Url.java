package com.leomaya.tinyurl.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@SequenceGenerator(name="url_seq", initialValue=1)
public class Url {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "url_seq")
    Long id;

    @Column(unique = true)
    String originalUrl;

    String tinyUrl;
    Date createdAt;
}
