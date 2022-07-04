package com.example.organizzeclone.viewmodel.cadastrar

import androidx.lifecycle.ViewModel
import com.example.organizzeclone.data.autenticacao.AutenticacaoRepository
import com.example.organizzeclone.data.database.DataBaseRepository
import com.example.organizzeclone.model.Usuario

class CadastrarViewModel(
        private val dataBaseRepository: DataBaseRepository,
        private val autenticacaoRepository: AutenticacaoRepository
    ) : ViewModel() {

    suspend fun cadastrarUsusario(usuario: Usuario): Boolean{
        if (autenticacaoRepository.cadastrarUsuario(usuario)){
            val id = autenticacaoRepository.recuperarEmailUsuarioAutal()
                .replace(".", "")
            return dataBaseRepository.salvarDadosDoUsuario(id, usuario)
        }
        return false
    }

}