package com.daniel.app_terminal_restaurante.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.daniel.app_terminal_restaurante.R;
import com.daniel.app_terminal_restaurante.adapter.holder.Pedido;
import com.daniel.app_terminal_restaurante.util.Time;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel on 30/05/2018.
 */

public class AdapterPedidos extends BaseAdapter {
    private List<Pedido> lin = new ArrayList<Pedido>();

    public List<Pedido> getLin() {
        return lin;
    }

    public void setLin(List<Pedido> lin) {
        this.lin = lin;
    }

    private Context context;

    public AdapterPedidos(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return lin.size();
    }

    @Override
    public Object getItem(int position) {
        return lin.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.i("[IFMG]", "view: " + lin.get(position).getProduto());
        View view = LayoutInflater.from(context).inflate(R.layout.pedidos_adapter, parent, false);
        TextView produto = (TextView) view.findViewById(R.id.tvProduto);
        TextView tempo_preparo = (TextView) view.findViewById(R.id.tvTempoPreparo);
        TextView observacao = (TextView) view.findViewById(R.id.tvObservacao);
        TextView mesa = (TextView) view.findViewById(R.id.tvNunMesa);
        TextView quantidade = (TextView) view.findViewById(R.id.tvQuantidade);
        TextView tempoEspera = (TextView) view.findViewById(R.id.tvTempoEspera);
        FrameLayout layout = (FrameLayout) view.findViewById(R.id.layFundo);
        Log.i("[IFMG]", "produto: " + lin.get(position).getProduto());
        Log.i("[IFMG]", "tempo: " + lin.get(position).getTempo_preparo());
        Log.i("[IFMG]", "obs: " + lin.get(position).getObservacao());
        Log.i("[IFMG]", "qtd: " + lin.get(position).getQuantidade());
        Log.i("[IFMG]", "mesa: " + lin.get(position).getMesa());
        Log.i("[IFMG]", "tempo esp: " + lin.get(position).getTempo_gasto());
        Log.i("[IFMG]", "hora pedido: " + lin.get(position).getHora_pedido());

        produto.setText(lin.get(position).getProduto() + "");
        tempo_preparo.setText(lin.get(position).getTempo_preparo());
        observacao.setText(lin.get(position).getObservacao());
        quantidade.setText(lin.get(position).getQuantidade() + "");
        tempoEspera.setText(Time.subtraiHoras(lin.get(position).getHora_pedido()));
        mesa.setText(lin.get(position).getMesa()+"");
        return view;
    }
}
