package com.example.library;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Book implements Serializable {
    @SerializedName("title")
    private String title;
    @SerializedName("author_name")
    private List<String> authors;
    @SerializedName("cover_i")
    private String cover;
    @SerializedName("number_of_pages_median")
    private String numberOfPages;
    @SerializedName("publish_date")
    private List<String> publishDate;
    @SerializedName("isbn")
    private List<String> isbn;
    @SerializedName("edition_count")
    private int editionCount;


    public String getTitle(){return this.title;}
    public void setTitle(String title){this.title = title;}

    public List<String> getAuthors(){return this.authors;}
    public void setAuthors(List<String> authors){this.authors = authors;}

    public String getCover(){return this.cover;}
    public void setCover(String cover){this.cover = cover;}

    public String getNumberOfPages(){return this.numberOfPages;}
    public void setNumberOfPages(String numberOfPages){this.numberOfPages = numberOfPages;}

    public List<String> getPublishDate(){return this.publishDate;}
    public void setPublishDate(List<String> publishDate){this.publishDate = publishDate;}

    public List<String> getIsbn(){return this.isbn;}
    public void setIsbn(List<String> isbn){this.isbn = isbn;}

    public int getEditionCount(){return this.editionCount;}
    public void setEditionCount(int editionCount){this.editionCount = editionCount;}
}
