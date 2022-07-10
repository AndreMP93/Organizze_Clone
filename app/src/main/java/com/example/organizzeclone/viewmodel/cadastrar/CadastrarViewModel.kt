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
            usuario.id = autenticacaoRepository.recuperarIdUsuarioAutal()
            return dataBaseRepository.salvarDadosDoUsuario(usuario.id, usuario)
        }
        return false
    }

}