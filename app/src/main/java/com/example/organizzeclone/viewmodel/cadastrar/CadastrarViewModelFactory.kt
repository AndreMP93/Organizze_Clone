package com.example.organizzeclone.viewmodel.cadastrar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.organizzeclone.data.autenticacao.AutenticacaoRepository
import com.example.organizzeclone.data.database.DataBaseRepository
import java.lang.IllegalArgumentException

class CadastrarViewModelFactory(
    private val dataBaseRepository: DataBaseRepository,
    private val autenticacaoRepository: AutenticacaoRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if( modelClass.isAssignableFrom(CadastrarViewModel::class.java)){
            CadastrarViewModel(this.dataBaseRepository, this.autenticacaoRepository) as T
        }else{
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}