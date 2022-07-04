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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.organizzeclone.R
import com.example.organizzeclone.adapter.AdapterMovimentacao
import com.example.organizzeclone.data.autenticacao.AutenticacaoFirebaseDataSource
import com.example.organizzeclone.data.autenticacao.AutenticacaoRepository
import com.example.organizzeclone.data.database.DataBaseRepository
import com.example.organizzeclone.data.database.RealtimeDatabeseFirebaseDataSource
import com.example.organizzeclone.databinding.ActivityInicialBinding
import com.example.organizzeclone.viewmodel.inicial.InicialViewModel
import com.example.organizzeclone.viewmodel.inicial.InicialViewModelFactory
import com.github.clans.fab.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener

class InicialActivity : AppCompatActivity() {

    private lateinit var viewModel: InicialViewModel
    private lateinit var binding: ActivityInicialBinding

    private lateinit var fabDespesa: FloatingActionButton
    private lateinit var fabReceita: FloatingActionButton
    private lateinit var textSaldacao: TextView
    private lateinit var textSaldo: TextView
    private lateinit var calendario: MaterialCalendarView

    private lateinit var mesAno: String
    private lateinit var recyclerMovimentacao: RecyclerView
    private lateinit var adapterMov: AdapterMovimentacao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInicialBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(
            this,
            InicialViewModelFactory(
                DataBaseRepository(RealtimeDatabeseFirebaseDataSource()),
                AutenticacaoRepository(AutenticacaoFirebaseDataSource())
            )
        ).get(InicialViewModel::class.java)

        inicializarParametros()
        setSupportActionBar(binding.toolbarInicial)

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

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_tela_inicial, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menuSair-> {
                viewModel.signOut()
                finish()
            }

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onStart(){
        super.onStart()
        viewModel.recuperarDadosUsuarioAtual()
        viewModel.recuperarListaDeMovimentacoes(mesAno)


        viewModel.listaMovimentacoes.observe(this, Observer {
            adapterMov = AdapterMovimentacao(
                viewModel.listaMovimentacoes.value!!,
                applicationContext
            )
            //Configuração do RecyclerView
            recyclerMovimentacao.layoutManager = LinearLayoutManager(this)
            recyclerMovimentacao.setHasFixedSize(true)
            //recyclerMovimentacao.addItemDecoration()
            recyclerMovimentacao.adapter = adapterMov
        })

        viewModel.erroManager.observe(this, Observer {
            if(it != ""){
                exibirSnackbar(it)
            }
        })

        viewModel.dadosUsuario.observe(this, Observer {
            exibirResumoDosDadosDoUsuario()
        })
    }


    private fun inicializarParametros(){
        fabDespesa = binding.menuDespesa
        fabReceita = binding.menuReceita
        textSaldacao = binding.textSaldacaoUsuario
        textSaldo = binding.textViewSaldoTotal
        recyclerMovimentacao = binding.RecyclerMovimentacao
        calendario = binding.calendarView

        configuracaoCalendario()
        swipe()
    }

    private fun exibirResumoDosDadosDoUsuario(){
        textSaldo.text = String.format("%.2f", (viewModel.dadosUsuario.value!!.receitaTotal - viewModel.dadosUsuario.value!!.despesaTotal))
        textSaldacao.text = getString(R.string.saldadacao) + " " + viewModel.dadosUsuario.value!!.nome
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
                viewModel.recuperarListaDeMovimentacoes(mesAno)
            }
        })
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
                viewModel.excluirMovimentacao(mesAno, adapterMov.lista[posicao])
                adapterMov.notifyItemRemoved(posicao)
            }
        })
        alertDialog.setNegativeButton("Cancelar", object : DialogInterface.OnClickListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onClick(p0: DialogInterface?, p1: Int) {
                exibirSnackbar("Cancelado")
                adapterMov.notifyDataSetChanged()
            }
        })
        val alert = alertDialog.create()
        alert.show()
    }

    private fun exibirSnackbar(menssagem: String){
        Snackbar.make(
            binding.root,
            menssagem,
            Snackbar.LENGTH_LONG
        ).show()
    }

}