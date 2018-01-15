package de.jonny_seitz.simplelibrary;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Book> books;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO add new book
            }
        });

        books = new ArrayList<>();
        books.add(new Book(
                "Bible",
                "Mark, Luke and many more...",
                "Religion",
                "Most sold book on earth and holy book of the Christians."
        ));
        books.add(new Book(
                "Fire & Fury",
                "Michael Wolff",
                "Biography, Politics",
                "Controversal book about Donald Trump and his way to become president."
        ));
        books.add(new Book(
                "Harry Potter",
                "J. K. Rowling",
                "Fantasy",
                "Novel about a rowing wizard, going to school and nearly gets killed every year"
        ));


        BookAdapter bookAdapter = new BookAdapter(this, books);
        ListView listView = findViewById(R.id.book_list);
        listView.setAdapter(bookAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
