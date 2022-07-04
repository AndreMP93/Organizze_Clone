package com.example.organizzeclone.viewmodel.main

import androidx.lifecycle.ViewModel
import com.example.organizzeclone.data.autenticacao.AutenticacaoRepository

class MainViewModel(private val autenticacaoRepository: AutenticacaoRepository) : ViewModel() {

    suspend fun verificarUsuarioLogado(): Boolean{
        return autenticacaoRepository.verificarUsuarioLogado()
    }

}