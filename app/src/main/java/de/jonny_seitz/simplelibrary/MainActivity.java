package de.jonny_seitz.simplelibrary;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

import de.jonny_seitz.simplelibrary.Connection.ApiConnection;
import de.jonny_seitz.simplelibrary.Connection.ApiGenerator;
import de.jonny_seitz.simplelibrary.Model.Book;
import io.realm.Realm;
import io.realm.RealmResults;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private BookAdapter adapter;
    private ArrayList<Book> books;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        SharedPreferences sharedPreferences =
                getSharedPreferences("warehouse", Context.MODE_PRIVATE);
        getSupportActionBar().setTitle(sharedPreferences.getString("library_name", ""));

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

        recyclerView = (RecyclerView) findViewById(R.id.book_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BookAdapter(this, books);
        recyclerView.setAdapter(adapter);

        ItemTouchHelper helper = new ItemTouchHelper(new SwipeHelper(adapter, recyclerView));
        helper.attachToRecyclerView(recyclerView);


        // load data from server
        ApiGenerator.create(ApiConnection.class)
                .getDef()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            System.out.println("response\n"+response);
                            books.addAll(response);
                            realm.beginTransaction();
                            realm.copyToRealm(books);
                            realm.commitTransaction();
                            adapter.notifyDataSetChanged();
                        },
                        error -> System.out.println("error\n"+error)
                );


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

    private void getBooks() throws IOException, XmlPullParserException {
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
        //copy book items from dbBook to books
        books.addAll(dbBooks);
    }

}
