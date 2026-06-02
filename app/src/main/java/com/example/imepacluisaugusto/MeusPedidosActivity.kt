package com.example.imepacluisaugusto

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.imepacluisaugusto.adapters.PedidoAdapter
import com.example.imepacluisaugusto.adapters.PedidoResumo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.math.ceil

class MeusPedidosActivity : AppCompatActivity() {

    private lateinit var recyclerPedidos: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var layoutVazio: LinearLayout
    private lateinit var layoutPaginacao: LinearLayout
    private lateinit var adapter: PedidoAdapter

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val limitePagina = 5

    private var paginaAtual = 1
    private var totalPaginas = 1

    private val todosPedidos = mutableListOf<PedidoResumo>()
    private val pedidosPaginaAtual = mutableListOf<PedidoResumo>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meus_pedidos)

        supportActionBar?.hide()

        findViewById<View>(R.id.btn_voltar).setOnClickListener {
            finish()
        }

        recyclerPedidos = findViewById(R.id.recycler_pedidos)
        progressBar = findViewById(R.id.progress_bar)
        layoutVazio = findViewById(R.id.layout_vazio)
        layoutPaginacao = findViewById(R.id.layout_paginacao)

        adapter = PedidoAdapter(pedidosPaginaAtual)

        recyclerPedidos.layoutManager = LinearLayoutManager(this)
        recyclerPedidos.adapter = adapter

        buscarPedidos()
    }

    private fun buscarPedidos() {

        val usuarioAtual = auth.currentUser

        if (usuarioAtual == null) {
            Toast.makeText(
                this,
                "Usuário não autenticado",
                Toast.LENGTH_SHORT
            ).show()

            finish()
            return
        }

        progressBar.visibility = View.VISIBLE

        db.collection("pedidos")
            .whereEqualTo("usuarioId", usuarioAtual.uid)
            .get()
            .addOnSuccessListener { documentos ->

                progressBar.visibility = View.GONE

                if (documentos.isEmpty) {

                    layoutVazio.visibility = View.VISIBLE
                    recyclerPedidos.visibility = View.GONE
                    layoutPaginacao.visibility = View.GONE

                    return@addOnSuccessListener
                }

                todosPedidos.clear()

                for (doc in documentos) {

                    val status =
                        doc.getString("status") ?: "Em andamento"

                    val total =
                        doc.getDouble("valorTotal") ?: 0.0

                    var itensTexto = ""

                    val itensArray =
                        doc.get("itens")
                                as? List<HashMap<String, Any>>

                    itensArray?.forEach { mapaItem ->

                        val qtd = mapItemToQtd(mapaItem)
                        val nome = mapItemToNome(mapaItem)

                        itensTexto += "${qtd}x $nome\n"
                    }

                    todosPedidos.add(
                        PedidoResumo(
                            doc.id,
                            status,
                            total,
                            itensTexto
                        )
                    )
                }

                totalPaginas =
                    ceil(
                        todosPedidos.size.toDouble() /
                                limitePagina.toDouble()
                    ).toInt()

                if (totalPaginas < 1) {
                    totalPaginas = 1
                }

                criarBotoesPaginacao()

                carregarPagina(1)

                layoutVazio.visibility = View.GONE
                recyclerPedidos.visibility = View.VISIBLE
                layoutPaginacao.visibility = View.VISIBLE
            }
            .addOnFailureListener {

                progressBar.visibility = View.GONE

                Toast.makeText(
                    this,
                    "Erro ao carregar pedidos.",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun carregarPagina(numeroPagina: Int) {

        paginaAtual = numeroPagina

        pedidosPaginaAtual.clear()

        val inicio =
            (numeroPagina - 1) * limitePagina

        var fim =
            inicio + limitePagina

        if (fim > todosPedidos.size) {
            fim = todosPedidos.size
        }

        pedidosPaginaAtual.addAll(
            todosPedidos.subList(inicio, fim)
        )

        adapter.atualizarLista(pedidosPaginaAtual)

        atualizarEstiloBotoes()
    }

    private fun criarBotoesPaginacao() {

        layoutPaginacao.removeAllViews()

        for (i in 1..totalPaginas) {

            val btn = Button(this)

            btn.text = i.toString()

            val params =
                LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )

            params.marginEnd = 12

            btn.layoutParams = params

            btn.setOnClickListener {
                carregarPagina(i)
            }

            layoutPaginacao.addView(btn)
        }
    }

    private fun atualizarEstiloBotoes() {

        for (i in 0 until layoutPaginacao.childCount) {

            val btn =
                layoutPaginacao.getChildAt(i) as Button

            if ((i + 1) == paginaAtual) {

                btn.setBackgroundColor(
                    Color.parseColor("#D32F2F")
                )

                btn.setTextColor(Color.WHITE)

            } else {

                btn.setBackgroundColor(
                    Color.parseColor("#E0E0E0")
                )

                btn.setTextColor(Color.BLACK)
            }
        }
    }

    private fun mapItemToQtd(
        mapaItem: HashMap<String, Any>
    ): Long {

        return try {

            (mapaItem["quantidade"] as? Number)
                ?.toLong() ?: 1L

        } catch (e: Exception) {

            1L
        }
    }

    private fun mapItemToNome(
        mapaItem: HashMap<String, Any>
    ): String {

        return try {

            val produto =
                mapaItem["produto"]
                        as? HashMap<String, Any>

            produto?.get("nome")
                    as? String
                ?: "Item Desconhecido"

        } catch (e: Exception) {

            "Item Desconhecido"
        }
    }
}