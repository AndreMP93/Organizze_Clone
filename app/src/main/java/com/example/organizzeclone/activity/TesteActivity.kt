package com.example.organizzeclone.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.organizzeclone.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class TesteActivity : AppCompatActivity() {

    private lateinit var botaoSair: Button
    private lateinit var autenticacao: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teste)

        autenticacao = Firebase.auth
        botaoSair =findViewById(R.id.buttonSair)
        botaoSair.setOnClickListener {

            autenticacao.signOut()
            finish()
        }
    }
}