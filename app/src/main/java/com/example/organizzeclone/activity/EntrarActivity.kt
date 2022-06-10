package com.example.organizzeclone.activity


import com.example.organizzeclone.R
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.organizzeclone.config.ConfiguracaoFirebase
import com.example.organizzeclone.model.Usuario
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class EntrarActivity : AppCompatActivity() {

    private lateinit var campoEmail: EditText
    private lateinit var campoSenha: EditText
    private lateinit var botaoEntrar: Button
    private lateinit var autenticacao: FirebaseAuth
    private lateinit var excecao: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entrar)

        inicializarVariaveis()

        botaoEntrar.setOnClickListener {
            val usuario = Usuario(" ", campoEmail.text.toString(), campoSenha.text.toString())
            if(campoEmail.text.isNotEmpty() and campoSenha.text.isNotEmpty() ){
                println("TESTE: TESTE -> ${usuario.email} ${usuario.senha} !!!")
                validarLogin(usuario)
            }else{
                println("TESTE: CAMPOS VAZIOS")

                Snackbar.make(findViewById(R.id.telaEntrar),
                    "Preencha todos os campos",
                    Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun inicializarVariaveis(){
        campoEmail = findViewById(R.id.editTextEntrarEmail)
        campoSenha = findViewById(R.id.editTextEntrarSenha)
        botaoEntrar = findViewById(R.id.buttonEntrar)
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao()
    }

    private fun validarLogin(usuario: Usuario){
        autenticacao.signInWithEmailAndPassword(usuario.email, usuario.senha).addOnCompleteListener {
                task ->
            if(task.isSuccessful){
                abrirTelaPrincipal()
            }else{
                excecao = " "
                try{
                    throw task.exception!!
                }catch (e: FirebaseAuthInvalidUserException){
                    excecao = "Usuáruio não está cadastrado"
                }catch (e: FirebaseAuthInvalidCredentialsException){
                    excecao = "Senha não correspondem ao usuário cadastrado"
                }catch (e: Exception){
                    excecao = "Erro ao fazer o login do usuario ${e.message}"
                    e.printStackTrace()
                }

                Snackbar.make(findViewById(R.id.telaEntrar),
                    excecao,
                    Snackbar.LENGTH_LONG).show()
                println("TESTE: Erro ao fazer o login")
            }
        }
    }

    private fun abrirTelaPrincipal(){
        startActivity(
            Intent(
                application,
                TesteActivity::class.java
            )
        )
        finish()
    }
}
