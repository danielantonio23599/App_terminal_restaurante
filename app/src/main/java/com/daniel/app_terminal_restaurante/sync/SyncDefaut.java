package com.daniel.app_terminal_restaurante.sync;

import android.content.Context;

import com.daniel.app_terminal_restaurante.modelo.persistencia.BdServidor;

import okhttp3.HttpUrl;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SyncDefaut {
    private static String ip = "192.168.0.4";
    private String url;

    public static String getUrl(Context c) {
        BdServidor bd = new BdServidor(c);
        String ip = bd.listar().getIp();
        if (!ip.equals("")) {
            return "http://" + ip + ":8089/RestauranteServer/";
        } else {
            return "http://" + "192.168.0.4" + ":8089/RestauranteServer/";
        }
    }


    public static final Retrofit RETROFIT_RESTAURANTE(Context c) {
        return new Retrofit.Builder().
                baseUrl(getUrl(c)).
                addConverterFactory(GsonConverterFactory.create()).
                build();
    }
}
