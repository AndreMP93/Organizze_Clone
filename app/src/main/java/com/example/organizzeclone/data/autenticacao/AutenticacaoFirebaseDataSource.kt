package com.example.organizzeclone.data.autenticacao

import com.example.organizzeclone.config.ConfiguracaoFirebase
import com.example.organizzeclone.model.Usuario
import com.google.firebase.auth.FirebaseAuth
import kotlin.coroutines.suspendCoroutine

class AutenticacaoFirebaseDataSource : AutenticacaoDataSource {

    private var autenticacao: FirebaseAuth = ConfiguracaoFirebase.getFirebaseAutenticacao()

    override suspend fun verificarUsuarioLogado(): Boolean {
        return suspendCoroutine {
            if(autenticacao.currentUser != null){
                it.resumeWith(Result.success(true))
            }else{
                it.resumeWith(Result.success(false))
            }
        }
    }

    override suspend fun cadastrarUsuario(usuario: Usuario) : Boolean{

        return suspendCoroutine { continuation ->
            autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao()
            autenticacao.createUserWithEmailAndPassword(usuario.email, usuario.senha)
                .addOnCompleteListener {
                if (it.isSuccessful){
                    continuation.resumeWith(Result.success(true))
                }
            }.addOnFailureListener{ exception ->
                    continuation.resumeWith(Result.failure(exception))
                }
        }
    }

    override suspend fun validarLogin(email: String, senha: String): Boolean {
        return suspendCoroutine { continuation ->
            autenticacao.signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener {
                    if (it.isSuccessful){
                        continuation.resumeWith(Result.success(true))
                    }
                }.addOnFailureListener {
                    continuation.resumeWith(Result.failure(it))
                }
        }
    }

    override suspend fun recuperarIdUsuarioAutal(): String {
        return autenticacao.currentUser?.uid.toString()
    }

    override fun singOut() {
        autenticacao.signOut()
    }

}