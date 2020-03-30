package com.daniel.app_terminal_restaurante.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.daniel.app_terminal_restaurante.R;
import com.daniel.app_terminal_restaurante.adapter.AdapterMesa;
import com.daniel.app_terminal_restaurante.adapter.AdapterPedidos;
import com.daniel.app_terminal_restaurante.adapter.holder.Mesa;
import com.daniel.app_terminal_restaurante.adapter.holder.Pedido;
import com.daniel.app_terminal_restaurante.modelo.beans.PreferencesSettings;
import com.daniel.app_terminal_restaurante.modelo.beans.SharedPreferencesEmpresa;
import com.daniel.app_terminal_restaurante.sync.RestauranteAPI;
import com.daniel.app_terminal_restaurante.sync.SyncDefaut;
import com.daniel.app_terminal_restaurante.util.Son;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MesasFragment extends Fragment implements AdapterView.OnItemClickListener {
    private GridView listView;
    private AlertDialog alerta;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmente_mesas, container, false);
        listView = (GridView) view.findViewById(R.id.lvMesas);
        buscarMesas();
        return view;
    }

    private void mostraDialog() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final LayoutInflater li = getLayoutInflater();
                //inflamos o layout alerta.xml na view
                View view = li.inflate(R.layout.alert_progress, null);
                TextView tvDesc = (TextView) view.findViewById(R.id.tvDesc);    //definimos para o botão do layout um clickListener
                tvDesc.setText("Buscando pedios Pendentes...");
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Aguarde...");
                builder.setView(view);
                builder.setCancelable(false);
                alerta = builder.create();
                alerta.show();
            }
        });

    }

    private void escondeDialog() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (alerta.isShowing()) {
                    alerta.dismiss();
                }
            }
        });

    }

    private void buscarMesas() {
        mostraDialog();
        SharedPreferencesEmpresa sh = PreferencesSettings.getAllPreferences(getActivity().getBaseContext());
        RestauranteAPI api = SyncDefaut.RETROFIT_RESTAURANTE(getContext()).create(RestauranteAPI.class);
        final Call<ArrayList<Mesa>> call = api.getMesasAbertas(sh.getEmpEmail(), sh.getEmpSenha());
        call.enqueue(new Callback<ArrayList<Mesa>>() {
            @Override
            public void onResponse(Call<ArrayList<Mesa>> call, Response<ArrayList<Mesa>> response) {
                System.out.println(response.isSuccessful());
                if (response.isSuccessful()) {
                    String auth = response.headers().get("auth");
                    if (auth.equals("1")) {
                        Log.i("[IFMG]", "login correto");
                        ArrayList<Mesa> u = response.body();
                        atualizaTabela(u);
                    } else {
                        Log.i("[IFMG]", "login incorreto");
                        escondeDialog();
                        // senha ou usuario incorreto

                    }
                } else {
                    Log.i("[IFMG]", "servidor fora do ar");
                    escondeDialog();

                    //servidor fora do ar
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Mesa>> call, Throwable t) {
                Log.i("[IFMG]", "servidor com erro " + t.getMessage());
                escondeDialog();
            }
        });
    }


    public void atualizaTabela(ArrayList<Mesa> mesas) {
        Log.i("[IFMG]", "mesas : " + mesas.size());
        AdapterMesa s = new AdapterMesa(getActivity());
        if (mesas.size() > 0) {
            s.setLin(mesas);
            listView.setAdapter(s);
            listView.setOnItemClickListener(MesasFragment.this);
        } else {
            s.setLin(mesas);
            listView.setAdapter(s);
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getActivity(), "nenhuma mesa nova!! ", Toast.LENGTH_SHORT);
                }
            });

        }
        escondeDialog();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
