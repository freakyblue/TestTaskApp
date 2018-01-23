package de.jonny_seitz.simplelibrary;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import io.realm.Realm;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Realm realm = Realm.getDefaultInstance();
        Book book = realm.where(Book.class)
                .equalTo("id", getIntent().getIntExtra("BOOK-ID", 42))
                .findFirst();
        getSupportActionBar().setTitle(book.getTitle());
        ((TextView) findViewById(R.id.title)).setText(book.getTitle());
        ((TextView) findViewById(R.id.author)).setText(book.getAuthor());
        ((TextView) findViewById(R.id.genre)).setText(book.getGenre());
        ((TextView) findViewById(R.id.description)).setText(book.getDescription());
    }

}
