package com.example.organizzeclone.model

import com.example.organizzeclone.config.ConfiguracaoFirebase
import com.google.firebase.database.Exclude

class Usuario () {
    lateinit var nome: String
    lateinit var email: String
    lateinit @Exclude @get: Exclude var senha: String
    var despesaTotal: Double = 0.0
    var receitaTotal: Double = 0.0

    fun salvarUsuarios(){
        val referenciaFirebaseDatabase = ConfiguracaoFirebase.getFirebaseDatabase()
        referenciaFirebaseDatabase.child("usuario")
            .child(this.email.replace(".", "")).setValue(this)
    }
    fun getId(): String{
        return this.email.replace(".", "")
    }

    constructor(nome: String, emial: String, senha: String) : this(){
        this.nome = nome
        this.email = emial
        this.senha = senha
    }


}