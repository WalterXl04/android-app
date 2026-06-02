package com.example.imepacluisaugusto

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.imepacluisaugusto.adapters.ProdutoAdapter
import com.example.imepacluisaugusto.models.Produto
import com.example.imepacluisaugusto.utils.CarrinhoManager

class CardapioActivity : AppCompatActivity() {

    private lateinit var recyclerProdutos: RecyclerView
    private lateinit var adapter: ProdutoAdapter
    private lateinit var cardCarrinho: View
    private lateinit var txtQtdItens: TextView
    private lateinit var txtTotalCarrinho: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cardapio)
        supportActionBar?.hide()

        // ATIVA O BOTÃO DE VOLTAR
        findViewById<View>(R.id.btn_voltar).setOnClickListener {
            finish()
        }

        recyclerProdutos = findViewById(R.id.recycler_produtos)
        cardCarrinho = findViewById(R.id.card_carrinho_flutuante)
        txtQtdItens = findViewById(R.id.txt_qtd_itens_carrinho)
        txtTotalCarrinho = findViewById(R.id.txt_total_carrinho)

        configurarRecyclerView()
        carregarProdutosLocais()
        atualizarCarrinhoFlutuante()

        val btnAbrirCheckout = findViewById<View>(R.id.btn_abrir_checkout)
        btnAbrirCheckout.setOnClickListener {
            startActivity(Intent(this, EnderecoActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        atualizarCarrinhoFlutuante()
    }

    private fun configurarRecyclerView() {
        adapter = ProdutoAdapter(emptyList()) {
            atualizarCarrinhoFlutuante()
        }

        recyclerProdutos.layoutManager = GridLayoutManager(this, 2)
        recyclerProdutos.adapter = adapter
    }

    private fun atualizarCarrinhoFlutuante() {
        val totalItens = CarrinhoManager.itens.sumOf { it.quantidade }
        if (totalItens > 0) {
            cardCarrinho.visibility = View.VISIBLE
            txtQtdItens.text = "$totalItens itens no carrinho"
            txtTotalCarrinho.text = String.format("R$ %.2f", CarrinhoManager.calcularTotal()).replace(".", ",")
        } else {
            cardCarrinho.visibility = View.GONE
        }
    }

    private fun carregarProdutosLocais() {
        val listaMock = listOf(
            Produto("1", "Frango Assado Completo", "Inteiro assado na brasa. Acompanha farofa.", 45.0, "Frangos", R.drawable.frango),
            Produto("2", "Costela no Bafo Premium", "Costela bovina premium assada 12h.", 79.90, "Carnes", R.drawable.costela),
            Produto("3", "Porção de Coxa", "Coxas de frango suculentas.", 35.0, "Acompanhamentos", R.drawable.coxa),
            Produto("4", "Fraldinha Assada", "Fraldinha assada acompanhada de batatas.", 62.0, "Carnes", R.drawable.fraldinha),
            Produto("5", "Coca-Cola 2 Litros", "Bem gelada.", 14.0, "Bebidas", R.drawable.coca)
        )
        adapter.atualizarLista(listaMock)
    }
}