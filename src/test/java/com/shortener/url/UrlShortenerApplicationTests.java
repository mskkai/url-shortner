package com.shortener.url;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.shortener.url.modal.Url;
import com.shortener.url.repository.UrlRepository;
import com.shortener.url.service.UrlService;

@SpringBootTest
class UrlShortenerApplicationTests {

	@Autowired
	private UrlService urlService;

	@MockBean
	private UrlRepository urlRepository;

	@BeforeEach
	void setUp() {
		Url url = new Url();
		url.setId(1);
		url.setUserName("testuser");
		url.setOriginalurl("https://www.example.com");
		url.setShorturl("abc123");
		url.setVisits(0);
		url.setExpiryAt(LocalDateTime.now().plusDays(100));
		Mockito.when(urlRepository.findById(1)).thenReturn(url);
		Mockito.when(urlRepository.findUrlByShorturl("abc123")).thenReturn(url);
		Mockito.when(urlRepository.save(Mockito.any(Url.class))).thenReturn(url);
		Mockito.when(urlRepository.findByUserName("testuser")).thenReturn(Collections.singletonList(url));
	}

	@Test
	void testDeleteById() {
		urlService.deleteById(1);
		Mockito.verify(urlRepository, Mockito.times(1)).deleteById(1);
	}

	@Test
	void testUpdateTotalVisits() {
		Url url = urlService.updateTotalVisits(1);
		Assertions.assertEquals(1, url.getVisits());
		Mockito.verify(urlRepository, Mockito.times(1)).findById(1);
		Mockito.verify(urlRepository, Mockito.times(1)).save(Mockito.any(Url.class));
	}

	@Test
	void testUpdateVisitsOfUrl() {
		Url url = new Url();
		url.setVisits(0);
		Url updatedUrl = urlService.updateVisitsOfUrl(url);
		Assertions.assertEquals(0, updatedUrl.getVisits());
		Mockito.verify(urlRepository, Mockito.times(1)).save(Mockito.any(Url.class));
	}

	@Test
	void testGetAllUrls() {
		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		List<Url> urls = urlService.getAllUrls();
		Assertions.assertEquals(0, urls.size());
		Mockito.verify(urlRepository, Mockito.times(1)).findByUserName(null);
	}

	@Test
	void testGetUrlByShortUrl() {
		Url url = urlService.getUrlByShortUrl("abc123");
		Assertions.assertNotNull(url);
		Assertions.assertEquals(1, url.getVisits());
		Mockito.verify(urlRepository, Mockito.times(1)).findUrlByShorturl(Mockito.anyString());
		Mockito.verify(urlRepository, Mockito.times(1)).save(Mockito.any(Url.class));
	}

	@Test
	void testGenerateShortUrl() {
		Url url = urlService.generateShortUrl("https://www.example.com");
		Assertions.assertNotNull(url);
		Assertions.assertEquals("testuser", url.getUserName());
		Assertions.assertEquals("https://www.example.com", url.getOriginalurl());
		Assertions.assertNotNull(url.getShorturl());
		Assertions.assertEquals(0, url.getVisits());
		Assertions.assertTrue(url.getExpiryAt().isAfter(LocalDateTime.now()));
		Mockito.verify(urlRepository, Mockito.times(1)).save(Mockito.any(Url.class));
	}

}
