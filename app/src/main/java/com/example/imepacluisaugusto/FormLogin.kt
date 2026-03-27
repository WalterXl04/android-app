package com.example.imepacluisaugusto

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class FormLogin : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_login)
        supportActionBar?.hide()

        // 1. Referenciar os campos e botões
        val editEmail = findViewById<EditText>(R.id.edit_email)
        val editSenha = findViewById<EditText>(R.id.edit_senha)
        val btnEntrar = findViewById<Button>(R.id.btn_entrar)
        val textCadastro = findViewById<TextView>(R.id.text_tela_cadastro)

        // 2. Lógica do botão Entrar com Validação
        btnEntrar.setOnClickListener {
            val email = editEmail.text.toString()
            val senha = editSenha.text.toString()

            if (email.isEmpty()) {
                editEmail.error = "Digite seu e-mail"
            } else if (senha.isEmpty()) {
                editSenha.error = "Digite sua senha"
            } else if (senha.length < 6) {
                editSenha.error = "A senha deve ter no mínimo 6 caracteres"
            } else {
                // Se tudo estiver certo, vai para a Home
                Toast.makeText(this, "Login realizado!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, HomeActivity::class.java))
            }
        }

        // Vai para a tela de Cadastro
        textCadastro.setOnClickListener {
            startActivity(Intent(this, FormCadastro::class.java))
        }
    }
}