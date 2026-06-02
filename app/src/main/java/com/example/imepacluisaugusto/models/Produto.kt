package com.example.imepacluisaugusto.models

data class Produto(
    val id: String,
    val nome: String,
    val descricao: String,
    val preco: Double,
    val categoria: String,
    val imagemResId: Int // Adicionamos este campo para receber o @drawable
)