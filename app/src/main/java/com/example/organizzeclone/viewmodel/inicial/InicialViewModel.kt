package com.example.organizzeclone.viewmodel.inicial

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.organizzeclone.data.autenticacao.AutenticacaoRepository
import com.example.organizzeclone.data.database.DataBaseRepository
import com.example.organizzeclone.model.Movimentacao
import com.example.organizzeclone.model.Usuario
import kotlinx.coroutines.launch

class InicialViewModel(private val dataBaseRepository: DataBaseRepository,
                       private val autenticacaoRepository: AutenticacaoRepository
) : ViewModel(){

    val listaMovimentacoes = MutableLiveData<ArrayList<Movimentacao>>()
    val dadosUsuario = MutableLiveData<Usuario>()
    val erroManager = MutableLiveData<String>()

    init {

        listaMovimentacoes.postValue(ArrayList<Movimentacao>())
        dadosUsuario.postValue(Usuario("", "", ""))
        erroManager.postValue("")
    }


    fun recuperarDadosUsuarioAtual() {
        viewModelScope.launch {
            try {
                val id = autenticacaoRepository.recuperarIdUsuarioAutal()
                val usuario = dataBaseRepository.recuperarDadosDoUsuario(id)
                dadosUsuario.postValue(usuario)
            }catch (e: Exception){
                erroManager.postValue(e.message)
            }
        }
    }

    fun recuperarListaDeMovimentacoes(mesAno: String){
        viewModelScope.launch {
            try {
                val id = autenticacaoRepository.recuperarIdUsuarioAutal()
                val lista = dataBaseRepository.recuperaListaDeMovimentacaoes(
                    mesAno,
                    id)
                listaMovimentacoes.postValue(lista)
            }catch (e: Exception){
                erroManager.postValue(e.message)
            }
        }

    }

    fun excluirMovimentacao(mesAno: String, movimentacao: Movimentacao){
        viewModelScope.launch {
            try {
                val idUsuario = autenticacaoRepository.recuperarIdUsuarioAutal()
                dataBaseRepository.excluirMovimentacao(mesAno, movimentacao,idUsuario)
                recuperarListaDeMovimentacoes(mesAno)
                recuperarDadosUsuarioAtual()
            }catch (e: Exception){
                erroManager.postValue(e.message)
            }
        }
    }

    fun signOut(){
        autenticacaoRepository.signOut()
    }
}