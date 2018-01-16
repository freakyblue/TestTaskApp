package de.jonny_seitz.simplelibrary;

import java.util.ArrayList;

/**
 * Created by Jonny on 16/01/18.
 */

//Singleton
public class Warehouse {

    private static Warehouse instance;
    private ArrayList<Book> books;

    private Warehouse () {
        books = new ArrayList<>();
    }

    public static Warehouse get() {
        if (instance == null) instance = new Warehouse();
        return instance;
    }

    public void setBooks (ArrayList<Book> books) {
        this.books = books;
    }

    public void addBook (Book book) {
        books.add(book);
    }

    public ArrayList<Book> getBooks() {
        return books;
    }
}
