package com.example.organizzeclone.model

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class Usuario(var nome: String, var email: String, var senha: String) {
    fun salvarUsuarios(){
        val reerenciaFirebase = FirebaseDatabase.getInstance().reference
    }
}