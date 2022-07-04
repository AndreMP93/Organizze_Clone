package com.example.organizzeclone.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.organizzeclone.R
import com.example.organizzeclone.model.Movimentacao

class AdapterMovimentacao(var lista: ArrayList<Movimentacao>, var contexto: Context): RecyclerView.Adapter<AdapterMovimentacao.ViewHolderMovimentacao>() {

    class ViewHolderMovimentacao(itemView: View): RecyclerView.ViewHolder(itemView){
        var valor: TextView
        var categoria: TextView
        var descricao: TextView
        init {
            valor = itemView.findViewById(R.id.textItemValor)
            categoria = itemView.findViewById(R.id.textItemCategoria)
            descricao = itemView.findViewById(R.id.textItemDescricao)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderMovimentacao {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movimentacao, parent, false)
        return ViewHolderMovimentacao(view)
    }

    override fun onBindViewHolder(holder: ViewHolderMovimentacao, position: Int) {
        val m = lista[position]
        holder.categoria.text = m.categoria
        holder.descricao.text = m.descricao
        holder.valor.text = m.valor.toString()
        when(m.tipo){
            "R"-> holder.valor.setTextColor(ContextCompat.getColor(contexto, R.color.cor_receita))
            "D" -> holder.valor.setTextColor(ContextCompat.getColor(contexto, R.color.cor_despesa))
        }
    }

    override fun getItemCount(): Int {
        return lista.size
    }
}