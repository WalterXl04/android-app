package com.example.imepacluisaugusto

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        supportActionBar?.hide()

        val textBemVindo = findViewById<TextView>(R.id.text_bem_vindo)
        val textEmailUsuario = findViewById<TextView>(R.id.text_email_usuario)

        val btnCardapio = findViewById<Button>(R.id.btn_cardapio)
        val btnMeusPedidos = findViewById<Button>(R.id.btn_meus_pedidos)
        val btnSair = findViewById<Button>(R.id.btn_sair)

        val db = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()
        val usuarioAtual = auth.currentUser

        if (usuarioAtual != null) {
            textEmailUsuario.text = usuarioAtual.email

            db.collection("Usuarios").document(usuarioAtual.uid).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val nome = document.getString("nome")
                        textBemVindo.text = "Olá, $nome!"
                    } else {
                        textBemVindo.text = "Olá, Cliente!"
                    }
                }
                .addOnFailureListener {
                    textBemVindo.text = "Olá, Cliente!"
                }
        } else {
            startActivity(Intent(this, FormLogin::class.java))
            finish()
        }

        // Abrir Tela de Cardápio
        btnCardapio.setOnClickListener {
            val intent = Intent(this, CardapioActivity::class.java)
            startActivity(intent)
        }

        // Abrir Tela de Meus Pedidos
        btnMeusPedidos.setOnClickListener {
            val intent = Intent(this, MeusPedidosActivity::class.java)
            startActivity(intent)
        }

        // Sair da Conta e voltar pro Login
        btnSair.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, FormLogin::class.java))
            finish()
        }
    }
}