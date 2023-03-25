package com.shortener.url;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.times;
import static org.hamcrest.Matchers.hasSize;
import com.shortener.url.modal.Url;
import com.shortener.url.service.UrlService;
import org.mockito.Mockito.*;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
class UrlControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UrlService urlService;

    @Test
    @DisplayName("Get all URLs")
    void testGetAllUrls() throws Exception {
        List<Url> urls = new ArrayList<>();
        urls.add(Url.builder().id(1).originalurl("https://www.google.com").build());
        urls.add(Url.builder().id(2).originalurl("https://www.facebook.com").build());

        Mockito.when(urlService.getAllUrls()).thenReturn(urls);

        mockMvc.perform(get("/url/shortner"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].originalurl", is("https://www.google.com")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].originalurl", is("https://www.facebook.com")));
    }

    @Test
    @DisplayName("Update total visits")
    void testUpdateTotalVisits() throws Exception {
        Url url = Url.builder().id(1).originalurl("https://www.google.com").visits(5).build();
        Mockito.when(urlService.updateTotalVisits(1)).thenReturn(url);

        mockMvc.perform(patch("/url/shortner/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.originalurl", is("https://www.google.com")))
                .andExpect(jsonPath("$.visits", is(5)));
    }

    @Test
    @DisplayName("Delete URL by ID")
    void testDeleteUrl() throws Exception {
        mockMvc.perform(delete("/url/shortner/1"))
                .andExpect(status().isOk());
        Mockito.verify(urlService, times(1)).deleteById(1);
    }

    @Test
    @DisplayName("Generate short URL")
    void testGenerateShortUrl() throws Exception {
        Url url = Url.builder().id(1).originalurl("https://www.google.com").build();
        Mockito.when(urlService.generateShortUrl("https://www.google.com")).thenReturn(url);

        mockMvc.perform(post("/url/shortner")
                .content("https://www.google.com")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.originalurl", is("https://www.google.com")));
    }

    @Test
    @DisplayName("Navigate to original URL")
    void testNavigateToOriginUrl() throws Exception {
        Url url = Url.builder().originalurl("https://www.google.com").build();
        Mockito.when(urlService.getUrlByShortUrl("abc123")).thenReturn(url);

        mockMvc.perform(get("/url/shortner/abc123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "https://www.google.com"));
    }

    @Test
    @DisplayName("Invalid short URL")
    void testInvalidShortUrl() throws Exception {
        Mockito.when(urlService.getUrlByShortUrl("invalid_url")).thenReturn(null);

        mockMvc.perform(get("/url/shortner/invalid_url"))
                .andExpect(status().isOk())
                .andExpect(view().name("view/viewPage"))
                .andExpect(model().attribute("message", "invalid url"));
    }
}
