package com.example.imepacluisaugusto

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class PerfilActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)
        supportActionBar?.hide()

        // ATIVA O BOTÃO DE VOLTAR
        findViewById<View>(R.id.btn_voltar).setOnClickListener {
            finish()
        }

        val edtNome = findViewById<EditText>(R.id.edt_perfil_nome)
        val edtEndereco = findViewById<EditText>(R.id.edt_perfil_endereco)
        val btnAtualizar = findViewById<MaterialButton>(R.id.btn_atualizar_perfil)

        // Aqui removemos a variável btnSair, já que o botão foi movido para a Home na etapa anterior

        val userId = auth.currentUser?.uid

        if (userId != null) {
            db.collection("Usuarios").document(userId).get()
                .addOnSuccessListener { documento ->
                    if (documento.exists()) {
                        edtNome.setText(documento.getString("nome"))
                        edtEndereco.setText(documento.getString("endereco"))
                    }
                }
        }

        btnAtualizar.setOnClickListener {
            if (userId != null) {
                val novosDados = mapOf(
                    "nome" to edtNome.text.toString(),
                    "endereco" to edtEndereco.text.toString()
                )

                db.collection("Usuarios").document(userId).update(novosDados)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Perfil atualizado com sucesso!", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Erro ao atualizar perfil.", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }
}