package com.example.imepacluisaugusto

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.imepacluisaugusto.models.Pedido
import com.example.imepacluisaugusto.utils.CarrinhoManager
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CheckoutActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)
        supportActionBar?.hide()

        // Botão de voltar da Toolbar (volta para a tela de pagamento/endereço)
        findViewById<View>(R.id.btn_voltar)?.setOnClickListener {
            finish()
        }

        // Bindings
        val txtEnderecoFinal = findViewById<TextView>(R.id.txt_endereco_final)
        val txtResumoItens = findViewById<TextView>(R.id.txt_resumo_itens)
        val txtSubtotal = findViewById<TextView>(R.id.txt_subtotal)
        val txtTaxa = findViewById<TextView>(R.id.txt_taxa_entrega)
        val txtTotal = findViewById<TextView>(R.id.txt_total_final)
        val btnConfirmar = findViewById<MaterialButton>(R.id.btn_confirmar_pedido)

        // Valores
        val subtotal = CarrinhoManager.calcularTotal()
        val taxaEntrega = 5.00
        val total = subtotal + taxaEntrega

        // 1. Preencher Endereço no Card Superior
        val enderecoFinal = if (CarrinhoManager.enderecoSelecionado.isNotEmpty()) {
            CarrinhoManager.enderecoSelecionado
        } else {
            "Endereço não selecionado"
        }
        txtEnderecoFinal.text = enderecoFinal

        // 2. Preencher Resumo (Apenas itens e pagamento)
        val resumo = StringBuilder()
        resumo.append("💳 Pagamento: ${CarrinhoManager.formaPagamentoSelecionada}${CarrinhoManager.trocoPara}\n")
        resumo.append("---------------------------\n")

        CarrinhoManager.itens.forEach { item ->
            resumo.append("${item.quantidade}x ${item.produto.nome} - ${formatarMoeda(item.produto.preco * item.quantidade)}\n")
        }

        if(CarrinhoManager.itens.isEmpty()) {
            txtResumoItens.text = "O carrinho está vazio."
            btnConfirmar.isEnabled = false
        } else {
            txtResumoItens.text = resumo.toString().trim()
        }

        // 3. Preencher Valores Financeiros
        txtSubtotal.text = formatarMoeda(subtotal)
        txtTaxa.text = formatarMoeda(taxaEntrega)
        txtTotal.text = formatarMoeda(total)

        // 4. Ação de Confirmar Pedido
        btnConfirmar.setOnClickListener {
            val usuarioAtual = auth.currentUser
            if (usuarioAtual != null) {
                val pedido = Pedido(
                    usuarioId = usuarioAtual.uid,
                    enderecoCompleto = CarrinhoManager.enderecoSelecionado,
                    valorTotal = total,
                    itens = CarrinhoManager.itens.toList(),
                    status = "Confirmado"
                )

                db.collection("pedidos").add(pedido).addOnSuccessListener {
                    CarrinhoManager.limparCarrinho()

                    // A MÁGICA ACONTECE AQUI:
                    // 1. Limpamos o histórico e abrimos a Home invisível no fundo
                    val intentHome = Intent(this, HomeActivity::class.java)
                    intentHome.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intentHome)

                    // 2. Imediatamente abrimos a tela de Meus Pedidos por cima dela
                    val intentPedidos = Intent(this, MeusPedidosActivity::class.java)
                    startActivity(intentPedidos)

                    // 3. Matamos o Checkout para não ficar na memória
                    finish()

                }.addOnFailureListener {
                    Snackbar.make(findViewById(android.R.id.content), "Erro ao enviar pedido.", Snackbar.LENGTH_LONG).show()
                }
            } else {
                Snackbar.make(findViewById(android.R.id.content), "Faça login para pedir.", Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun formatarMoeda(valor: Double): String = String.format("R$ %.2f", valor).replace(".", ",")
}