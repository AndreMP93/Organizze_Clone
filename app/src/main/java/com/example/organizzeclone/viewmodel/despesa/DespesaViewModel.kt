package com.example.organizzeclone.viewmodel.despesa

import androidx.lifecycle.ViewModel
import com.example.organizzeclone.data.autenticacao.AutenticacaoRepository
import com.example.organizzeclone.data.database.DataBaseRepository
import com.example.organizzeclone.model.Movimentacao
import com.example.organizzeclone.model.Usuario

class DespesaViewModel(private val dataBaseRepository: DataBaseRepository,
                       private val autenticacaoRepository: AutenticacaoRepository
) : ViewModel(){

    suspend fun cadastrarMovimentcao(mesAno: String, movimentcao: Movimentacao){
        val idUsuario = autenticacaoRepository.recuperarIdUsuarioAutal()
        if (dataBaseRepository.cadastrarMovimentacao(movimentcao, mesAno, idUsuario)){
            atualizarDespesaTotal(movimentcao.valor)
        }
    }

    private suspend fun atualizarDespesaTotal(valor: Double){
        val usuario = recuperarDadosUsuarioAtual()
        usuario.despesaTotal+=valor
        dataBaseRepository.atualizarDadosDoUsuario(usuario.id, usuario)

    }

    private suspend fun recuperarDadosUsuarioAtual() : Usuario {
        val id = autenticacaoRepository.recuperarIdUsuarioAutal()
        return dataBaseRepository.recuperarDadosDoUsuario(id)
    }
}