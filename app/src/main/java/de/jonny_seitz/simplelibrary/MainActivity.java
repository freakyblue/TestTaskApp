package de.jonny_seitz.simplelibrary;

import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
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
                startActivity(new Intent(getBaseContext(), AddActivity.class));
            }
        });

        books = new ArrayList<>();
        try {
            getBooks();
        }
        catch (Exception e){}

        BookAdapter bookAdapter = new BookAdapter(this, books);
        ListView listView = findViewById(R.id.book_list);
        listView.setAdapter(bookAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(getBaseContext(), DetailsActivity.class);
                intent.putExtra("BOOK", books.get(position));
                startActivity(intent);
            }
        });
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

    private void getBooks() throws IOException, XmlPullParserException {
        if (Warehouse.get().getBooks().isEmpty()) {
            String title="", author="", genre="", description="";
            XmlResourceParser parser = getResources().getXml(R.xml.books);
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    switch (parser.getName()) {
                        case "title":
                            parser.next();
                            title = parser.getText();
                            break;
                        case "author":
                            parser.next();
                            author = parser.getText();
                            break;
                        case "genre":
                            parser.next();
                            genre = parser.getText();
                            break;
                        case "description":
                            parser.next();
                            description = parser.getText();
                            break;
                        default:
                            break;
                    }
                }
                if (eventType == XmlPullParser.END_TAG) {
                    if (parser.getName().equals("book")) {
                        books.add(new Book(title, author, genre, description));
                    }
                }
                eventType = parser.next();
            }
            parser.close();
            Warehouse.get().setBooks(books);
        }

        Book book = (Book) getIntent().getSerializableExtra("ADDBOOK");
        if (book != null) Warehouse.get().addBook(book);

        books = Warehouse.get().getBooks();
    }

}
