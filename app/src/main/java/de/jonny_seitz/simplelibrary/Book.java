package de.jonny_seitz.simplelibrary;

/**
 * Created by Jonny on 15/01/18.
 */

public class Book {

    private String title;
    private String author;
    private String genre;
    private String description;

    public Book(String title, String author, String genre, String description) {
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getGenre() {
        return genre;
    }

    public String getDescription() {
        return description;
    }
}
