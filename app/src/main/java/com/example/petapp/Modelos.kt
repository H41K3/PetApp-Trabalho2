package com.example.petapp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import java.util.Locale

// ---------- data classes ----------

data class Pet(
    val id: Int,
    val nome: String,
    val especie: String,
    val raca: String,
    val idadeAnos: Int,
    val pesoKg: Double
)

data class Agendamento(
    val id: Int,
    val petId: Int,
    val servico: String,
    val data: String,
    val horario: String,
    val observacoes: String = "",
    val concluido: Boolean = false
)

object Servicos {
    val precosBase: Map<String, Double> = linkedMapOf(
        "Banho" to 59.0,
        "Tosa" to 75.0,
        "Vacinas" to 89.0,
        "Consulta" to 129.0,
        "Hotel" to 110.0,
        "Táxi pet" to 25.0
    )

    private val emojis = mapOf(
        "Banho" to "🛁",
        "Tosa" to "✂️",
        "Vacinas" to "💉",
        "Consulta" to "🩺",
        "Hotel" to "🛏️",
        "Táxi pet" to "🚐"
    )

    fun emoji(servico: String): String = emojis[servico] ?: "🐾"
}

fun Pet.emoji(): String = if (especie == "Gato") "🐱" else "🐶"

fun Pet.porte(): String = when {
    pesoKg <= 10.0 -> "Pequeno"
    pesoKg <= 25.0 -> "Médio"
    else -> "Grande"
}

fun Pet.fatorPreco(): Double = when (porte()) {
    "Pequeno" -> 1.0
    "Médio" -> 1.25
    else -> 1.5
}

fun Pet.idadeHumana(): Int = when {
    idadeAnos <= 0 -> 0
    idadeAnos == 1 -> 15
    idadeAnos == 2 -> 24
    else -> 24 + (idadeAnos - 2) * (if (especie == "Gato") 4 else 5)
}

fun Pet.precoDoServico(servico: String): Double =
    (Servicos.precosBase[servico] ?: 0.0) * fatorPreco()

fun Double.comoReais(): String = "R$ " + String.format(Locale.forLanguageTag("pt-BR"), "%.2f", this)

fun Double.comoKg(): String =
    if (this % 1.0 == 0.0) "${toInt()} kg" else "$this kg".replace('.', ',')


class DadosApp {
    val pets = mutableStateListOf(
        Pet(1, "Thor", "Cão", "Golden Retriever", 4, 32.0),
        Pet(2, "Mel", "Gato", "Siamês", 2, 4.5)
    )

    val agendamentos = mutableStateListOf(
        Agendamento(1, 1, "Banho", "10/10/2026", "10:30", "Banho + hidratação"),
        Agendamento(2, 1, "Vacinas", "17/10/2026", "15:00"),
        Agendamento(3, 2, "Consulta", "12/10/2026", "09:00", "Check-up anual")
    )
    var servicoPreSelecionado by mutableStateOf("")
    var petPreSelecionado by mutableStateOf(-1)

    private var proximoPetId = 3
    private var proximoAgendamentoId = 4

    fun buscarPet(id: Int): Pet? = pets.firstOrNull { it.id == id }
    fun buscarAgendamento(id: Int): Agendamento? = agendamentos.firstOrNull { it.id == id }

    fun adicionarPet(nome: String, especie: String, raca: String, idade: Int, peso: Double) {
        pets.add(Pet(proximoPetId++, nome, especie, raca, idade, peso))
    }

    fun removerPet(id: Int) {
        pets.removeAll { it.id == id }
        agendamentos.removeAll { it.petId == id }
    }

    fun atualizarPeso(id: Int, novoPeso: Double) {
        val i = pets.indexOfFirst { it.id == id }
        if (i >= 0) pets[i] = pets[i].copy(pesoKg = novoPeso)
    }

    fun adicionarAgendamento(petId: Int, servico: String, data: String, horario: String, obs: String) {
        agendamentos.add(Agendamento(proximoAgendamentoId++, petId, servico, data, horario, obs))
    }

    fun removerAgendamento(id: Int) {
        agendamentos.removeAll { it.id == id }
    }

    fun alternarConcluido(id: Int) {
        val i = agendamentos.indexOfFirst { it.id == id }
        if (i >= 0) agendamentos[i] = agendamentos[i].copy(concluido = !agendamentos[i].concluido)
    }
}
