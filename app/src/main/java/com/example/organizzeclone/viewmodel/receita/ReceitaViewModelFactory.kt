package com.example.organizzeclone.viewmodel.receita

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.organizzeclone.data.autenticacao.AutenticacaoRepository
import com.example.organizzeclone.data.database.DataBaseRepository
import com.example.organizzeclone.viewmodel.cadastrar.CadastrarViewModel
import java.lang.IllegalArgumentException

class ReceitaViewModelFactory(
    private val dataBaseRepository: DataBaseRepository,
    private val autenticacaoRepository: AutenticacaoRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if( modelClass.isAssignableFrom(ReceitaViewModel::class.java)){
            ReceitaViewModel(this.dataBaseRepository, this.autenticacaoRepository) as T
        }else{
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}