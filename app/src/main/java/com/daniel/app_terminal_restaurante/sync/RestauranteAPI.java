package com.daniel.app_terminal_restaurante.sync;
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
    @POST("restaurante_server/BuscarUm")
    Call<Produtos> buscarUmProduto(@Field("nomeUsuario") String nomeUsuario, @Field("senha") String senha, @Field("produto") String cod);

    @FormUrlEncoded
    @POST("restaurante_server/TotalMesa")
    Call<Void> getValorMesa(@Field("nomeUsuario") String nomeUsuario, @Field("senha") String senha, @Field("mesa") String mesa);

    @FormUrlEncoded
    @POST("restaurante_server/TotalVendidoCaixa")
    Call<Void> totalVendidoCaixa(@Field("nomeUsuario") String nomeUsuario, @Field("senha") String senha);

    @FormUrlEncoded
    @POST("restaurante_server/ListarProdutosVendidos")
    Call<ArrayList<ProdutosGravados>> listarProdutosVendidos(@Field("nomeUsuario") String nomeUsuario, @Field("senha") String senha);

    @FormUrlEncoded
    @POST("restaurante_server/ListarProdutosMesa")
    Call<ArrayList<ProdutosGravados>> listarProdutosMesa(@Field("nomeUsuario") String nomeUsuario, @Field("senha") String senha, @Field("mesa") String mesa);



    @FormUrlEncoded
    @POST("restaurante_server/AtualizaVenda")
    Call<Void> atualizaVenda(@Field("venda") String venda, @Field("nomeUsuario") String nomeUsuario, @Field("senha") String senha);


    @FormUrlEncoded
    @POST("restaurante_server/CancelarPedido")
    Call<Void> cancelarPedido(@Field("pedido") String pedido, @Field("motivo") String motivo, @Field("nomeUsuario") String nomeUsuario, @Field("senha") String senha);


    @FormUrlEncoded
    @POST("restaurante_server/LoginEmpresa")
    Call<SharedPreferencesEmpresa> fazLoginEmpresa(@Field("nomeUsuario") String nomeUsuario, @Field("senha") String senha);

    @FormUrlEncoded
    @POST("restaurante_server/ListarPedidos")
    Call<ArrayList<Pedido>> listarPedidos(@Field("nomeUsuario") String nomeUsuario, @Field("senha") String senha);
    @FormUrlEncoded
    @POST("restaurante_server/ListarPedidosRealizados")
    Call<ArrayList<Pedido>> listarPedidosRealizados(@Field("nomeUsuario") String nomeUsuario, @Field("senha") String senha);
    @FormUrlEncoded
    @POST("restaurante_server/ListarPedidosAtrazados")
    Call<ArrayList<Pedido>> listarPedidosAtrazados(@Field("nomeUsuario") String nomeUsuario, @Field("senha") String senha);
}
