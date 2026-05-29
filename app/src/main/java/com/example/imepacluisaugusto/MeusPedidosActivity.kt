package com.example.imepacluisaugusto

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MeusPedidosActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meus_pedidos)
        supportActionBar?.hide() // Esconde a barra roxa do topo
    }
}