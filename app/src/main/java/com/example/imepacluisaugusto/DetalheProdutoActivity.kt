package com.example.imepacluisaugusto

import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.imepacluisaugusto.models.Produto
import com.example.imepacluisaugusto.utils.CarrinhoManager
import com.google.android.material.button.MaterialButton

class DetalheProdutoActivity : AppCompatActivity() {

    private var quantidade = 1
    private var precoUnitario = 0.0
    private lateinit var produtoAtual: Produto

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalhe_produto)
        supportActionBar?.hide()

        val id = intent.getStringExtra("id") ?: ""
        val nome = intent.getStringExtra("nome") ?: "Produto"
        val descricao = intent.getStringExtra("descricao") ?: ""
        val categoria = intent.getStringExtra("categoria") ?: ""
        precoUnitario = intent.getDoubleExtra("preco", 0.0)

        // NOVO: Recebendo o ID da imagem (se falhar, usa a logo como fallback)
        val imagemResId = intent.getIntExtra("imagemResId", R.drawable.logo)

        // NOVO: Passando o imagemResId no lugar da string vazia ""
        produtoAtual = Produto(id, nome, descricao, precoUnitario, categoria, imagemResId)

        val txtNome = findViewById<TextView>(R.id.txt_detalhe_nome)
        val txtDesc = findViewById<TextView>(R.id.txt_detalhe_desc)
        val txtPreco = findViewById<TextView>(R.id.txt_detalhe_preco)
        val txtQuantidade = findViewById<TextView>(R.id.txt_quantidade)
        val btnMenos = findViewById<ImageButton>(R.id.btn_menos)
        val btnMais = findViewById<ImageButton>(R.id.btn_mais)
        val btnAddCarrinho = findViewById<MaterialButton>(R.id.btn_add_carrinho)

        // Opcional: Se você tiver um ImageView na tela de detalhes (ex: R.id.img_produto_detalhe),
        // a imagem do produto será carregada nele também:
        val imgDetalhe = findViewById<ImageView?>(R.id.img_produto)
        imgDetalhe?.setImageResource(imagemResId)

        txtNome.text = nome
        txtDesc.text = descricao
        txtPreco.text = formatarMoeda(precoUnitario)
        atualizarBotaoAdd(btnAddCarrinho)

        btnMais.setOnClickListener {
            quantidade++
            txtQuantidade.text = quantidade.toString()
            atualizarBotaoAdd(btnAddCarrinho)
        }

        btnMenos.setOnClickListener {
            if (quantidade > 1) {
                quantidade--
                txtQuantidade.text = quantidade.toString()
                atualizarBotaoAdd(btnAddCarrinho)
            }
        }

        btnAddCarrinho.setOnClickListener {
            CarrinhoManager.adicionarProduto(produtoAtual, quantidade)
            finish()
        }
    }

    private fun atualizarBotaoAdd(botao: MaterialButton) {
        val total = precoUnitario * quantidade
        botao.text = "Adicionar • ${formatarMoeda(total)}"
    }

    private fun formatarMoeda(valor: Double): String {
        return String.format("R$ %.2f", valor).replace(".", ",")
    }
}