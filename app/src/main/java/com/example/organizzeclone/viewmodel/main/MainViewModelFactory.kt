package com.example.organizzeclone.viewmodel.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.organizzeclone.data.autenticacao.AutenticacaoRepository
import java.lang.IllegalArgumentException

class MainViewModelFactory(private val autenticacaoRepository: AutenticacaoRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if( modelClass.isAssignableFrom(MainViewModel::class.java)){
            MainViewModel(this.autenticacaoRepository) as T
        }else{
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }


}