package com.shortener.url.controller;

import com.shortener.url.modal.Url;
import com.shortener.url.service.UrlService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("url/shortner")
@CrossOrigin(origins = "http://localhost:4200")
public class UrlController {

    @Autowired
    private UrlService urlService;
    
    @GetMapping
    public List<Url> getAllUrls() {
        return urlService.getAllUrls();
    }

    @PatchMapping("/{id}")
    public Url updateTotalVisits(@PathVariable int id) {
        return urlService.updateTotalVisits(id);
    }

    @DeleteMapping("/{id}")
    public void deleteUrl(@PathVariable int id) {
        urlService.deleteById(id);
    }

    @PostMapping
    public Url generateShortUrl(@RequestBody String url) {
        return urlService.generateShortUrl(url);
    }

    @GetMapping("/{id}")
    public ModelAndView navigateToOriginUrl(@PathVariable String id) {
        Url urlToNavigate = urlService.getUrlByShortUrl(id);
        if (urlToNavigate == null) {
           return new ModelAndView("view/viewPage").addObject("message", "invalid url");
        }
        return new ModelAndView("redirect:" + urlToNavigate.getOriginalurl());
    }
}
