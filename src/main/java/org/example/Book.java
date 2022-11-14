package org.example;

import java.time.LocalDate;

public class Book {
    private String title;
    private String authors;
    private  float average_rating;
    private String language_code;
    private int num_pages;
    private LocalDate publication_date;

    public Book(String title, String authors, float average_rating, String language_code, int num_pages, LocalDate publication_date) {
        this.title = title;
        this.authors = authors;
        this.average_rating = average_rating;
        this.language_code = language_code;
        this.num_pages = num_pages;
        this.publication_date = publication_date;
    }


    public String getTitle() {
        return title;
    }

    public String getAuthors() {
        return authors;
    }

    public float getAverage_rating() {
        return average_rating;
    }

    public String getLanguage_code() {
        return language_code;
    }

    public int getNum_pages() {
        return num_pages;
    }

    public LocalDate getPublication_date() {
        return publication_date;
    }

    public Book() {
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public void setAverage_rating(float average_rating) {
        this.average_rating = average_rating;
    }

    public void setLanguage_code(String language_code) {
        this.language_code = language_code;
    }

    public void setNum_pages(int num_pages) {
        this.num_pages = num_pages;
    }

    public void setPublication_date(LocalDate publication_date) {
        this.publication_date = publication_date;
    }

    @Override
    public String toString() {
        return "Book{" +
                "title='" + title + '\'' +
                ", authors='" + authors + '\'' +
                ", average_rating=" + average_rating +
                ", language_code='" + language_code + '\'' +
                ", num_pages=" + num_pages +
                ", publication_date=" + publication_date +
                '}';
    }
}
