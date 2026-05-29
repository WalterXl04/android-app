package com.example.imepacluisaugusto

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class CardapioActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cardapio)
        supportActionBar?.hide() // Esconde a barra roxa do topo

        // Encontra os botões de adicionar do layout XML
        val btnAddFrango = findViewById<Button>(R.id.btn_add_frango)
        val btnAddMaionese = findViewById<Button>(R.id.btn_add_maionese)

        // Ação ao clicar no botão do Frango
        btnAddFrango.setOnClickListener {
            Toast.makeText(this, "Frango Assado adicionado ao pedido!", Toast.LENGTH_SHORT).show()
        }

        // Ação ao clicar no botão da Maionese
        btnAddMaionese.setOnClickListener {
            Toast.makeText(this, "Maionese adicionada ao pedido!", Toast.LENGTH_SHORT).show()
        }
    }
}