package com.example.organizzeclone.data.database

import com.example.organizzeclone.model.Movimentacao
import com.example.organizzeclone.model.Usuario

class DataBaseRepository (
    private val dataSource: DataBaseDataSource
    ){

    suspend fun cadastrarMovimentacao(movimentacao: Movimentacao, mesAno: String, idUsuario: String) = dataSource.cadastrarMovimentacao(movimentacao, mesAno, idUsuario)

    suspend fun recuperarDadosDoUsuario(id: String): Usuario = dataSource.recuperarDadosDoUsuario(id)

    suspend fun recuperaListaDeMovimentacaoes(mesAno: String, idUsuario: String): ArrayList<Movimentacao> = dataSource.recuperaListaDeMovimentacaoes(mesAno, idUsuario)

    suspend fun excluirMovimentacao(mesAno: String, movimentacao: Movimentacao, idUsuario: String) = dataSource.excluirMovimentacao(mesAno, movimentacao, idUsuario)

    suspend fun salvarDadosDoUsuario(id: String, usuario: Usuario) = dataSource.salvarDadosDoUsuario(id ,usuario)

    suspend fun atualizarDadosDoUsuario(id: String, usuario: Usuario) = dataSource.atualizarDadosUsuario(id, usuario)
}