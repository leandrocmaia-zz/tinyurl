package com.leomaya.tinyurl.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class UrlValidator {

    public static final UrlValidator INSTANCE = new UrlValidator();

    private UrlValidator() {}

    private static final String URL_REGEX = "^(http:\\/\\/www\\.|https:\\/\\/www\\.|http:\\/\\/|https:\\/\\/)?[a-z0-9]+([\\-\\.]{1}[a-z0-9]+)*\\.[a-z]{2,5}(:[0-9]{1,5})?(\\/.*)?$";

    public boolean validate(String url) {
        Matcher m = Pattern.compile(URL_REGEX).matcher(url);
        return m.matches();

    }
}