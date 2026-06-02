package com.example.imepacluisaugusto

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.imepacluisaugusto.utils.CarrinhoManager
import com.google.android.material.button.MaterialButton

class PagamentoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pagamento)
        supportActionBar?.hide()

        val radioGroup = findViewById<RadioGroup>(R.id.radio_group_pagamento)
        val layoutTroco = findViewById<LinearLayout>(R.id.layout_troco)
        val edtTroco = findViewById<EditText>(R.id.edt_troco)
        val btnRevisar = findViewById<MaterialButton>(R.id.btn_revisar_pedido)

        // Mostrar campo de troco apenas se for Dinheiro
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.radio_dinheiro) {
                layoutTroco.visibility = View.VISIBLE
            } else {
                layoutTroco.visibility = View.GONE
                edtTroco.text.clear()
            }
        }

        btnRevisar.setOnClickListener {
            val selecionadoId = radioGroup.checkedRadioButtonId
            if (selecionadoId == -1) {
                Toast.makeText(this, "Selecione uma forma de pagamento", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val radioSelecionado = findViewById<RadioButton>(selecionadoId)
            CarrinhoManager.formaPagamentoSelecionada = radioSelecionado.text.toString()

            if (selecionadoId == R.id.radio_dinheiro && edtTroco.text.isNotEmpty()) {
                CarrinhoManager.trocoPara = " (Troco para R$ ${edtTroco.text})"
            }

            // Vai para a última etapa!
            startActivity(Intent(this, CheckoutActivity::class.java))
        }
    }
}