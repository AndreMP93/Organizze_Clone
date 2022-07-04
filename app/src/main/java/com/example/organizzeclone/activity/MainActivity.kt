package com.example.organizzeclone.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.organizzeclone.R
import com.example.organizzeclone.data.autenticacao.AutenticacaoFirebaseDataSource
import com.example.organizzeclone.data.autenticacao.AutenticacaoRepository
import com.example.organizzeclone.viewmodel.main.MainViewModel
import com.example.organizzeclone.viewmodel.main.MainViewModelFactory
import com.heinrichreimersoftware.materialintro.app.IntroActivity
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide
import kotlinx.coroutines.launch

class MainActivity : IntroActivity() {

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*val aut = ConfiguracaoFirebase.getFirebaseAutenticacao()
        aut.signOut()*/

        viewModel = ViewModelProvider(
            this,
            MainViewModelFactory(AutenticacaoRepository(AutenticacaoFirebaseDataSource()))
        ).get(MainViewModel::class.java)

        isButtonBackVisible = false
        isButtonNextVisible = false

        /*adicionarSlide(R.layout.intro_01, true)
        adicionarSlide(R.layout.intro_02, true)
        adicionarSlide(R.layout.intro_03, true)
        adicionarSlide(R.layout.intro_04, true)*/
        adicionarSlide(R.layout.intro_cadastro, false)

    }

    private fun adicionarSlide(idFragment: Int, canGoForward: Boolean) {
        addSlide(
            FragmentSlide.Builder()
                .background(R.color.white)
                .backgroundDark(R.color.black)
                .fragment(idFragment)
                .canGoForward(canGoForward)
                .build()
        )
    }

    override fun onStart() {
        super.onStart()
        lifecycleScope.launch {
            verificarUsuarioLogado()
        }
    }

    fun btCadastrar(view: View){
        startActivity(Intent(applicationContext, CadastrarActivity::class.java))
    }

    fun btEntrar(view: View){
        startActivity(Intent(applicationContext, EntrarActivity::class.java))
    }

    suspend fun verificarUsuarioLogado(){
        if(viewModel.verificarUsuarioLogado()){
            abrirTelaPrincipal()
        }
    }

    private fun abrirTelaPrincipal(){
        startActivity(Intent(application, InicialActivity::class.java))
    }

}