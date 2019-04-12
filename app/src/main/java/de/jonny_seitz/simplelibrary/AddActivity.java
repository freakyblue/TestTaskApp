package de.jonny_seitz.simplelibrary;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.jonny_seitz.simplelibrary.Model.Book;
import io.realm.Realm;

public class AddActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 1;
    private String cover;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        getSupportActionBar().setTitle(R.string.add_book);
        cover = null;

        requestPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, 0);
        requestPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE, 1);
        requestPermission(this, Manifest.permission.CAMERA, 0);
    }

    static void requestPermission(Activity thisActivity, String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(thisActivity, permission) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(thisActivity, new String[]{permission}, requestCode);
        }
    }

    public void addCover(View view) {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            File image = null;
            try {
                image = File.createTempFile(
                        new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()),
                        ".jpg",
                        getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                );
                // Save a file: path for use with ACTION_VIEW intents
                cover = image.getAbsolutePath();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            if (image != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "de.jonny_seitz.simplelibrary.fileprovider",
                        image);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(cameraIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
            bitmapOptions.inSampleSize = 10;
            ((ImageView) findViewById(R.id.cover))
                    .setImageBitmap(BitmapFactory.decodeFile(cover, bitmapOptions));
        }
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

        //System.out.println("cover: "+cover);//TODO remove
        try {
            File file = new File(cover);
            if (!file.exists()) cover = "";
        }
        catch (Exception e) {}

        Realm realm = Realm.getDefaultInstance();
        int id = realm.where(Book.class).max("id").intValue()+1;
        realm.beginTransaction();
        realm.copyToRealm(new Book(id, title, author, genre, description, cover));
        realm.commitTransaction();
        realm.close();
        startActivity(new Intent(AddActivity.this, MainActivity.class));
    }

}
