package de.jonny_seitz.simplelibrary;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.jonny_seitz.simplelibrary.Connection.ApiConnection;
import de.jonny_seitz.simplelibrary.Connection.ApiGenerator;
import de.jonny_seitz.simplelibrary.Model.Book;
import io.realm.Realm;
import io.realm.RealmResults;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private SwipeRefreshLayout swipeContainer;
    private RecyclerView recyclerView;
    private BookAdapter adapter;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar(findViewById(R.id.toolbar));

        SharedPreferences sharedPreferences =
                getSharedPreferences("warehouse", Context.MODE_PRIVATE);
        getSupportActionBar().setTitle(sharedPreferences.getString("library_name", ""));

        findViewById(R.id.fab).setOnClickListener(view -> startActivity(
                new Intent(getBaseContext(), AddActivity.class)
        ));

        try {
            getDefaultBooks();
        }
        catch (Exception e){}

        recyclerView = findViewById(R.id.book_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BookAdapter(this, getDBBooks());
        recyclerView.setAdapter(adapter);

        ItemTouchHelper helper = new ItemTouchHelper(new SwipeHelper(adapter, recyclerView));
        helper.attachToRecyclerView(recyclerView);

        //refresh swipe
        swipeContainer = findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(() -> {
            loadBooksFromServer();
        });
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright);

        loadBooksFromServer();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void loadBooksFromServer() {
        // load data from server
        ApiGenerator.create(ApiConnection.class)
                .getDef()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            System.out.println("response\n"+response);
                            addOrUpdateBooks(response);
                            adapter.setBooks(getDBBooks());
                            adapter.notifyDataSetChanged();
                            swipeContainer.setRefreshing(false);
                        },
                        error -> {
                            System.out.println("error\n"+error);
                            swipeContainer.setRefreshing(false);
                        }
                );
    }

    /*
    * Adds default books to DB if DB is empty
     */
    private void getDefaultBooks() throws IOException, XmlPullParserException {
        Realm.init(this);
        realm = Realm.getDefaultInstance();
        RealmResults<Book> dbBooks = realm.where(Book.class).findAll();
        if (dbBooks.isEmpty()) {
            int id = 0;
            String title="", author="", genre="", description="";
            XmlResourceParser parser = getResources().getXml(R.xml.books);
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    switch (parser.getName()) {
                        case "id":
                            parser.next();
                            id = Integer.parseInt(parser.getText());
                            break;
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
                        realm.beginTransaction();
                        realm.copyToRealm(new Book(id, title, author, genre, description, null));
                        realm.commitTransaction();
                    }
                }
                eventType = parser.next();
            }
            parser.close();
        }
    }

    private List<Book> getDBBooks() {
        List<Book> books = new ArrayList<>();
        for(Book book : realm.where(Book.class).findAll()) {
            books.add(book);
        }
        return books;
    }

    private void addOrUpdateBooks (List<Book> newBooks) {
        for (Book newBook : newBooks) {
            System.out.println("new book: "+newBook.getTitle());
            //update if book already in DB
            if (realm.where(Book.class).equalTo("id", newBook.getId()).count() == 1) {
                System.out.println("update "+newBook.getTitle());
                realm.beginTransaction();
                Book dbBook = realm.where(Book.class).equalTo("id", newBook.getId()).findFirst();
                dbBook.setTitle(newBook.getTitle());
                dbBook.setAuthor(newBook.getAuthor());
                dbBook.setGenre(newBook.getGenre());
                dbBook.setDescription(newBook.getDescription());
                dbBook.setCover(newBook.getCover());
                realm.commitTransaction();
            }
            else {
                //insert new book
                System.out.println("insert "+newBook.getTitle());
                realm.beginTransaction();
                realm.copyToRealm(newBook);
                realm.commitTransaction();
            }
        }
    }

}
