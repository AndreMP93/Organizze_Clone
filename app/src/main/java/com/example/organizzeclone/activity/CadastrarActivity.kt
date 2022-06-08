package com.example.organizzeclone.activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.organizzeclone.R
import com.example.organizzeclone.model.Usuario
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class CadastrarActivity : AppCompatActivity() {

    private lateinit var campoNome: EditText
    private lateinit var campoEmail: EditText
    private lateinit var campoSenha: EditText
    private lateinit var botaoCadastrar: Button
    private lateinit var autenticacao: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastrar)

        inicializarVariaveis()

        botaoCadastrar.setOnClickListener {
            val usuario = Usuario(campoNome.text.toString(), campoEmail.text.toString(), campoSenha.text.toString())
            if(campoNome.text.isNotEmpty() and campoEmail.text.isNotEmpty() and campoSenha.text.isNotEmpty() ){

                cadastrarUsusario(usuario)
            }else{
                println("TESTE: CAMPOS VAZIOS")
                Snackbar.make(findViewById(R.id.telaCadastro),
                    "Preencha todos os campos",
                    Snackbar.LENGTH_LONG).show()
            }
        }


    }

    private fun inicializarVariaveis(){
        campoNome = findViewById(R.id.editTextCadastroNome)
        campoEmail = findViewById(R.id.editTextCadastroEmail)
        campoSenha = findViewById(R.id.editTextCadastroSenha)
        botaoCadastrar = findViewById(R.id.buttonCadastrar)
    }


    private fun cadastrarUsusario(usuario: Usuario){
        autenticacao = Firebase.auth
        autenticacao.createUserWithEmailAndPassword(
            usuario.email,
            usuario.senha
        ).addOnCompleteListener{ task ->
            if(task.isSuccessful){
                finish()
            }else {
                var excecao = " "
                try{
                    throw task.exception!!
                }catch (e: FirebaseAuthWeakPasswordException){

                    excecao = "Digite uma senha mais forte"
                }catch (e: FirebaseAuthInvalidCredentialsException){
                    excecao = "Por favor, digite um email valido"
                }catch (e: FirebaseAuthUserCollisionException){
                    excecao = "Está conta já existe"
                }catch (e: Exception){
                    excecao = "Erro ao cadastrar o usuario ${e.message}"
                    e.printStackTrace()
                }
                Snackbar.make(findViewById(R.id.telaCadastro),
                    excecao,
                    Snackbar.LENGTH_LONG).show()
            }
        }
    }
}
