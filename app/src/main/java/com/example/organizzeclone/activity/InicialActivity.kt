package com.example.organizzeclone.activity

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.organizzeclone.R
import com.example.organizzeclone.adapter.AdapterMovimentacao
import com.example.organizzeclone.config.ConfiguracaoFirebase
import com.example.organizzeclone.helper.RealtimeDatabase
import com.github.clans.fab.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.ValueEventListener
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener

class InicialActivity : AppCompatActivity() {

    private lateinit var fabDespesa: FloatingActionButton
    private lateinit var fabReceita: FloatingActionButton
    private lateinit var textSaldacao: TextView
    private lateinit var textSaldo: TextView
    private lateinit var calendario: MaterialCalendarView
    private var autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao()
    private lateinit var mesAno: String
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
        adapterMov = AdapterMovimentacao(RealtimeDatabase.listaMovimentacao, application)

        //Configuração do RecyclerView
        recyclerMovimentacao.layoutManager = LinearLayoutManager(this)
        recyclerMovimentacao.setHasFixedSize(true)
        //recyclerMovimentacao.addItemDecoration()
        recyclerMovimentacao.adapter = adapterMov
    }

    override fun onStop(){
        super.onStop()

        RealtimeDatabase.refUsuario.removeEventListener(postListener)
        RealtimeDatabase.refMovimentacao.removeEventListener(postListenerMovimentacao)
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
        fabDespesa = findViewById(R.id.menu_despesa)
        fabReceita = findViewById(R.id.menu_receita)
        textSaldacao = findViewById(R.id.textSaldacaoUsuario)
        textSaldo = findViewById(R.id.textViewSaldoTotal)
        recyclerMovimentacao = findViewById(R.id.RecyclerMovimentacao)
        calendario = findViewById(R.id.calendar_View)

        configuracaoCalendario()
        swipe()

    }

    private fun configuracaoCalendario(){
        val meses = arrayOf("Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro")
        calendario.setTitleMonths(meses)

        if (calendario.currentDate.month < 10){
            mesAno = "0"+calendario.currentDate.month.toString()+calendario.currentDate.year
        }else{
            calendario.currentDate.month.toString()+calendario.currentDate.year
        }
        calendario.setOnMonthChangedListener(object : OnMonthChangedListener{
            override fun onMonthChanged(widget: MaterialCalendarView?, date: CalendarDay?) {
                mesAno = if(date?.month!!.toInt() < 10){
                    "0"+date.month+date.year
                }else{
                    date.month.toString()+date.year
                }
                RealtimeDatabase.refMovimentacao.removeEventListener(postListenerMovimentacao)
                recuperarListaMovimentacoes()
            }
        })
    }

    private fun recuperaResumo(){
        postListener = RealtimeDatabase.recuperarUsuario{
            println("TESTE: recuperaResumo()")
            textSaldacao.text = getString(R.string.saladacao) + " ${RealtimeDatabase.usuario.nome}"
            textSaldo.text = String.format("%.2f", (RealtimeDatabase.usuario.receitaTotal - RealtimeDatabase.usuario.despesaTotal))
        }
    }

    private fun recuperarListaMovimentacoes(){
        postListenerMovimentacao = RealtimeDatabase.recuperarMovimentacoes(mesAno, adapterMov)
    }

    private fun swipe(){

        val itemTouch = object : ItemTouchHelper.Callback(){
            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
                return makeMovementFlags(ItemTouchHelper.ACTION_STATE_IDLE,
                    swipeFlags)
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                excluirMovimentacao(viewHolder)
            }
        }
        ItemTouchHelper(itemTouch).attachToRecyclerView(recyclerMovimentacao)
    }

    private fun excluirMovimentacao(viewHolder: RecyclerView.ViewHolder){

        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle(getString(R.string.alertDialogTitulo))
        alertDialog.setMessage(getString(R.string.alertDialogMensagem))
        alertDialog.setCancelable(false)
        alertDialog.setPositiveButton("Confirmar", object : DialogInterface.OnClickListener {
            override fun onClick(p0: DialogInterface?, p1: Int) {
                val posicao = viewHolder.adapterPosition
                RealtimeDatabase.movimentacao = RealtimeDatabase.listaMovimentacao[posicao]
                RealtimeDatabase.excluirMovimentacao(mesAno) { adapterMov.notifyItemRemoved(posicao) }
            }
        })
        alertDialog.setNegativeButton("Cancelar", object : DialogInterface.OnClickListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onClick(p0: DialogInterface?, p1: Int) {
                Snackbar.make(
                    findViewById(R.id.tela_inicial),
                    "Cancelado",
                    Snackbar.LENGTH_LONG
                ).show()
                adapterMov.notifyDataSetChanged()
            }
        })

        val alert = alertDialog.create()
        alert.show()
    }

}