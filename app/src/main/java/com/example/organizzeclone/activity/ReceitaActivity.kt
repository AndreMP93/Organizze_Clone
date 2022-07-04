package com.example.organizzeclone.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.organizzeclone.R
import com.example.organizzeclone.data.autenticacao.AutenticacaoFirebaseDataSource
import com.example.organizzeclone.data.autenticacao.AutenticacaoRepository
import com.example.organizzeclone.data.database.DataBaseRepository
import com.example.organizzeclone.data.database.RealtimeDatabeseFirebaseDataSource
import com.example.organizzeclone.databinding.ActivityReceitaBinding
import com.example.organizzeclone.helper.DateUtil
import com.example.organizzeclone.model.Movimentacao
import com.example.organizzeclone.viewmodel.receita.ReceitaViewModel
import com.example.organizzeclone.viewmodel.receita.ReceitaViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class ReceitaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReceitaBinding
    private lateinit var viewModel: ReceitaViewModel

    private lateinit var campoValor: EditText
    private lateinit var campoData: EditText
    private lateinit var campoCategoria: EditText
    private lateinit var campoDescricao: EditText
    private lateinit var fabSalvar: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReceitaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(
            this,
            ReceitaViewModelFactory(
                DataBaseRepository(RealtimeDatabeseFirebaseDataSource()),
                AutenticacaoRepository(AutenticacaoFirebaseDataSource())
            )
        ).get(ReceitaViewModel::class.java)

        incializarVariaveos()

        fabSalvar.setOnClickListener {
            if(validarCamposDespesa()){
                val movimentacao = Movimentacao(
                    campoValor.text.toString().toDouble(),
                    campoData.text.toString(),
                    campoCategoria.text.toString(),
                    campoDescricao.text.toString(),
                    "R"
                )
                val mesAno = DateUtil.mesAnoData(campoData.text.toString())
                salvarReceita(mesAno, movimentacao)
            }
        }
    }

    private fun incializarVariaveos(){
        campoValor = binding.editTextValorReceita
        campoData = binding.editTextDataReceita
        campoCategoria = binding.editTextCategoriaReceita
        campoDescricao = binding.editTextDescricaoReceita
        fabSalvar = binding.fabSalvarReceita
        campoData.setText(DateUtil.dataAtual())
    }

    private fun salvarReceita(mesAno: String, movimentacao: Movimentacao){
        lifecycleScope.launch {
            try {
                viewModel.cadastrarMovimentcao(mesAno, movimentacao)
                finish()
            }catch (e: Exception){
                e.message?.let { exibirSnackbar(it) }
            }
        }
    }

    private fun exibirSnackbar(menssagem: String){
        Snackbar.make(binding.root,
            menssagem,
            Snackbar.LENGTH_LONG).show()
    }

    private fun validarCamposDespesa(): Boolean{
        if (campoValor.text.isEmpty()){
            Snackbar.make(
                findViewById(R.id.telaReceita),
                "O Valor da despesa não foi preenchido",
                Snackbar.LENGTH_LONG
            ).show()
            return false
        }else{
            if(campoData.text.isEmpty()){
                Snackbar.make(
                    findViewById(R.id.telaReceita),
                    "A data da despesa não foi preenchido",
                    Snackbar.LENGTH_LONG
                ).show()
                return false
            }
        }
        return true
    }

}
