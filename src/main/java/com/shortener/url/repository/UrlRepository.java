package com.shortener.url.repository;

import com.shortener.url.modal.Url;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UrlRepository extends JpaRepository<Url, Integer> {

    Url findUrlByShorturl(String id);

    Url findById(int id);

    void deleteById(int id);

    List<Url> findByUserName(String userName);
}
