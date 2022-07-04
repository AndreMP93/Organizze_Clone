package com.example.organizzeclone.data.autenticacao

import com.example.organizzeclone.model.Usuario

interface AutenticacaoDataSource {
    suspend fun verificarUsuarioLogado(): Boolean

    suspend fun cadastrarUsuario(usuario: Usuario): Boolean

    suspend fun validarLogin(email: String, senha: String): Boolean

    suspend fun recuperarEmailUsuarioAutal(): String

    fun singOut()

}