package com.example.imepacluisaugusto

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class PerfilActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)
        supportActionBar?.hide()

        val editNome = findViewById<EditText>(R.id.edit_perfil_nome)
        val editEmail = findViewById<EditText>(R.id.edit_perfil_email)
        val editTelefone = findViewById<EditText>(R.id.edit_perfil_telefone)
        val btnSalvar = findViewById<Button>(R.id.btn_salvar_perfil)

        val usuarioAtual = auth.currentUser

        if (usuarioAtual != null) {
            val uid = usuarioAtual.uid
            editEmail.setText(usuarioAtual.email)

            // REQUISITO DA FACULDADE: SELECT APENAS 1 REGISTRO
            db.collection("Usuarios").document(uid).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        editNome.setText(document.getString("nome"))
                        editTelefone.setText(document.getString("telefone") ?: "")
                    }
                }

            // REQUISITO DA FACULDADE: UPDATE
            btnSalvar.setOnClickListener {
                val novoNome = editNome.text.toString()
                val novoTelefone = editTelefone.text.toString()

                if (novoNome.isNotEmpty()) {
                    val dadosAtualizados = mapOf(
                        "nome" to novoNome,
                        "telefone" to novoTelefone
                    )

                    db.collection("Usuarios").document(uid).update(dadosAtualizados)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Dados atualizados com sucesso!", Toast.LENGTH_SHORT).show()
                            finish() // Fecha a tela e volta pra Home
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Erro ao atualizar.", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    Toast.makeText(this, "O nome não pode ficar vazio.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}