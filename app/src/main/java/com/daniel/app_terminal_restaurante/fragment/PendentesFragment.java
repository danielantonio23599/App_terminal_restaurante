package com.daniel.app_terminal_restaurante.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;


import com.daniel.app_terminal_restaurante.LoginActivity;
import com.daniel.app_terminal_restaurante.PedidosActivity;
import com.daniel.app_terminal_restaurante.R;
import com.daniel.app_terminal_restaurante.adapter.AdapterPedidos;
import com.daniel.app_terminal_restaurante.adapter.holder.Pedido;
import com.daniel.app_terminal_restaurante.modelo.beans.PreferencesSettings;
import com.daniel.app_terminal_restaurante.modelo.beans.Servidor;
import com.daniel.app_terminal_restaurante.modelo.beans.SharedPreferencesEmpresa;
import com.daniel.app_terminal_restaurante.modelo.persistencia.BdServidor;
import com.daniel.app_terminal_restaurante.sync.RestauranteAPI;
import com.daniel.app_terminal_restaurante.sync.SyncDefaut;
import com.daniel.app_terminal_restaurante.util.Son;
import com.daniel.app_terminal_restaurante.util.TecladoUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PendentesFragment extends Fragment implements AdapterView.OnItemClickListener {
    private ArrayList<Pedido> pp = new ArrayList<Pedido>();
    private AlertDialog alerta;
    private ListView listView;
    private volatile Thread timer;
    private boolean para = true;

    public void stop() {
        para = false;
        onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pendentes, container, false);
        listView = (ListView) view.findViewById(R.id.lvPendentes);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        atualizarFragment();
           }

    public void atualizarFragment() {
        Log.i("[IFMG]", "chamou atualizarFragmanet");
        timer = new Thread(new Runnable() {
            @Override
            public void run() {
                //TODO your background code
                while (para) {
                    atualizaPedidos();
                    try {
                        Thread.sleep(30000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }


        });
        timer.start();
    }

    private void atualizaPedidos() {
        if (getActivity() != null) {
            Log.i("[IFMG]", "faz selveti buscando pedidos pendentes");
            // mostraDialog();
            RestauranteAPI api = SyncDefaut.RETROFIT_RESTAURANTE(getContext()).create(RestauranteAPI.class);
            SharedPreferencesEmpresa s = PreferencesSettings.getAllPreferences(getActivity().getBaseContext());
            final Call<ArrayList<Pedido>> call = api.listarPedidos(s.getEmpEmail(), s.getEmpSenha() + "");

            call.enqueue(new Callback<ArrayList<Pedido>>() {
                @Override
                public void onResponse(Call<ArrayList<Pedido>> call, Response<ArrayList<Pedido>> response) {
                    if (response.code() == 200) {
                        String auth = response.headers().get("auth");

                        if (auth.equals("1")) {
                            //escondeDialog();
                            ArrayList<Pedido> u = response.body();
                            verificaNovo(u);
                        } else {
                            //escondeDialog();
                            Toast.makeText(getActivity().getBaseContext(), "Nome de usuário ou senha incorretos", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        //escondeDialog();
                        Toast.makeText(getActivity().getBaseContext(), "Erro ao fazer login, erro servidor", Toast.LENGTH_SHORT).show();
                        Log.i("[IFMG]", "t1: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<Pedido>> call, Throwable t) {
                    // escondeDialog();
                    Toast.makeText(getActivity().getBaseContext(), "Erro ao fazer login, falhaaaaa", Toast.LENGTH_SHORT).show();
                    Log.i("[IFMG]", "faz login");
                    Log.i("Teste", "t2: " + t.getMessage());
                    //mudaActivity(MainActivity.class);
                }
            });
        }
    }

    private void verificaNovo(ArrayList<Pedido> u) {
        if (u.size() > pp.size()) {
            pp = u;
            Son.start(getContext());
            atualizaTabela(u);
        } else {
            atualizaTabela(u);
        }
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

    private void showFinalizarPedido(final int cod) {
        Log.i("[IFMG]", "codigo = " + cod);
        final LayoutInflater li = getLayoutInflater();
        //inflamos o layout alerta.xml na view
        View view = li.inflate(R.layout.alert_finalizacao, null);
        Button ok = (Button) view.findViewById(R.id.btnOk);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alerta.dismiss();
                finalizarPedido(cod);

            }
        });
        Button cancelar = (Button) view.findViewById(R.id.btnCancelar);
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alerta.dismiss();
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(view);
        builder.setCancelable(false);
        alerta = builder.create();
        alerta.show();
    }


    private void finalizarPedido(int cod) {
        if (getActivity() != null) {
            Log.i("[IFMG]", "faz selveti atualizar pedidos");
            // mostraDialog();
            RestauranteAPI api = SyncDefaut.RETROFIT_RESTAURANTE(getContext()).create(RestauranteAPI.class);
            SharedPreferencesEmpresa s = PreferencesSettings.getAllPreferences(getActivity().getBaseContext());
            Log.i("[IFMG]", "localhost : " + SyncDefaut.getUrl(getContext()));
            Log.i("[IFMG]", "email : " + s.getEmpEmail() + " senha : " + s.getEmpSenha());
            final Call<ArrayList<Pedido>> call = api.alterarPedido(s.getEmpEmail(), s.getEmpSenha() + "", cod + "");

            call.enqueue(new Callback<ArrayList<Pedido>>() {
                @Override
                public void onResponse(Call<ArrayList<Pedido>> call, Response<ArrayList<Pedido>> response) {
                    if (response.code() == 200) {
                        String auth = response.headers().get("auth");
                        Log.i("[IFMG]", "auth : " + auth);
                        if (auth.equals("1")) {
                            //escondeDialog();
                            ArrayList<Pedido> u = response.body();
                            verificaNovo(u);
                        } else {
                            //escondeDialog();
                            Toast.makeText(getActivity().getBaseContext(), "Nome de usuário ou senha incorretos", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        //escondeDialog();
                        Toast.makeText(getActivity().getBaseContext(), "Erro ao fazer login, erro servidor", Toast.LENGTH_SHORT).show();
                        Log.i("[IFMG]", "t1: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<Pedido>> call, Throwable t) {
                    // escondeDialog();
                    Toast.makeText(getActivity().getBaseContext(), "Erro ao fazer login, falhaaaaa", Toast.LENGTH_SHORT).show();
                    Log.i("[IFMG]", "faz login");
                    Log.i("Teste", "t2: " + t.getMessage());
                    //mudaActivity(MainActivity.class);
                }
            });
        }

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
        AdapterPedidos p = (AdapterPedidos) parent.getAdapter();
        showFinalizarPedido(p.getLin().get(position).getCodigo());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.interrupt();
        }
    }
    public void replaceFragment(Fragment fragment) {
        getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, fragment, "IFMG").addToBackStack(null).commit();
    }
}
