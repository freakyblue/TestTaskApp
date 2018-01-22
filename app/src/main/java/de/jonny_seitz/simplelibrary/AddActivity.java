package de.jonny_seitz.simplelibrary;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class AddActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        getSupportActionBar().setTitle(R.string.add_book);
    }

    public void addBook(View view) {
        String title = ((EditText) findViewById(R.id.title)).getText().toString();
        if(title.equals("")) {
            ((EditText) findViewById(R.id.title)).setError(getText(R.string.required));
            return;
        }
        String author = ((EditText) findViewById(R.id.author)).getText().toString();
        if(author.equals("")) {
            ((EditText) findViewById(R.id.author)).setError(getText(R.string.required));
            return;
        }
        String genre = ((EditText) findViewById(R.id.genre)).getText().toString();
        if(genre.equals("")) {
            ((EditText) findViewById(R.id.genre)).setError(getText(R.string.required));
            return;
        }
        String description = ((EditText) findViewById(R.id.description)).getText().toString();
        int id = Warehouse.get().newId();
        Book book = new Book(id, title, author, genre, description);
        Intent intent = new Intent(AddActivity.this, MainActivity.class);
        intent.putExtra("ADDBOOK", book);
        startActivity(intent);
    }
}
