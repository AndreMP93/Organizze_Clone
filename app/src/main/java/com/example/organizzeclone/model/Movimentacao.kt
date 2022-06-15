package com.example.organizzeclone.model

import com.example.organizzeclone.config.ConfiguracaoFirebase
import com.example.organizzeclone.helper.DateUtil
import kotlin.properties.Delegates

class Movimentacao() {
    var valor by Delegates.notNull<Double>()
    lateinit var data: String
    lateinit var categoria: String
    lateinit var descricao: String
    lateinit var tipo: String

    constructor(valor: Double, data: String, categoria: String, descricao: String, tipo: String) : this(){
        this.valor = valor
        this.data = data
        this.categoria = categoria
        this.descricao = descricao
        this.tipo = tipo

    }
    fun salvarBD(){
        val autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao()
        val email = autenticacao.currentUser?.email?.replace(".", "")
        val chaveData = DateUtil.mesAnoData(this.data)
        val firebase = ConfiguracaoFirebase.getFirebaseDatabase()
        firebase.child("movimentacao")
            .child(email.toString())
            .child(chaveData)
            .push().setValue(this)
    }


}