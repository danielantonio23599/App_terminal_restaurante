package com.daniel.app_terminal_restaurante.sync;

import com.daniel.app_terminal_restaurante.adapter.holder.Mesa;
import com.daniel.app_terminal_restaurante.adapter.holder.Pedido;
import com.daniel.app_terminal_restaurante.modelo.beans.Produtos;
import com.daniel.app_terminal_restaurante.modelo.beans.ProdutosGravados;
import com.daniel.app_terminal_restaurante.modelo.beans.SharedPreferencesEmpresa;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RestauranteAPI {
    // Servlets para testes no servidor local
    @FormUrlEncoded
    @POST("restaurante_server/LoginEmpresa")
    Call<SharedPreferencesEmpresa> fazLoginEmpresa(@Field("nomeUsuario") String nomeUsuario, @Field("senha") String senha);

    @FormUrlEncoded
    @POST("restaurante_server/ListarPedidos")
    Call<ArrayList<Pedido>> listarPedidos(@Field("nomeUsuario") String nomeUsuario, @Field("senha") String senha);

    @FormUrlEncoded
    @POST("restaurante_server/AlterarPedido")
    Call<ArrayList<Pedido>> alterarPedido(@Field("nomeUsuario") String nomeUsuario, @Field("senha") String senha, @Field("pedido") String pedido);

    @FormUrlEncoded
    @POST("restaurante_server/ListarPedidosRealizados")
    Call<ArrayList<Pedido>> listarPedidosRealizados(@Field("nomeUsuario") String nomeUsuario, @Field("senha") String senha);
    @FormUrlEncoded
    @POST("restaurante_server/ListarMesasAbertas")
    Call<ArrayList<Mesa>> getMesasAbertas(@Field("nomeUsuario") String nomeUsuario, @Field("senha") String senha);

}
