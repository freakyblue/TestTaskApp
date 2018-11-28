package de.jonny_seitz.simplelibrary.Connection;

import java.util.List;

import de.jonny_seitz.simplelibrary.Model.Book;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

public interface ApiConnection {

    @GET("book/{id}")
    Observable<List<Book>> getData(@Path("id") String barcode);

    @GET("t.json")
    Observable<List<Book>> getDef();

}
