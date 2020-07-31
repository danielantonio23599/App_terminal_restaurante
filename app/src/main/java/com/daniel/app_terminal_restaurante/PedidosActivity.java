package com.daniel.app_terminal_restaurante;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.daniel.app_terminal_restaurante.adapter.navigation.TabsAdapter;
import com.daniel.app_terminal_restaurante.fragment.PendentesFragment;
import com.daniel.app_terminal_restaurante.fragment.RealizadosFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

public class PedidosActivity extends AppCompatActivity {
    private ViewPager content;
    private Toast toast;
    private long lastBackPressTime = 0;
    private PendentesFragment l;
    private RealizadosFragment r;
    private AlertDialog alerta;
    private BottomNavigationView navigation;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Log.i("IFMG", "onNavegation");
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    content.setCurrentItem(0);
                    return true;
                case R.id.navigation_dashboard:
                    content.setCurrentItem(1);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        content = (ViewPager) findViewById(R.id.viewpager);
        //Todo Método para colocar fragment no view pager
        Log.i("IFMG", "onCeate");
        //cria adapter para tabs(para jogar no ViewPager)
        l = new PendentesFragment();
        r = new RealizadosFragment();
        TabsAdapter adapter = new TabsAdapter(getSupportFragmentManager());
        adapter.add(r, "");
        adapter.add(l, "");
        navigation = (BottomNavigationView) findViewById(R.id.nav_view);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_home);
        content = (ViewPager) findViewById(R.id.viewpager); //identifica pagerview !!!!!!!!!!!!!!!!!!!!! Repetido
        content.setAdapter(adapter);  //seta adapter
        //seta enventos do PagerView para com ButtonNav
        content.setOnPageChangeListener(onPageChangeListener);
        content.setCurrentItem(1);


    }

    /*
     * Criação dos enventos do PagerView para com ButtonNav
     */
    ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            Log.i("IFMG", "onPage");
            switch (position) {
                case 0:
                    navigation.setSelectedItemId(R.id.navigation_home);
                    break;
                case 1:
                    navigation.setSelectedItemId(R.id.navigation_dashboard);
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private void mostraDialog() {
        final LayoutInflater li = getLayoutInflater();
        //inflamos o layout alerta.xml na view
        View view = li.inflate(R.layout.alert_progress, null);
        TextView tvDesc = (TextView) view.findViewById(R.id.tvDesc);    //definimos para o botão do layout um clickListener
        tvDesc.setText("Aguarde carregando...");
        AlertDialog.Builder builder = new AlertDialog.Builder(PedidosActivity.this);
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

    @Override
    public void onBackPressed() {
        if (this.lastBackPressTime < System.currentTimeMillis() - 2000) {
            toast = Toast.makeText(this, "Pressione o Botão Voltar novamente para finalizar a viagem", Toast.LENGTH_SHORT);
            toast.show();
            this.lastBackPressTime = System.currentTimeMillis();
        } else {
            if (toast != null) {
                l.stop();
                r.stop();
                toast.cancel();
                super.onBackPressed();
            }

        }
    }

}

