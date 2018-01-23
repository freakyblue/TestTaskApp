package de.jonny_seitz.simplelibrary;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import io.realm.Realm;

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

        Realm realm = Realm.getDefaultInstance();
        int id = ((int) (long) realm.where(Book.class).max("id"))+1;
        realm.beginTransaction();
        realm.copyToRealm(new Book(id, title, author, genre, description));
        realm.commitTransaction();
        realm.close();
        startActivity(new Intent(AddActivity.this, MainActivity.class));
    }
}
