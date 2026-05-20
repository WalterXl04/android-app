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

        // 1. Encontrar os TextViews do layout (Nome e E-mail)
        val textBemVindo = findViewById<TextView>(R.id.text_bem_vindo)
        val textEmailUsuario = findViewById<TextView>(R.id.text_email_usuario) // Identificando o novo campo

        // 2. Encontrar o botão de sair
        val btnSair = findViewById<Button>(R.id.btn_sair)

        // 3. Inicializar o Firebase Auth e Firestore
        val db = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()
        val usuarioAtual = auth.currentUser

        // 4. Verificar se o usuário está logado de verdade
        if (usuarioAtual != null) {
            val uid = usuarioAtual.uid

            // 👇 AQUI ESTÁ A MÁGICA DO EMAIL: Pegamos direto do login! 👇
            val emailLogado = usuarioAtual.email
            textEmailUsuario.text = emailLogado

            // 5. Buscar o NOME do usuário lá no banco de dados Firestore
            db.collection("Usuarios").document(uid).get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        val nome = documentSnapshot.getString("nome")
                        textBemVindo.text = "Olá, $nome!"
                    } else {
                        textBemVindo.text = "Olá, Usuário!"
                    }
                }
                .addOnFailureListener {
                    textBemVindo.text = "Olá, Usuário!"
                }
        } else {
            // Se falhar a segurança, joga de volta pro login
            startActivity(Intent(this, FormLogin::class.java))
            finish()
        }

        // 6. Configuração do Botão Sair
        btnSair.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, FormLogin::class.java))
            finish()
        }
    }
}