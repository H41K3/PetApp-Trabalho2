package com.example.petapp

object Rotas {
    const val LOGIN = "login"
    const val CADASTRO = "cadastro"
    const val INICIO = "inicio"
    const val PETS = "pets"
    const val AGENDA = "agenda"
    const val CONTA = "conta"

    const val DETALHE_PET = "pet/{petId}"
    const val DETALHE_AGENDAMENTO = "agendamento/{agendamentoId}"

    fun detalhePet(id: Int) = "pet/$id"
    fun detalheAgendamento(id: Int) = "agendamento/$id"
}
