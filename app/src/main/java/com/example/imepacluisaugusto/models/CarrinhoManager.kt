package com.example.imepacluisaugusto.utils

import com.example.imepacluisaugusto.models.ItemCarrinho
import com.example.imepacluisaugusto.models.Produto

object CarrinhoManager {
    val itens = mutableListOf<ItemCarrinho>()

    // Variáveis temporárias para o funil de checkout
    var enderecoSelecionado: String = ""
    var formaPagamentoSelecionada: String = ""
    var trocoPara: String = ""

    fun adicionarProduto(produto: Produto, quantidade: Int) {
        val itemExistente = itens.find { it.produto.id == produto.id }
        if (itemExistente != null) {
            itemExistente.quantidade += quantidade
        } else {
            itens.add(ItemCarrinho(produto, quantidade))
        }
    }

    fun removerProduto(produtoId: String) {
        val item = itens.find { it.produto.id == produtoId }
        if (item != null) {
            if (item.quantidade > 1) {
                item.quantidade -= 1
            } else {
                itens.remove(item)
            }
        }
    }

    fun calcularTotal(): Double = itens.sumOf { it.produto.preco * it.quantidade }

    fun limparCarrinho() {
        itens.clear()
        enderecoSelecionado = ""
        formaPagamentoSelecionada = ""
        trocoPara = ""
    }
}