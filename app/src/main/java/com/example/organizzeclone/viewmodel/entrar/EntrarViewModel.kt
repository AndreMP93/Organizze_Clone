package com.example.organizzeclone.viewmodel.entrar

import androidx.lifecycle.ViewModel
import com.example.organizzeclone.data.autenticacao.AutenticacaoRepository


class EntrarViewModel (private val autenticacaoRepository: AutenticacaoRepository) : ViewModel() {
    suspend fun validarLogin(email: String, senha: String): Boolean{
        return autenticacaoRepository.validarLogin(email, senha)
    }
}