package com.leomaya.tinyurl.util;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.LongStream;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class Base62ConverterTest {

    private static final Logger log = LoggerFactory.getLogger(Base62ConverterTest.class);


    @Test
    public void should_convert_base10_to_base62() {
        assertThat(Base62Converter.INSTANCE.createUniqueID(1l), equalTo("b"));
        assertThat(Base62Converter.INSTANCE.createUniqueID(123l), equalTo("b9"));
        assertThat(Base62Converter.INSTANCE.createUniqueID(9223372036854775807l), equalTo("k9viXaIfiWh"));
    }

    @Test
    public void should_not_overlap() {
        ConcurrentHashMap<Long, String> overlap = new ConcurrentHashMap();

        long n = 1000000;

        LongStream.range(1, n).parallel().forEach(id -> {
            String base = Base62Converter.INSTANCE.createUniqueID(id);
            overlap.putIfAbsent(id, base);
        });

        int total = overlap.size();
        int distinct = overlap.entrySet()
                .stream()
                .map(e -> e.getValue())
                .distinct()
                .collect(toList())
                .size();

        log.info("Total: {} - Overlaped: {}", total, total - distinct);

        assertThat(total, equalTo(distinct));
    }
}
