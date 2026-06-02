package com.example.imepacluisaugusto.models

data class Pedido(
    val id: String = "",
    val usuarioId: String = "",
    val enderecoCompleto: String = "",
    val formaPagamento: String = "",
    val valorTotal: Double = 0.0,
    val status: String = "Preparando", // Preparando, Saiu para Entrega, Entregue
    val itens: List<ItemCarrinho> = listOf(),
    val timestamp: Long = System.currentTimeMillis()
)

data class ItemCarrinho(
    val produto: Produto,
    var quantidade: Int
)