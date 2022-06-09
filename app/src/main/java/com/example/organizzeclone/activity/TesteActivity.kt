package com.example.organizzeclone.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.organizzeclone.R
import com.github.clans.fab.FloatingActionButton
import com.github.clans.fab.FloatingActionMenu
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class TesteActivity : AppCompatActivity() {

    private lateinit var botaoSair: Button
    private lateinit var autenticacao: FirebaseAuth
    private lateinit var fabDespesa: FloatingActionButton
    private lateinit var fabReceita: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teste)

        autenticacao = Firebase.auth
        botaoSair =findViewById(R.id.buttonSair)
        botaoSair.setOnClickListener {

            autenticacao.signOut()
            finish()
        }

        fabDespesa = findViewById(R.id.menu_despesa)
        fabDespesa.setOnClickListener {
            startActivity(Intent(
                applicationContext,
                DespesaActivity::class.java
            ))
        }

        fabReceita = findViewById(R.id.menu_receita)
        fabReceita.setOnClickListener {
            startActivity(Intent(
                applicationContext,
                ReceitaActivity::class.java
            ))
        }
    }
}