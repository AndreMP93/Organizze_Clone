package com.example.organizzeclone.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.example.organizzeclone.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.heinrichreimersoftware.materialintro.app.IntroActivity
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide

class MainActivity : IntroActivity() {

    private lateinit var autenticacao: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        isButtonBackVisible = false
        isButtonNextVisible = false
        addSlide(FragmentSlide.Builder()
            .background(R.color.white)
            .backgroundDark(R.color.black)
            .fragment(R.layout.intro_01)
            .build())

        addSlide(FragmentSlide.Builder()
            .background(R.color.white)
            .backgroundDark(R.color.black)
            .fragment(R.layout.intro_02)
            .build())

        addSlide(FragmentSlide.Builder()
            .background(R.color.white)
            .backgroundDark(R.color.black)
            .fragment(R.layout.intro_03)
            .build())

        addSlide(FragmentSlide.Builder()
            .background(R.color.white)
            .backgroundDark(R.color.black)
            .fragment(R.layout.intro_04)
            .build())

        addSlide(FragmentSlide.Builder()
            .background(R.color.white)
            .backgroundDark(R.color.black)
            .fragment(R.layout.intro_cadastro)
            .canGoForward(false)
            .build())


    }

    override fun onStart() {
        super.onStart()
        verificarUsuarioLogado()
    }


    fun btCadastrar(view: View){
        startActivity(Intent(applicationContext, CadastrarActivity::class.java))

    }

    fun btEntrar(view: View){
        startActivity(Intent(applicationContext, EntrarActivity::class.java))
    }

    fun verificarUsuarioLogado(){
        autenticacao = Firebase.auth
        if(autenticacao.currentUser != null){
            abrirTelaPrincipal()
        }
    }

    private fun abrirTelaPrincipal(){
        startActivity(
            Intent(
                application,
                InicialActivity::class.java
            )
        )
    }

}