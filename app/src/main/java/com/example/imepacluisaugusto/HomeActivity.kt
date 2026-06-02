package com.example.imepacluisaugusto

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HomeActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        supportActionBar?.hide()

        // 1. Vincular os campos do Header
        val txtNome = findViewById<TextView>(R.id.text_bem_vindo)
        val txtEmail = findViewById<TextView>(R.id.text_email_usuario)
        val txtAvatarInicial = findViewById<TextView>(R.id.text_avatar_inicial) // Certifique-se de dar esse ID no XML

        // 2. Buscar dados no Firestore
        val userId = auth.currentUser?.uid
        if (userId != null) {
            db.collection("Usuarios").document(userId).get()
                .addOnSuccessListener { documento ->
                    if (documento.exists()) {
                        val nome = documento.getString("nome") ?: "Cliente"
                        val email = auth.currentUser?.email ?: ""

                        txtNome.text = "Olá, $nome!"
                        txtEmail.text = email

                        // Atualiza a letra do avatar com a primeira letra do nome
                        txtAvatarInicial.text = nome.firstOrNull()?.toString()?.uppercase() ?: "U"
                    }
                }
        }

        // --- Configuração dos botões (como já tínhamos feito) ---
        findViewById<View>(R.id.btn_cardapio).setOnClickListener {
            startActivity(Intent(this, CardapioActivity::class.java))
        }

        findViewById<View>(R.id.card_cardapio).setOnClickListener {
            startActivity(Intent(this, CardapioActivity::class.java))
        }

        findViewById<View>(R.id.card_meus_pedidos).setOnClickListener {
            startActivity(Intent(this, MeusPedidosActivity::class.java))
        }

        findViewById<View>(R.id.card_meu_perfil).setOnClickListener {
            startActivity(Intent(this, PerfilActivity::class.java))
        }

        findViewById<View>(R.id.card_sobre).setOnClickListener {
            startActivity(Intent(this, SobreActivity::class.java))
        }

        findViewById<MaterialButton>(R.id.btn_sair).setOnClickListener {
            auth.signOut()
            val intent = Intent(this, FormLogin::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
}