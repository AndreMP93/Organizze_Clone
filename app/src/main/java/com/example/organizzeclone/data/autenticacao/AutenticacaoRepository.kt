package com.example.organizzeclone.data.autenticacao

import com.example.organizzeclone.model.Usuario

class AutenticacaoRepository (private val dataSource: AutenticacaoDataSource) {

    suspend fun verificarUsuarioLogado() = dataSource.verificarUsuarioLogado()

    suspend fun cadastrarUsuario(usuario: Usuario) = dataSource.cadastrarUsuario(usuario)

    suspend fun validarLogin(email: String, senha: String) = dataSource.validarLogin(email, senha)

    suspend fun recuperarEmailUsuarioAutal() = dataSource.recuperarEmailUsuarioAutal()

    fun signOut() = dataSource.singOut()

}