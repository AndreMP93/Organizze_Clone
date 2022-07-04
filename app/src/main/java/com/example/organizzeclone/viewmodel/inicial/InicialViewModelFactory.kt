package com.example.organizzeclone.viewmodel.inicial

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.organizzeclone.data.autenticacao.AutenticacaoRepository
import com.example.organizzeclone.data.database.DataBaseRepository
import com.example.organizzeclone.viewmodel.despesa.DespesaViewModel
import java.lang.IllegalArgumentException

class InicialViewModelFactory(private val dataBaseRepository: DataBaseRepository,
                              private val autenticacaoRepository: AutenticacaoRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if( modelClass.isAssignableFrom(InicialViewModel::class.java)){
            InicialViewModel(this.dataBaseRepository, this.autenticacaoRepository) as T
        }else{
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}