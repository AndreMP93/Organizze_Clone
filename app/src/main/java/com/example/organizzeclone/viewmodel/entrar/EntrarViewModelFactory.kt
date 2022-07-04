package com.example.organizzeclone.viewmodel.entrar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.organizzeclone.data.autenticacao.AutenticacaoRepository
import java.lang.IllegalArgumentException

class EntrarViewModelFactory(private val autenticacaoRepository: AutenticacaoRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if( modelClass.isAssignableFrom(EntrarViewModel::class.java)){
            EntrarViewModel(this.autenticacaoRepository) as T
        }else{
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}