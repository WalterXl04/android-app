package com.example.imepacluisaugusto

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.imepacluisaugusto.utils.CarrinhoManager
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL

class EnderecoActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private var enderecoPadrao: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_endereco)
        supportActionBar?.hide()

        val txtEnderecoSalvo = findViewById<TextView>(R.id.txt_endereco_salvo)
        val cardEnderecoSalvo = findViewById<MaterialCardView>(R.id.card_endereco_salvo)

        val edtCep = findViewById<EditText>(R.id.edt_cep)
        val edtRua = findViewById<EditText>(R.id.edt_rua)
        val edtBairro = findViewById<EditText>(R.id.edt_bairro)
        val edtCidade = findViewById<EditText>(R.id.edt_cidade)
        val edtEstado = findViewById<EditText>(R.id.edt_estado)
        val edtNumero = findViewById<EditText>(R.id.edt_numero)
        val edtComplemento = findViewById<EditText>(R.id.edt_complemento)
        val checkSalvar = findViewById<CheckBox>(R.id.check_salvar_padrao)

        // 1. Buscar Endereço Salvo no Firestore
        val userId = auth.currentUser?.uid
        if (userId != null) {
            db.collection("Usuarios").document(userId).get().addOnSuccessListener { doc ->
                enderecoPadrao = doc.getString("endereco")
                if (enderecoPadrao.isNullOrEmpty()) {
                    txtEnderecoSalvo.text = "Nenhum endereço salvo."
                    cardEnderecoSalvo.strokeColor = resources.getColor(android.R.color.darker_gray)
                } else {
                    txtEnderecoSalvo.text = enderecoPadrao
                }
            }
        }

        // 2. Ação de clicar no endereço salvo
        cardEnderecoSalvo.setOnClickListener {
            if (!enderecoPadrao.isNullOrEmpty()) {
                CarrinhoManager.enderecoSelecionado = enderecoPadrao!!
                irParaPagamento()
            }
        }

        // 3. Integração ViaCEP
        findViewById<MaterialButton>(R.id.btn_buscar_cep).setOnClickListener {
            val cep = edtCep.text.toString().replace("-", "")
            if (cep.length == 8) {
                buscarCep(cep, edtRua, edtBairro, edtCidade, edtEstado, edtNumero)
            } else {
                Toast.makeText(this, "CEP Inválido", Toast.LENGTH_SHORT).show()
            }
        }

        // 4. Continuar com Novo Endereço
        findViewById<MaterialButton>(R.id.btn_continuar_pagamento).setOnClickListener {
            val rua = edtRua.text.toString()
            val num = edtNumero.text.toString()
            val bairro = edtBairro.text.toString()
            val cidade = edtCidade.text.toString()

            if (rua.isEmpty() || num.isEmpty() || bairro.isEmpty() || cidade.isEmpty()) {
                Toast.makeText(this, "Preencha os campos obrigatórios", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val novoEndereco = "$rua, $num ${edtComplemento.text} - $bairro, $cidade-${edtEstado.text}"
            CarrinhoManager.enderecoSelecionado = novoEndereco

            if (checkSalvar.isChecked && userId != null) {
                db.collection("Usuarios").document(userId).update("endereco", novoEndereco)
            }
            irParaPagamento()
        }
    }

    private fun buscarCep(cep: String, rua: EditText, bairro: EditText, cid: EditText, uf: EditText, num: EditText) {
        // Mostra um feedback visual de que algo está acontecendo
        Toast.makeText(this, "Buscando CEP...", Toast.LENGTH_SHORT).show()

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val resposta = URL("https://viacep.com.br/ws/$cep/json/").readText()
                val json = JSONObject(resposta)

                if (json.has("erro")) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@EnderecoActivity, "CEP não encontrado", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // Atualiza a tela na Thread Principal
                    withContext(Dispatchers.Main) {
                        rua.setText(json.getString("logradouro"))
                        bairro.setText(json.getString("bairro"))
                        cid.setText(json.getString("localidade"))
                        uf.setText(json.getString("uf"))
                        num.requestFocus()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    // Exibe o erro real para sabermos o que aconteceu
                    Toast.makeText(this@EnderecoActivity, "Erro de rede: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun irParaPagamento() {
        startActivity(Intent(this, PagamentoActivity::class.java))
    }
}