package com.example.imepacluisaugusto

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class FormCadastro : AppCompatActivity() {

    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_cadastro)
        supportActionBar?.hide()

        val editNome = findViewById<EditText>(R.id.edit_nome)
        val editEmail = findViewById<EditText>(R.id.edit_email_cadastro)
        val editSenha = findViewById<EditText>(R.id.edit_senha_cadastro)
        val btnCadastrar = findViewById<Button>(R.id.btn_cadastrar)

        btnCadastrar.setOnClickListener {
            val email = editEmail.text.toString()
            val senha = editSenha.text.toString()
            val nome = editNome.text.toString()

            if (nome.isEmpty() || email.isEmpty() || senha.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
            } else if (senha.length < 6) {
                editSenha.error = "A senha deve ter pelo menos 6 caracteres"
            } else {
                auth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener { tarefa ->
                    if (tarefa.isSuccessful) {
                        Toast.makeText(this, "Sucesso ao cadastrar $nome!", Toast.LENGTH_SHORT).show()
                        finish() // Volta para a tela de Login
                    } else {
                        val erro = tarefa.exception?.message
                        Toast.makeText(this, "Erro: $erro", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}