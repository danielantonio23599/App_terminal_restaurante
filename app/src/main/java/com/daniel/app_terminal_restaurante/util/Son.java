package com.daniel.app_terminal_restaurante.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;

import com.daniel.app_terminal_restaurante.R;

public class Son {
    public static void start(Context context) {
        MediaPlayer mp = MediaPlayer.create(context, R.raw.bell);
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {

                mp.release();
            }

        });
        mp.start();
    }
}
