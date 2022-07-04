package com.example.organizzeclone.model

import com.google.firebase.database.Exclude

class Usuario () {
    lateinit var nome: String
    lateinit var email: String
    lateinit @Exclude @get: Exclude var senha: String
    var despesaTotal: Double = 0.0
    var receitaTotal: Double = 0.0


    fun getId(): String{
        return this.email.replace(".", "")
    }

    constructor(nome: String, emial: String, senha: String) : this(){
        this.nome = nome
        this.email = emial
        this.senha = senha
    }


}