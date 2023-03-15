package com.shortener.url.logic;

import com.google.common.hash.Hashing;
import org.apache.commons.validator.routines.UrlValidator;

import java.nio.charset.Charset;

public class GenerateShortUrl {

    /**
     * Method to generate the short url using hashing murmur algorithm
     */
    public static String getShortUrl(String url) {

        String shortUrl = Hashing.murmur3_32_fixed().hashString(url, Charset.defaultCharset()).toString();
        return shortUrl;
    }

    /**
     * Method to validate the given url
     *
     * @return  boolean [return description]
     */
    public static boolean isUrlValid(String url) {
        UrlValidator urlValidator = new UrlValidator(
               new String[]{"http","https"}
        );
        boolean result = urlValidator.isValid(url);
        return result;
    }

}
