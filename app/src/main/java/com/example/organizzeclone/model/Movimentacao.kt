package com.example.organizzeclone.model

import com.example.organizzeclone.config.ConfiguracaoFirebase
import com.example.organizzeclone.helper.DateUtil
import com.google.firebase.database.Exclude
import kotlin.properties.Delegates

class Movimentacao() {
    var valor by Delegates.notNull<Double>()
    lateinit var data: String
    lateinit var categoria: String
    lateinit var descricao: String
    lateinit var tipo: String
    @Exclude @get:Exclude
    lateinit var id: String

    constructor(valor: Double, data: String, categoria: String, descricao: String, tipo: String) : this(){
        this.valor = valor
        this.data = data
        this.categoria = categoria
        this.descricao = descricao
        this.tipo = tipo

    }

}