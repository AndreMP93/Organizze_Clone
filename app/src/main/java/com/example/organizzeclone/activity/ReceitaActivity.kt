package com.example.organizzeclone.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import com.example.organizzeclone.R
import com.example.organizzeclone.config.ConfiguracaoFirebase
import com.example.organizzeclone.helper.DateUtil
import com.example.organizzeclone.model.Movimentacao
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue

class ReceitaActivity : AppCompatActivity() {

    private lateinit var campoValor: EditText
    private lateinit var campoData: EditText
    private lateinit var campoCategoria: EditText
    private lateinit var campoDescricao: EditText
    private lateinit var fabSalvar: FloatingActionButton
    private var userReferencia = ConfiguracaoFirebase.getFirebaseDatabase().child("usuario")
    private var autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao()
    private var receitaTotal = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receita)

        incializarVariaveos()
        recuperarReceita()

        fabSalvar.setOnClickListener {
            if(validarCamposDespesa()){
                salvarReceita()
                finish()
            }
        }

    }

    private fun incializarVariaveos(){
        campoValor = findViewById(R.id.editTextValorReceita)
        campoData = findViewById(R.id.editTextDataReceita)
        campoCategoria = findViewById(R.id.editTextCategoriaReceita)
        campoDescricao = findViewById(R.id.editTextDescricaoReceita)
        fabSalvar = findViewById(R.id.fabSalvarReceita)
        campoData.setText(DateUtil.dataAtual())
    }

    private fun salvarReceita(){
        val receitaGerada = campoValor.text.toString().toDouble()
        val movimentacao = Movimentacao( receitaGerada,
                                        campoData.text.toString(),
                                        campoCategoria.text.toString(),
                                        campoDescricao.text.toString(),
                                        "R")
        val receitaAtualizada = receitaTotal + receitaGerada
        movimentacao.salvarBD()
        println("TESTE: a= ${receitaAtualizada} - T= ${receitaTotal} + G = ${receitaGerada}")
        atualizarReceitaTotal(receitaAtualizada)

    }

    private fun atualizarReceitaTotal(novaReceita: Double){
        val idUsuario = autenticacao.currentUser?.email?.replace(".", "")
        userReferencia.child(idUsuario.toString()).child("receitaTotal").setValue(novaReceita)

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

    private fun recuperarReceita(){
        val idUsuario = autenticacao.currentUser?.email?.replace(".", "")
        val postListener = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                receitaTotal = snapshot.getValue<Double>()!!
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("ERRO", "Erro ao tentar acessar o Firebase")
            }

        }
        userReferencia.child(idUsuario.toString()).child("receitaTotal").addValueEventListener(postListener)


    }

}
