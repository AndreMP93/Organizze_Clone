package com.example.organizzeclone.activity


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.organizzeclone.data.autenticacao.AutenticacaoFirebaseDataSource
import com.example.organizzeclone.data.autenticacao.AutenticacaoRepository
import com.example.organizzeclone.databinding.ActivityEntrarBinding
import com.example.organizzeclone.viewmodel.entrar.EntrarViewModel
import com.example.organizzeclone.viewmodel.entrar.EntrarViewModelFactory
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.*
import kotlinx.coroutines.launch

class EntrarActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEntrarBinding
    private lateinit var viewModel: EntrarViewModel
    private lateinit var autenticacao: FirebaseAuth
    private lateinit var excecao: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEntrarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(
            this,
            EntrarViewModelFactory(AutenticacaoRepository(AutenticacaoFirebaseDataSource()))
        )
            .get(EntrarViewModel::class.java)


        binding.buttonEntrar.setOnClickListener {
            val email = binding.editTextEntrarEmail.text.toString()
            val senha = binding.editTextEntrarSenha.text.toString()

            if(email.isNotEmpty() and senha.isNotEmpty()
            ){
                validarLogin(email, senha)
            }else{
                exibirSnackbar("Preencha todos os campos")
            }
        }
    }


    private fun validarLogin(email: String, senha: String){
        lifecycleScope.launch {
            try {
                if (viewModel.validarLogin(email, senha)){
                    finish()
                }
            }catch (e: Exception){
                when(e){
                    is FirebaseAuthInvalidUserException ->
                        exibirSnackbar("Usuáruio não está cadastrado")
                    is FirebaseAuthInvalidCredentialsException ->
                        exibirSnackbar("Senha não correspondem ao usuário cadastrado")
                    else ->
                        exibirSnackbar("Erro ao fazer o login do usuario ${e.message}")
                }

            }
        }
    }
    private fun exibirSnackbar(menssagem: String){
        Snackbar.make(binding.root,
            menssagem,
            Snackbar.LENGTH_LONG).show()
    }

}
