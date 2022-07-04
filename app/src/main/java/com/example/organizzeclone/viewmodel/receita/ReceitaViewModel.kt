package com.example.organizzeclone.viewmodel.receita

import androidx.lifecycle.ViewModel
import com.example.organizzeclone.data.autenticacao.AutenticacaoRepository
import com.example.organizzeclone.data.database.DataBaseRepository
import com.example.organizzeclone.model.Movimentacao
import com.example.organizzeclone.model.Usuario

class ReceitaViewModel(private val dataBaseRepository: DataBaseRepository,
                       private val autenticacaoRepository: AutenticacaoRepository
) : ViewModel() {

    suspend fun cadastrarMovimentcao(mesAno: String, movimentcao: Movimentacao){
        val idUsuario = autenticacaoRepository.recuperarEmailUsuarioAutal().replace(".", "")
        if (dataBaseRepository.cadastrarMovimentacao(movimentcao, mesAno, idUsuario)){
            atualizarReceitatotal(movimentcao.valor)
        }
    }

    private suspend fun atualizarReceitatotal(valor: Double){
        val usuario = recuperarDadosUsuarioAtual()
        usuario.receitaTotal+=valor
        dataBaseRepository.atualizarDadosDoUsuario(usuario.getId(), usuario)

    }

    private suspend fun recuperarDadosUsuarioAtual() : Usuario {
        val id = autenticacaoRepository.recuperarEmailUsuarioAutal().replace(".", "")
        return dataBaseRepository.recuperarDadosDoUsuario(id)
    }

}