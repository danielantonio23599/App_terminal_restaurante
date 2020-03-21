package com.daniel.app_terminal_restaurante.sync;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SyncDefaut {
    public static final Retrofit RETROFIT_RESTAURANTE = new Retrofit.Builder().
            baseUrl("http://192.168.0.4:8089/RestauranteServer/").
            addConverterFactory(GsonConverterFactory.create()).
            build();
}
