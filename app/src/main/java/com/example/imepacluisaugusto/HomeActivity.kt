package com.example.imepacluisaugusto

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        supportActionBar?.hide()

        val btnSair = findViewById<Button>(R.id.btn_sair)
        btnSair.setOnClickListener {
            finish()
        }
    }
}