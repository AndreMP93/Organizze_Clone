package com.example.organizzeclone.helper

import android.annotation.SuppressLint
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.example.organizzeclone.R
import com.example.organizzeclone.adapter.AdapterMovimentacao
import com.example.organizzeclone.config.ConfiguracaoFirebase
import com.example.organizzeclone.model.Movimentacao
import com.example.organizzeclone.model.Usuario
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue

class RealtimeDatabase {

    companion object{
        const val chaveUsuario = "usuario"
        const val chaveMovimentacao = "movimentacao"
        var autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao()
        var idUsuario = autenticacao.currentUser?.email.toString().replace(".", "")
        var refUsuario = ConfiguracaoFirebase.getFirebaseDatabase().child("usuario")
        lateinit var usuario: Usuario
        var refMovimentacao = ConfiguracaoFirebase.getFirebaseDatabase().child("movimentacao")
        lateinit var movimentacao: Movimentacao
        val listaMovimentacao = ArrayList<Movimentacao>()


        fun recuperarUsuario(funcao: ()-> Unit): ValueEventListener{
            return refUsuario.child(idUsuario).addValueEventListener(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    usuario = snapshot.getValue<Usuario>()!!
                    funcao()
                }
                override fun onCancelled(error: DatabaseError) {
                    Log.w("Erro Firebase", "Erro ao acessar o Firebase", error.toException())
                }
            })
        }

        fun recuperarMovimentacoes(mesAno: String, adapterMov: AdapterMovimentacao): ValueEventListener{
            return refMovimentacao.child(idUsuario).child(mesAno).addValueEventListener(object : ValueEventListener{
                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(snapshot: DataSnapshot) {
                    listaMovimentacao.clear()
                    for(dados in snapshot.children){
                        val m = dados.getValue<Movimentacao>()!!
                        m.id = dados.key.toString()
                        listaMovimentacao.add(m)
                    }
                    adapterMov.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w("Erro Firebase", "Erro ao acessar o Firebase", error.toException())
                }
            })
        }

        fun excluirMovimentacao( mesAno: String, funcao: ()-> Unit){

            refMovimentacao.child(idUsuario).child(mesAno).child(movimentacao.id).removeValue()
            funcao()
            if(movimentacao.tipo == "R"){
                refUsuario.child(idUsuario).child("receitaTotal").setValue(usuario.receitaTotal - movimentacao.valor)
            }else{
                refUsuario.child(idUsuario).child("despesaTotal").setValue(usuario.despesaTotal - movimentacao.valor)
            }
        }


    }
}