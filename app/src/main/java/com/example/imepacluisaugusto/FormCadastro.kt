package com.example.imepacluisaugusto

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class FormCadastro : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_cadastro)
        supportActionBar?.hide()

        val editNome = findViewById<EditText>(R.id.edit_nome)
        val editEmail = findViewById<EditText>(R.id.edit_email_cadastro)
        val editSenha = findViewById<EditText>(R.id.edit_senha_cadastro)
        val btnCadastrar = findViewById<Button>(R.id.btn_cadastrar)

        btnCadastrar.setOnClickListener {
            val nome = editNome.text.toString()
            val email = editEmail.text.toString()
            val senha = editSenha.text.toString()

            if (nome.isEmpty()) {
                editNome.error = "Preencha o nome"
            } else if (email.isEmpty()) {
                editEmail.error = "Preencha o e-mail"
            } else if (senha.isEmpty()) {
                editSenha.error = "Preencha a senha"
            } else {
                Toast.makeText(this, "Cadastro realizado!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}