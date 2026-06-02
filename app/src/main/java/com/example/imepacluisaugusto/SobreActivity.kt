package com.example.imepacluisaugusto

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class SobreActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sobre)
        supportActionBar?.hide()

        // Ativa o botão de voltar da Toolbar
        findViewById<View>(R.id.btn_voltar).setOnClickListener {
            finish()
        }
    }
}