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
        //TODO check for correct input
        String title = ((EditText) findViewById(R.id.title)).getText().toString();
        String author = ((EditText) findViewById(R.id.author)).getText().toString();
        String genre = ((EditText) findViewById(R.id.genre)).getText().toString();
        String description = ((EditText) findViewById(R.id.description)).getText().toString();
        Book book = new Book(title, author, genre, description);
        Intent intent = new Intent(AddActivity.this, MainActivity.class);
        intent.putExtra("ADDBOOK", book);
        startActivity(intent);
    }
}
