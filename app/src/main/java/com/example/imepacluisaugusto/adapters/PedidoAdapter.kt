package com.example.imepacluisaugusto.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.imepacluisaugusto.R

// Classe simples para transitar dados do Firebase para a Tela
data class PedidoResumo(val id: String, val status: String, val total: Double, val itensDescricao: String)

class PedidoAdapter(private var listaPedidos: List<PedidoResumo>) : RecyclerView.Adapter<PedidoAdapter.PedidoViewHolder>() {

    class PedidoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtStatus: TextView = itemView.findViewById(R.id.txt_status_pedido)
        val txtTotal: TextView = itemView.findViewById(R.id.txt_total_pedido)
        val txtItens: TextView = itemView.findViewById(R.id.txt_itens_pedido)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PedidoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_pedido, parent, false)
        return PedidoViewHolder(view)
    }

    override fun onBindViewHolder(holder: PedidoViewHolder, position: Int) {
        val pedido = listaPedidos[position]

        holder.txtStatus.text = pedido.status
        holder.txtItens.text = pedido.itensDescricao.trim()
        holder.txtTotal.text = String.format("R$ %.2f", pedido.total).replace(".", ",")

        // Muda a cor dependendo do status
        if (pedido.status.lowercase() == "entregue") {
            holder.txtStatus.setTextColor(holder.itemView.context.resources.getColor(android.R.color.darker_gray))
        } else {
            holder.txtStatus.setTextColor(holder.itemView.context.resources.getColor(android.R.color.holo_green_dark))
        }
    }

    override fun getItemCount(): Int = listaPedidos.size

    fun atualizarLista(novaLista: List<PedidoResumo>) {
        listaPedidos = novaLista
        notifyDataSetChanged()
    }
}