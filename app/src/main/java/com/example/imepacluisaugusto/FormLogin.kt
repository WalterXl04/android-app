package com.example.imepacluisaugusto

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class FormLogin : AppCompatActivity() {

    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_login)
        supportActionBar?.hide()

        val editEmail = findViewById<EditText>(R.id.edit_email)
        val editSenha = findViewById<EditText>(R.id.edit_senha)
        val btnEntrar = findViewById<Button>(R.id.btn_entrar)
        val textCadastro = findViewById<TextView>(R.id.text_tela_cadastro)

        btnEntrar.setOnClickListener {
            val email = editEmail.text.toString()
            val senha = editSenha.text.toString()

            if (email.isEmpty()) {
                editEmail.error = "Digite seu e-mail"
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                editEmail.error = "Digite um e-mail válido"
            } else if (senha.isEmpty()) {
                editSenha.error = "Digite sua senha"
            } else {
                auth.signInWithEmailAndPassword(email, senha).addOnCompleteListener { tarefa ->
                    if (tarefa.isSuccessful) {
                        Toast.makeText(this, "Bem-vindo!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, HomeActivity::class.java))
                    } else {
                        Toast.makeText(this, "E-mail ou senha incorretos!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        textCadastro.setOnClickListener {
            startActivity(Intent(this, FormCadastro::class.java))
        }
    }
}