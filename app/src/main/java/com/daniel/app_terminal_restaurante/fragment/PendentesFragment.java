package com.daniel.app_terminal_restaurante.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;


import com.daniel.app_terminal_restaurante.LoginActivity;
import com.daniel.app_terminal_restaurante.PedidosActivity;
import com.daniel.app_terminal_restaurante.R;
import com.daniel.app_terminal_restaurante.adapter.AdapterPedidos;
import com.daniel.app_terminal_restaurante.adapter.holder.Pedido;
import com.daniel.app_terminal_restaurante.modelo.beans.PreferencesSettings;
import com.daniel.app_terminal_restaurante.modelo.beans.SharedPreferencesEmpresa;
import com.daniel.app_terminal_restaurante.sync.RestauranteAPI;
import com.daniel.app_terminal_restaurante.sync.SyncDefaut;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PendentesFragment extends Fragment implements AdapterView.OnItemClickListener {
    private ArrayList<Pedido> pp;
    private AlertDialog alerta;
    private ListView listView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pendentes, container, false);
        listView = (ListView) view.findViewById(R.id.lvPendentes);
        atualizaPedidos();
        return view;
    }

    private void atualizaPedidos() {
        Log.i("[IFMG]", "faz login");
        mostraDialog();
        RestauranteAPI api = SyncDefaut.RETROFIT_RESTAURANTE.create(RestauranteAPI.class);
        SharedPreferencesEmpresa s = PreferencesSettings.getAllPreferences(getActivity().getBaseContext());
        final Call<ArrayList<Pedido>> call = api.listarPedidos(s.getEmpEmail(), s.getEmpSenha() + "");

        call.enqueue(new Callback<ArrayList<Pedido>>() {
            @Override
            public void onResponse(Call<ArrayList<Pedido>> call, Response<ArrayList<Pedido>> response) {
                if (response.code() == 200) {
                    String auth = response.headers().get("auth");

                    if (auth.equals("1")) {
                        escondeDialog();
                        ArrayList<Pedido> u = response.body();
                        atualizaTabela(u);
                    } else {
                        escondeDialog();
                        Toast.makeText(getActivity().getBaseContext(), "Nome de usuário ou senha incorretos", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    escondeDialog();
                    Toast.makeText(getActivity().getBaseContext(), "Erro ao fazer login, erro servidor", Toast.LENGTH_SHORT).show();
                    Log.i("[IFMG]", "t1: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Pedido>> call, Throwable t) {
                escondeDialog();
                Toast.makeText(getActivity().getBaseContext(), "Erro ao fazer login, falhaaaaa", Toast.LENGTH_SHORT).show();
                Log.i("[IFMG]", "faz login");
                Log.i("Teste", "t2: " + t.getMessage());
                //mudaActivity(MainActivity.class);
            }
        });
    }

    private void mostraDialog() {
        final LayoutInflater li = getLayoutInflater();
        //inflamos o layout alerta.xml na view
        View view = li.inflate(R.layout.alert_progress, null);
        TextView tvDesc = (TextView) view.findViewById(R.id.tvDesc);    //definimos para o botão do layout um clickListener
        tvDesc.setText("Fazendo Login...");
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Aguarde...");
        builder.setView(view);
        builder.setCancelable(false);
        alerta = builder.create();
        alerta.show();
    }

    private void escondeDialog() {
        if (alerta.isShowing()) {
            alerta.dismiss();
        }
    }

    public void atualizaTabela(ArrayList<Pedido> pedidos) {
        Log.i("[IFMG]", "pedidos: " + pedidos.size());
        AdapterPedidos s = new AdapterPedidos(getActivity());
        if (pedidos.size() > 0) {
            s.setLin(pedidos);
            listView.setAdapter(s);
            listView.setOnItemClickListener(PendentesFragment.this);
        } else {
            s.setLin(pedidos);
            listView.setAdapter(s);
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getActivity(), "nenhum pedio Novo!! ", Toast.LENGTH_SHORT);
                }
            });

        }


    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
