package com.example.organizzeclone.data.database

import com.example.organizzeclone.config.ConfiguracaoFirebase
import com.example.organizzeclone.model.Movimentacao
import com.example.organizzeclone.model.Usuario
import com.google.firebase.database.ktx.getValue
import kotlin.coroutines.suspendCoroutine

class RealtimeDatabeseFirebaseDataSource : DataBaseDataSource {
    private val referenciaUsuario = ConfiguracaoFirebase.getFirebaseDatabase().child("usuario")
    private val referenciaMovimentacao = ConfiguracaoFirebase.getFirebaseDatabase().child("movimentacao")



    override suspend fun cadastrarMovimentacao(movimentacao: Movimentacao, mesAno: String, idUsuario: String): Boolean {
        return suspendCoroutine { continuation ->
            referenciaMovimentacao.child(idUsuario).child(mesAno).push().setValue(movimentacao).addOnCompleteListener {
                continuation.resumeWith(Result.success(true))
            }.addOnFailureListener {
                continuation.resumeWith(Result.failure(it))
            }
        }
    }

    override suspend fun recuperaListaDeMovimentacaoes(mesAno: String, idUsuario: String): ArrayList<Movimentacao> {
        return suspendCoroutine { continuation ->
            referenciaMovimentacao.child(idUsuario).child(mesAno).get().addOnSuccessListener {
                val listaMovimentacao = ArrayList<Movimentacao>()
                for(dados in it.children) {
                    val movimentacao = dados.getValue<Movimentacao>()!!
                    movimentacao.id = dados.key.toString()
                    listaMovimentacao.add(movimentacao)
                }
                continuation.resumeWith(Result.success(listaMovimentacao))
            }.addOnFailureListener {
                continuation.resumeWith(Result.failure(it))
            }
        }
    }

    override suspend fun excluirMovimentacao(mesAno: String, movimentacao: Movimentacao, idUsuario: String) {
        val usuario = this.recuperarDadosDoUsuario(idUsuario)
        referenciaMovimentacao
            .child(idUsuario)
            .child(mesAno)
            .child(movimentacao.id)
            .removeValue()

        if(movimentacao.tipo == "R"){
            referenciaUsuario.child(idUsuario).child("receitaTotal").setValue(
                usuario.receitaTotal - movimentacao.valor)
        }else{
            referenciaUsuario.child(idUsuario).child("despesaTotal").setValue(
                usuario.despesaTotal - movimentacao.valor)
        }
    }

    override suspend fun salvarDadosDoUsuario(idUsuario: String, u: Usuario): Boolean {
        return suspendCoroutine { continuation ->
            referenciaUsuario.child(idUsuario)
                .setValue(u).addOnCompleteListener {
                    continuation.resumeWith(Result.success(true))
                }
                .addOnFailureListener {  exception ->
                    continuation.resumeWith(Result.failure(exception))
                }
        }
    }

    override suspend fun atualizarDadosUsuario(id: String, usuario: Usuario): Boolean {
        return suspendCoroutine { continuation ->
            referenciaUsuario.child(id)
                .setValue(usuario).addOnCompleteListener {
                    continuation.resumeWith(Result.success(true))
                }
                .addOnFailureListener {  exception ->
                    continuation.resumeWith(Result.failure(exception))
                }
        }
    }

    override suspend fun recuperarDadosDoUsuario(id: String): Usuario {
        return suspendCoroutine { continuation ->
            referenciaUsuario.child(id).get().addOnSuccessListener {
                val usuario = it.getValue<Usuario>()!!
                continuation.resumeWith(Result.success(usuario))
            }.addOnFailureListener {
                continuation.resumeWith(Result.failure(it))
            }
        }
    }
}