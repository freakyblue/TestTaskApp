package de.jonny_seitz.simplelibrary;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
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
import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.Realm;

public class AddActivity extends AppCompatActivity {

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
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
                .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        cover = new SimpleDateFormat("yyyMMdd_HHmmss").format(new Date())+".jpg";
        File file = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                cover
        );
        String authority = getBaseContext().getApplicationContext().getPackageName()+
                ".de.jonny_seitz.simplelibrary.provider";
        cameraIntent.putExtra(
                MediaStore.EXTRA_OUTPUT,
                FileProvider.getUriForFile(getBaseContext(), authority, file)
        );
        startActivityForResult(cameraIntent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((resultCode == RESULT_OK) && (requestCode == 0)) {
            ((ImageView) findViewById(R.id.cover))
                    .setImageBitmap((Bitmap) data.getExtras().get("data"));
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

        Realm realm = Realm.getDefaultInstance();
        int id = ((int) (long) realm.where(Book.class).max("id"))+1;
        realm.beginTransaction();
        realm.copyToRealm(new Book(id, title, author, genre, description, cover));
        realm.commitTransaction();
        realm.close();
        startActivity(new Intent(AddActivity.this, MainActivity.class));
    }

}
