package com.shortener.url.service;

import com.shortener.url.modal.Url;
import com.shortener.url.repository.UrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.shortener.url.logic.GenerateShortUrl.getShortUrl;
import static com.shortener.url.logic.GenerateShortUrl.isUrlValid;

import java.time.LocalDate;
import java.util.List;


@Service
public class UrlService {

    private final int EXP_DAYS_VALUE = 100;
    @Autowired
    private UrlRepository urlRepository;

    public void deleteById(int id) {
        urlRepository.deleteById(id);
    }

    public Url updateTotalVisits(int id) {
        Url currentUrl = urlRepository.findById(id);
        return updateVisitsOfUrl(currentUrl);
    }

    public Url updateVisitsOfUrl(Url urlToUpdate) {
        urlToUpdate.setVisits(urlToUpdate.getVisits() + 1);
        return urlRepository.save(urlToUpdate);
    }

    public List<Url> getAllUrls() {
        return urlRepository.findAll();
    }

    /**
     * Method to retrieve the source URL from target shortened URL
     * This method also updates the visits for the given URL
     */
    public Url getUrlByShortUrl(String id) {
        Url urlToNavigate = urlRepository.findUrlByShorturl(id);
        if (urlToNavigate != null) {
            if (urlToNavigate.getExpiryAt().isBefore(LocalDate.now().atStartOfDay())) {
                return null;
            }
            this.updateTotalVisits(urlToNavigate.getId());
        }
        return urlToNavigate;
    }

    /**
     * Method to validate the given URL and to generate the shortened URL for the given URL
     */
    public Url generateShortUrl(String url) {
        if(! isUrlValid(url)) {
            System.out.println("URL is not valid");
            return null;
        }

        Url urlObject = new Url();
        urlObject.setOriginalurl(url);
        urlObject.setShorturl(getShortUrl(url));
        urlObject.setExpiryAt(LocalDate.now().plusDays(EXP_DAYS_VALUE + 1).atStartOfDay());

        return urlRepository.save(urlObject);
    }
}
