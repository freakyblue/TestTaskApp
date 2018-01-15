package de.jonny_seitz.simplelibrary;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Book book = (Book) getIntent().getSerializableExtra("BOOK");
        ((TextView) findViewById(R.id.title)).setText(book.getTitle());
        ((TextView) findViewById(R.id.author)).setText(book.getAuthor());
        ((TextView) findViewById(R.id.genre)).setText(book.getGenre());
        ((TextView) findViewById(R.id.description)).setText(book.getDescription());
    }

}
