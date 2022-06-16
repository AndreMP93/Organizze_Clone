package com.example.organizzeclone.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.organizzeclone.R
import com.example.organizzeclone.adapter.AdapterMovimentacao
import com.example.organizzeclone.config.ConfiguracaoFirebase
import com.example.organizzeclone.model.Movimentacao
import com.example.organizzeclone.model.Usuario
import com.github.clans.fab.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener

class InicialActivity : AppCompatActivity() {

    private lateinit var fabDespesa: FloatingActionButton
    private lateinit var fabReceita: FloatingActionButton
    private lateinit var textSaldacao: TextView
    private lateinit var textSaldo: TextView
    private lateinit var calendario: MaterialCalendarView
    private var refUsuario = ConfiguracaoFirebase.getFirebaseDatabase().child("usuario")
    private var refMovimentacao = ConfiguracaoFirebase.getFirebaseDatabase().child("movimentacao")
    private var autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao()
    private lateinit var usuario: Usuario
    private lateinit var mesAno: String
    private val listaMovimentacao = ArrayList<Movimentacao>()
    private lateinit var postListener: ValueEventListener
    private lateinit var postListenerMovimentacao: ValueEventListener
    private lateinit var recyclerMovimentacao: RecyclerView
    private lateinit var adapterMov: AdapterMovimentacao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicial)

        inicializarParametros()
        setSupportActionBar(findViewById(R.id.toolbarInicial))

        fabDespesa.setOnClickListener {
            startActivity(Intent(
                applicationContext,
                DespesaActivity::class.java
            ))
        }

        fabReceita.setOnClickListener {
            startActivity(Intent(
                applicationContext,
                ReceitaActivity::class.java
            ))
        }

        //Configuração do Adapter
        adapterMov = AdapterMovimentacao(listaMovimentacao, application)

        //Configuração do RecyclerView
        recyclerMovimentacao.layoutManager = LinearLayoutManager(this)
        recyclerMovimentacao.setHasFixedSize(true)
        //recyclerMovimentacao.addItemDecoration()
        recyclerMovimentacao.adapter = adapterMov
    }

    override fun onStop(){
        super.onStop()

        refUsuario.removeEventListener(postListener)
        refMovimentacao.removeEventListener(postListenerMovimentacao)
    }

    override fun onStart(){
        super.onStart()

        recuperaResumo()
        recuperarListaMovimentacoes()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_tela_inicial, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menuSair-> {autenticacao.signOut()
                finish()}

        }
        return super.onOptionsItemSelected(item)
    }

    private fun inicializarParametros(){
        calendario = findViewById(R.id.calendar_View)
        fabDespesa = findViewById(R.id.menu_despesa)
        fabReceita = findViewById(R.id.menu_receita)
        textSaldacao = findViewById(R.id.textSaldacaoUsuario)
        textSaldo = findViewById(R.id.textViewSaldoTotal)
        recyclerMovimentacao = findViewById(R.id.RecyclerMovimentacao)

        val meses = arrayOf("Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro")
        calendario.setTitleMonths(meses)
        if (calendario.currentDate.month < 10){
            mesAno = "0"+calendario.currentDate.month.toString()+calendario.currentDate.year
        }else{
            calendario.currentDate.month.toString()+calendario.currentDate.year
        }
        println("TESTE: mesano=${mesAno}")
        calendario.setOnMonthChangedListener(object : OnMonthChangedListener{
            override fun onMonthChanged(widget: MaterialCalendarView?, date: CalendarDay?) {
                if(date?.month!!.toInt() < 10){
                    mesAno = "0"+date.month+date.year
                }else{
                    mesAno = date.month.toString()+date.year
                }
                refMovimentacao.removeEventListener(postListenerMovimentacao)
                recuperarListaMovimentacoes()
            }
        })
    }

    private fun recuperaResumo(){
        val idUsuario = autenticacao.currentUser?.email.toString().replace(".", "")
        postListener = refUsuario.child(idUsuario).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                usuario = snapshot.getValue<Usuario>()!!
                textSaldacao.text = getString(R.string.saladacao) + " ${usuario.nome}"
                textSaldo.text = String.format("%.2f", (usuario.receitaTotal - usuario.despesaTotal))
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("Erro Firebase", "Erro ao acessar o Firebase", error.toException())
            }
        })
    }



    private fun recuperarListaMovimentacoes(){
        listaMovimentacao.clear()
        val idUsuario = autenticacao.currentUser?.email.toString().replace(".", "")
        postListenerMovimentacao = refMovimentacao.child(idUsuario).child(mesAno).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(dados in snapshot.children){
                    var m = dados.getValue<Movimentacao>()!!
                    println("TESTE: ${m.valor}")
                    listaMovimentacao.add(m)
                }
                adapterMov.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("Erro Firebase", "Erro ao acessar o Firebase", error.toException())
            }
        })
    }

}