package com.example.imepacluisaugusto.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.imepacluisaugusto.DetalheProdutoActivity
import com.example.imepacluisaugusto.R
import com.example.imepacluisaugusto.models.Produto
import com.example.imepacluisaugusto.utils.CarrinhoManager
import com.google.android.material.button.MaterialButton

class ProdutoAdapter(
    private var listaProdutos: List<Produto>,
    private val onCarrinhoAtualizado: () -> Unit
) : RecyclerView.Adapter<ProdutoAdapter.ProdutoViewHolder>() {

    class ProdutoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgProduto: ImageView = itemView.findViewById(R.id.img_produto)
        val txtNome: TextView = itemView.findViewById(R.id.txt_nome_produto)
        val txtDesc: TextView = itemView.findViewById(R.id.txt_desc_produto)
        val txtPreco: TextView = itemView.findViewById(R.id.txt_preco_produto)
        val txtQtd: TextView = itemView.findViewById(R.id.txt_qtd)
        val btnRemover: MaterialButton = itemView.findViewById(R.id.btn_remover)
        val btnAdicionar: MaterialButton = itemView.findViewById(R.id.btn_adicionar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProdutoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_produto, parent, false)
        return ProdutoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProdutoViewHolder, position: Int) {
        val produto = listaProdutos[position]

        val itemCarrinho = CarrinhoManager.itens.find { it.produto.id == produto.id }
        val qtdAtual = itemCarrinho?.quantidade ?: 0

        holder.txtNome.text = produto.nome
        holder.txtDesc.text = produto.descricao
        holder.txtPreco.text = String.format("R$ %.2f", produto.preco).replace(".", ",")
        holder.txtQtd.text = qtdAtual.toString()

        // NOVO: Carrega a imagem do drawable dinamicamente no Cardápio!
        holder.imgProduto.setImageResource(produto.imagemResId)

        holder.btnAdicionar.setOnClickListener {
            CarrinhoManager.adicionarProduto(produto, 1)
            notifyItemChanged(position)
            onCarrinhoAtualizado()
        }

        holder.btnRemover.setOnClickListener {
            if (qtdAtual > 0) {
                CarrinhoManager.removerProduto(produto.id)
                notifyItemChanged(position)
                onCarrinhoAtualizado()
            }
        }

        // NOVO: Clique no card para abrir os Detalhes enviando a imagem
        holder.itemView.setOnClickListener {
            val contexto = holder.itemView.context
            val intent = Intent(contexto, DetalheProdutoActivity::class.java).apply {
                putExtra("id", produto.id)
                putExtra("nome", produto.nome)
                putExtra("descricao", produto.descricao)
                putExtra("categoria", produto.categoria)
                putExtra("preco", produto.preco)
                putExtra("imagemResId", produto.imagemResId) // Passando a imagem!
            }
            contexto.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = listaProdutos.size

    fun atualizarLista(novaLista: List<Produto>) {
        listaProdutos = novaLista
        notifyDataSetChanged()
    }
}