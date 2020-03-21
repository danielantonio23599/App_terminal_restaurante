package com.daniel.app_terminal_restaurante;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;


import com.daniel.app_terminal_restaurante.modelo.beans.PreferencesSettings;
import com.daniel.app_terminal_restaurante.modelo.beans.SharedPreferencesEmpresa;
import com.daniel.app_terminal_restaurante.sync.RestauranteAPI;
import com.daniel.app_terminal_restaurante.sync.SyncDefaut;
import com.daniel.app_terminal_restaurante.util.PermissionUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by polo on 30/06/2018.
 */

public class LoginActivity extends AppCompatActivity {

    private EditText userEmail, user_pwd;
    private Button buttonLogin;
    private TextView tvSign_up;
    private AlertDialog alerta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //settings.setUserLogOff(getBaseContext());
        //mostraDialog();
        setContentView(R.layout.activity_login);

        tvSign_up = (TextView) findViewById(R.id.tvSign_up);
        userEmail = (EditText) findViewById(R.id.input_email);
        user_pwd = (EditText) findViewById(R.id.input_senha);
        buttonLogin = (Button) findViewById(R.id.btnLogin);

        //todo validar todas as permissões de uma vez aqui
        String[] permissoes = new String[]{
                android.Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE
        };
        PermissionUtils.validate(this, 0, permissoes);


        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Todo Verificar credenciais e rodar SplashScreen
                fazLogin(userEmail.getText() + "", user_pwd.getText() + "");
                //Com MD5: -> Cadastrar Adm com MD5
//                fazLogin(userName.getText() + "", StringUtils.md5(user_pwd.getText() + ""));

            }
        });

        tvSign_up.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                tvSign_up.setPaintFlags(tvSign_up.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                tvSign_up.setTextColor(R.color.colorAccent);
                mudaActivity(PedidosActivity.class);

            }
        });

    }

    /*@Override
    protected void onResume() {
        super.onResume();
        CategoriaControle ca = new CategoriaControle(getApplicationContext());
        if (ca.listarCategorias().size() == 0) {
            //PovoamentoBD.povoar(getApplicationContext());
            Log.i("[IFMG]", "povoamento");
        }
        Usuario u = settings.getAllPreferences(getBaseContext());

        if (!u.getNome().equals("")) {
            Log.i("[IFMG]", "usuario" + u.getId());
            mudaActivity(MainActivity.class);
        }

    }*/

    private void mudaActivity(Class classe) {
        Log.i("[IFMG]", "passou no muda actyvity" + classe.getName());
        Intent intent = new Intent(this, classe);
        startActivity(intent);
        finish();

    }

    public void fazLogin(String nomeUsuario, String senha) {
        Log.i("[IFMG]", "faz login");
        mostraDialog();

        RestauranteAPI api = SyncDefaut.RETROFIT_RESTAURANTE.create(RestauranteAPI.class);

        final Call<SharedPreferencesEmpresa> call = api.fazLoginEmpresa(nomeUsuario, senha);

        call.enqueue(new Callback<SharedPreferencesEmpresa>() {
            @Override
            public void onResponse(Call<SharedPreferencesEmpresa> call, Response<SharedPreferencesEmpresa> response) {
                if (response.code() == 200) {
                    String auth = response.headers().get("auth");

                    if (auth.equals("1")) {
                        escondeDialog();
                        SharedPreferencesEmpresa u = response.body();
                        //Preference Settings ==============================================
                        PreferencesSettings.updateAllPreferences(getBaseContext(), u);
                        mudaActivity(PedidosActivity.class);
                    } else {
                        escondeDialog();
                        Toast.makeText(getBaseContext(), "Nome de usuário ou senha incorretos", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    escondeDialog();
                    Toast.makeText(getBaseContext(), "Erro ao fazer login, erro servidor", Toast.LENGTH_SHORT).show();
                    Log.i("[IFMG]", "t1: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<SharedPreferencesEmpresa> call, Throwable t) {
                escondeDialog();
                Toast.makeText(getBaseContext(), "Erro ao fazer login, falhaaaaa", Toast.LENGTH_SHORT).show();
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
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
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

}
