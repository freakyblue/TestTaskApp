package de.jonny_seitz.simplelibrary;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Jonny on 17/01/18.
 */


public class BookTest {

    private Book book;

    @org.junit.Before
    public void setUp() {
        book = new Book(
                "Bible",
                "Mark, Luke and many more...",
                "Religion",
                "Most sold book on earth and holy book of the Christians."
        );
    }

    @Test
    public void testBook() {
        book = new Book(
                "Bible",
                "Mark, Luke and many more...",
                "Religion",
                "Most sold book on earth and holy book of the Christians."
        );
    }

    @Test(expected = NullPointerException.class)
    public void testBookTitleNull() {
        book = new Book(
                null,
                "Mark, Luke and many more...",
                "Religion",
                "Most sold book on earth and holy book of the Christians."
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBookTitleEmpty() {
        book = new Book(
                "",
                "Mark, Luke and many more...",
                "Religion",
                "Most sold book on earth and holy book of the Christians."
        );
    }


    @Test(expected = NullPointerException.class)
    public void testBookAuthorNull() {
        book = new Book(
                "Bible",
                null,
                "Religion",
                "Most sold book on earth and holy book of the Christians."
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBookAuthorEmpty() {
        book = new Book(
                "Bible",
                "",
                "Religion",
                "Most sold book on earth and holy book of the Christians."
        );
    }

    @Test(expected = NullPointerException.class)
    public void testBookGenreNull() {
        book = new Book(
                "Bible",
                "Mark, Luke and many more...",
                null,
                "Most sold book on earth and holy book of the Christians."
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBookGenreEmpty() {
        book = new Book(
                "Bible",
                "Mark, Luke and many more...",
                "",
                "Most sold book on earth and holy book of the Christians."
        );
    }

    @Test(expected = NullPointerException.class)
    public void testBookDescriptionNull() {
        book = new Book(
                "Bible",
                "Mark, Luke and many more...",
                "Religion",
                null
        );
    }

    @Test
    public void testGetTitle()  {
        assertEquals(book.getTitle(), "Bible");
    }

    @Test
    public void testGetAuthor()  {
        assertEquals(book.getAuthor(), "Mark, Luke and many more...");
    }

    @Test
    public void testGetGenre()  {
        assertEquals(book.getGenre(), "Religion");
    }

    @Test
    public void testGetDescription()  {
        assertEquals(
                book.getDescription(),
                "Most sold book on earth and holy book of the Christians."
        );
    }

}
