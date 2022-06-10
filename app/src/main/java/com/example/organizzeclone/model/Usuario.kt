package com.example.organizzeclone.model

import com.example.organizzeclone.config.ConfiguracaoFirebase
import com.google.firebase.database.Exclude

class Usuario(var nome: String, var email: String, @get: Exclude var senha: String) {
    fun salvarUsuarios(){
        val referenciaFirebaseDatabase = ConfiguracaoFirebase.getFirebaseDatabase()
        referenciaFirebaseDatabase.child("usuario")
            .child(this.email.replace(".", "")).setValue(this)
    }
}