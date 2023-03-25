package com.shortener.url;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import com.shortener.url.modal.Url;

public class UrlModelTests {

    @Test
    public void testUrlConstructor() {
        Url url = new Url(1, "shorturl", "originalurl", "userName", 0, LocalDateTime.now(), LocalDateTime.now());
        assertNotNull(url);
        assertEquals(1, url.getId());
        assertEquals("shorturl", url.getShorturl());
        assertEquals("originalurl", url.getOriginalurl());
        assertEquals("userName", url.getUserName());
        assertEquals(0, url.getVisits());
        assertNotNull(url.getCreatedAt());
        assertNotNull(url.getExpiryAt());
    }

    @Test
    public void testUrlBuilder() {
        Url url = Url.builder()
                .id(1)
                .shorturl("shorturl")
                .originalurl("originalurl")
                .userName("userName")
                .visits(0)
                .createdAt(LocalDateTime.now())
                .expiryAt(LocalDateTime.now())
                .build();
        assertNotNull(url);
        assertEquals(1, url.getId());
        assertEquals("shorturl", url.getShorturl());
        assertEquals("originalurl", url.getOriginalurl());
        assertEquals("userName", url.getUserName());
        assertEquals(0, url.getVisits());
        assertNotNull(url.getCreatedAt());
        assertNotNull(url.getExpiryAt());
    }

    @Test
    public void testUrlGetterSetter() {
        Url url = new Url();
        url.setId(1);
        url.setShorturl("shorturl");
        url.setOriginalurl("originalurl");
        url.setUserName("userName");
        url.setVisits(0);
        LocalDateTime createdAt = LocalDateTime.now();
        url.setCreatedAt(createdAt);
        LocalDateTime expiryAt = LocalDateTime.now();
        url.setExpiryAt(expiryAt);

        assertEquals(1, url.getId());
        assertEquals("shorturl", url.getShorturl());
        assertEquals("originalurl", url.getOriginalurl());
        assertEquals("userName", url.getUserName());
        assertEquals(0, url.getVisits());
        assertEquals(createdAt, url.getCreatedAt());
        assertEquals(expiryAt, url.getExpiryAt());
    }
}