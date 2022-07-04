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
import com.example.organizzeclone.databinding.ActivityDespesaBinding
import com.example.organizzeclone.helper.DateUtil
import com.example.organizzeclone.model.Movimentacao
import com.example.organizzeclone.viewmodel.despesa.DespesaViewModel
import com.example.organizzeclone.viewmodel.despesa.DespesaViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class DespesaActivity : AppCompatActivity() {

    private lateinit var viewModel: DespesaViewModel
    private lateinit var binding: ActivityDespesaBinding

    private lateinit var campoValor: EditText
    private lateinit var campoData: EditText
    private lateinit var campoCategoria: EditText
    private lateinit var campoDescricao: EditText
    private lateinit var fabSalvar: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityDespesaBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        viewModel = ViewModelProvider(
            this,
            DespesaViewModelFactory(
                DataBaseRepository(RealtimeDatabeseFirebaseDataSource()),
                AutenticacaoRepository(AutenticacaoFirebaseDataSource())
            )
        ).get(DespesaViewModel::class.java)

        incializarVariaveos()


        fabSalvar.setOnClickListener {
            if(validarCamposDespesa()) {
                val movimentacao = Movimentacao(
                    campoValor.text.toString().toDouble(),
                    campoData.text.toString(),
                    campoCategoria.text.toString(),
                    campoDescricao.text.toString(),
                    "D"
                )
                val mesAno = DateUtil.mesAnoData(campoData.text.toString())
                salvarDespesa(mesAno, movimentacao)
            }
        }


    }

    private fun incializarVariaveos(){
        campoValor = binding.editTextValorDespesa
        campoData = binding.editTextDataDespesa
        campoCategoria = binding.editTextCategoriaDespesa
        campoDescricao = binding.editTextDescricaoDespesa
        fabSalvar = binding.fabSalvarDespesa
        campoData.setText(DateUtil.dataAtual())
    }

    private fun salvarDespesa(mesAno: String, movimentacao: Movimentacao){
        lifecycleScope.launch {
            try {
                viewModel.cadastrarMovimentcao(mesAno, movimentacao)
                finish()
            }catch (e:Exception){
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
                findViewById(R.id.telaDespesa),
                "O Valor da despesa não foi preenchido",
                Snackbar.LENGTH_LONG
            ).show()
            return false
        }else{
            if(campoData.text.isEmpty()){
                Snackbar.make(
                    findViewById(R.id.telaDespesa),
                    "A data da despesa não foi preenchido",
                    Snackbar.LENGTH_LONG
                ).show()
                return false
            }
        }
        return true
    }
}