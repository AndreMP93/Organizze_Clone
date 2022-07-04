package com.example.organizzeclone.activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.organizzeclone.data.autenticacao.AutenticacaoDataSource
import com.example.organizzeclone.data.autenticacao.AutenticacaoFirebaseDataSource
import com.example.organizzeclone.data.autenticacao.AutenticacaoRepository
import com.example.organizzeclone.data.database.DataBaseDataSource
import com.example.organizzeclone.data.database.DataBaseRepository
import com.example.organizzeclone.data.database.RealtimeDatabeseFirebaseDataSource
import com.example.organizzeclone.databinding.ActivityCadastrarBinding
import com.example.organizzeclone.model.Usuario
import com.example.organizzeclone.viewmodel.cadastrar.CadastrarViewModel
import com.example.organizzeclone.viewmodel.cadastrar.CadastrarViewModelFactory
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.*
import kotlinx.coroutines.launch

class CadastrarActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCadastrarBinding
    private lateinit var viewModel: CadastrarViewModel
    private lateinit var campoNome: EditText
    private lateinit var campoEmail: EditText
    private lateinit var campoSenha: EditText
    private lateinit var botaoCadastrar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCadastrarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dataBaseDataSource: DataBaseDataSource = RealtimeDatabeseFirebaseDataSource()
        val autenticacaoDataSource: AutenticacaoDataSource = AutenticacaoFirebaseDataSource()
        viewModel = ViewModelProvider(
            this,
            CadastrarViewModelFactory(
                DataBaseRepository(dataBaseDataSource),
                AutenticacaoRepository(autenticacaoDataSource)
            )
        ).get(CadastrarViewModel::class.java)

        inicializarVariaveis()

        botaoCadastrar.setOnClickListener {

            if(campoNome.text.isNotEmpty() and campoEmail.text.isNotEmpty() and campoSenha.text.isNotEmpty() ){
                val usuario = Usuario(
                    binding.editTextCadastroNome.text.toString(),
                    binding.editTextCadastroEmail.text.toString(),
                    binding.editTextCadastroSenha.text.toString()
                )
                cadastrarUsusario(usuario)

            }else{
                exibirSnackbar("Preencha todos os campos")
            }
        }


    }

    private fun inicializarVariaveis(){
        campoNome = binding.editTextCadastroNome
        campoEmail = binding.editTextCadastroEmail
        campoSenha = binding.editTextCadastroSenha
        botaoCadastrar = binding.buttonCadastrar
    }

    private fun cadastrarUsusario(usuario: Usuario){
        lifecycleScope.launch {
            try {
                if (viewModel.cadastrarUsusario(usuario)){
                    finish()
                }
            }catch (e: java.lang.Exception){
                when(e){
                    is FirebaseAuthWeakPasswordException ->
                        exibirSnackbar("Digite uma senha mais forte")

                    is FirebaseAuthInvalidCredentialsException ->
                        exibirSnackbar("Por favor, digite um email valido")

                    is FirebaseAuthUserCollisionException ->
                        exibirSnackbar("Está conta já existe")
                    else ->
                        exibirSnackbar("Erro: ao cadastrar novo ususaio")
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
