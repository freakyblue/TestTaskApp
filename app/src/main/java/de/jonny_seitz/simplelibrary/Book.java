package de.jonny_seitz.simplelibrary;

import java.io.Serializable;

/**
 * Created by Jonny on 15/01/18.
 */

public class Book implements Serializable {

    private int id;
    private String title;
    private String author;
    private String genre;
    private String description;

    public Book(int id, String title, String author, String genre, String description) {
        if (id <= 0)
            throw new IllegalArgumentException("id has to be positive!");
        if (title == null)
            throw new NullPointerException("title can't be null!");
        if (title.equals(""))
            throw new IllegalArgumentException("title can't be empty!");
        if (author == null)
            throw new NullPointerException("author can't be null!");
        if (author.equals(""))
            throw new IllegalArgumentException("author can't be empty!");
        if (genre == null)
            throw new NullPointerException("genre can't be null!");
        if (genre.equals(""))
            throw new IllegalArgumentException("genre can't be empty!");
        if (description == null)
            throw new NullPointerException("description can't be null!");
        this.id = id;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.description = description;
    }

    public int getId() {
        return id;
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
