package com.example.organizzeclone.data.database

import com.example.organizzeclone.model.Movimentacao
import com.example.organizzeclone.model.Usuario

interface DataBaseDataSource {


    suspend fun cadastrarMovimentacao(movimentacao: Movimentacao, mesAno: String, idUsuario: String): Boolean

    suspend fun recuperaListaDeMovimentacaoes(mesAno: String , idUsuario: String): ArrayList<Movimentacao>

    suspend fun excluirMovimentacao(mesAno: String, movimentacao: Movimentacao, idUsuario: String)

    suspend fun recuperarDadosDoUsuario(id: String): Usuario

    suspend fun salvarDadosDoUsuario(idUsuario: String, usuario: Usuario): Boolean

    suspend fun atualizarDadosUsuario(id: String, usuario: Usuario): Boolean
}