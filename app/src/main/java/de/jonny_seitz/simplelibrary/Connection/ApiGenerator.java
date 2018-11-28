package de.jonny_seitz.simplelibrary.Connection;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiGenerator {

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl("https://jonny-seitz.de/test/")
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create());

    private static Retrofit retrofit = builder.build();

    public static <S> S create(Class<S> serviceClass) {
        return retrofit.create(serviceClass);
    }

}
