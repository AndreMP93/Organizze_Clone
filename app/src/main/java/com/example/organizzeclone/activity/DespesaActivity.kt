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

class DespesaActivity : AppCompatActivity() {

    private lateinit var campoValor: EditText
    private lateinit var campoData: EditText
    private lateinit var campoCategoria: EditText
    private lateinit var campoDescricao: EditText
    private lateinit var fabSalvar: FloatingActionButton
    private lateinit var movimentacao: Movimentacao
    private var usuarioReferencia = ConfiguracaoFirebase.getFirebaseDatabase().child("usuario")
    private var autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao()
    private var despesaTotal: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_despesa)

        incializarVariaveos()
        recuperarDespesaTotal()


        fabSalvar.setOnClickListener {
            if(validarCamposDespesa()) {
                salvarDespesa()
                finish()
            }
        }


    }

    private fun incializarVariaveos(){
        campoValor = findViewById(R.id.editTextValorDespesa)
        campoData = findViewById(R.id.editTextDataDespesa)
        campoCategoria = findViewById(R.id.editTextCategoriaDespesa)
        campoDescricao = findViewById(R.id.editTextDescricaoDespesa)
        fabSalvar = findViewById(R.id.fabSalvarDespesa)
        campoData.setText(DateUtil.dataAtual())
    }

    private fun salvarDespesa(){
        val despesaGerada = campoValor.text.toString().toDouble()
        movimentacao = Movimentacao(despesaGerada,
                                    campoData.text.toString(),
                                    campoCategoria.text.toString(),
                                    campoDescricao.text.toString(),
                                    "D")

        val despesaAtualizada = despesaTotal + despesaGerada
        movimentacao.salvarBD()
        atualizarDespesa(despesaAtualizada)
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

    private fun atualizarDespesa(despesaAtualizada: Double){
        val idUsuario = autenticacao.currentUser?.email?.replace(".", "")
        usuarioReferencia.child(idUsuario.toString()).child("despesaTotal").setValue(despesaAtualizada)


    }

    private fun recuperarDespesaTotal(){
        val idUsuario = autenticacao.currentUser?.email?.replace(".", "")
        val postListener = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                despesaTotal = snapshot.getValue<Double>()!!
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("TAG", "loadPost:onCancelled", error.toException())
            }
        }

        usuarioReferencia.child(idUsuario.toString()).child("despesaTotal").addValueEventListener(postListener)
    }
}