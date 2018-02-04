package de.jonny_seitz.simplelibrary;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.transition.Slide;
import android.view.Gravity;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

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
        if (book.getCover() != null) {
            File file = new File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                    book.getCover()
            );
            if(file.exists()) {
                Bitmap bitmap = BitmapFactory
                        .decodeFile(file.getAbsolutePath(), new BitmapFactory.Options());
                bitmap = Bitmap.createScaledBitmap(
                        bitmap,
                        bitmap.getWidth()/(bitmap.getHeight()/400),
                        400,
                        true
                );
                ((ImageView) findViewById(R.id.cover)).setImageBitmap(bitmap);
            }
        }
        ((TextView) findViewById(R.id.title)).setText(book.getTitle());
        ((TextView) findViewById(R.id.author)).setText(book.getAuthor());
        ((TextView) findViewById(R.id.genre)).setText(book.getGenre());
        ((TextView) findViewById(R.id.description)).setText(book.getDescription());

        //transition
        Slide slide = new Slide(Gravity.BOTTOM);
        slide.setInterpolator(AnimationUtils.loadInterpolator(
                this,
                android.R.interpolator.linear_out_slow_in
        ));
        getWindow().setEnterTransition(slide);


    }

}
