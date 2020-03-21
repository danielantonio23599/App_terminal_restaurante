package com.daniel.app_terminal_restaurante.util;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.zip.DataFormatException;

public class Time {
    public static String getTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        Date data = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(data);
        Date data_atual = cal.getTime();
        String hora_atual = dateFormat.format(data_atual);
        Log.i("hora_atual", hora_atual); // Esse é o que você que
        return hora_atual;
    }
    public static String subtraiHoras(String horaIni){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Date dataIni = null;
        Date dataFim = null;
        try {
            dataIni = sdf.parse(horaIni);
            dataFim = sdf.parse(getTime());
        } catch (Exception e) {

        }
        Date resultado = new Date(dataFim.getTime() - dataIni.getTime());
        return sdf.format(resultado);
    }
    public static String getCronometro(String horaInicial){

       return "" ;
    }
}
