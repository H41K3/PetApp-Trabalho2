@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.petapp

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val regexData = Regex("""^\d{2}/\d{2}/\d{4}$""")
private val regexHora = Regex("""^([01]\d|2[0-3]):[0-5]\d$""")

//agendamentos: adicionar, marcar/remover e abrir detalhes
@Composable
fun AgendaScreen(dados: DadosApp, onAgendamentoClick: (Int) -> Unit) {
    var petId by rememberSaveable {
        mutableStateOf(
            if (dados.petPreSelecionado > 0) dados.petPreSelecionado
            else dados.pets.firstOrNull()?.id ?: -1
        )
    }
    var servico by rememberSaveable {
        mutableStateOf(dados.servicoPreSelecionado.ifBlank { "Banho" })
    }
    var data by rememberSaveable { mutableStateOf("") }
    var horario by rememberSaveable { mutableStateOf("") }
    var obs by rememberSaveable { mutableStateOf("") }
    var erro by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(Unit) {
        dados.servicoPreSelecionado = ""
        dados.petPreSelecionado = -1
    }

    //se o pet escolhido foi removido, escolhe outro
    val petAtual = dados.buscarPet(petId) ?: dados.pets.firstOrNull()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(FundoBege),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text("Agenda", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        }

        //formulário: novo agendamento
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Novo agendamento", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.Black)

                    if (dados.pets.isEmpty()) {
                        Text(
                            "Cadastre um pet na aba Pets antes de agendar.",
                            color = Color.Gray,
                            fontSize = 13.sp
                        )
                    } else {
                        Text("Pet", fontSize = 12.sp, color = Color.Gray)
                        Row(
                            modifier = Modifier.horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            dados.pets.forEach { pet ->
                                FilterChip(
                                    selected = petAtual?.id == pet.id,
                                    onClick = { petId = pet.id },
                                    label = { Text("${pet.emoji()} ${pet.nome}") }
                                )
                            }
                        }

                        Text("Serviço", fontSize = 12.sp, color = Color.Gray)
                        Row(
                            modifier = Modifier.horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Servicos.precosBase.keys.forEach { nomeServico ->
                                FilterChip(
                                    selected = servico == nomeServico,
                                    onClick = { servico = nomeServico },
                                    label = { Text("${Servicos.emoji(nomeServico)} $nomeServico") }
                                )
                            }
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            OutlinedTextField(
                                value = data,
                                onValueChange = { data = it.filter { c -> c.isDigit() || c == '/' }.take(10) },
                                label = { Text("Data") },
                                placeholder = { Text("dd/mm/aaaa") },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                shape = RoundedCornerShape(12.dp),
                                colors = coresCampo(),
                                modifier = Modifier.weight(1f)
                            )
                            OutlinedTextField(
                                value = horario,
                                onValueChange = { horario = it.filter { c -> c.isDigit() || c == ':' }.take(5) },
                                label = { Text("Horário") },
                                placeholder = { Text("hh:mm") },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                shape = RoundedCornerShape(12.dp),
                                colors = coresCampo(),
                                modifier = Modifier.weight(1f)
                            )
                        }

                        OutlinedTextField(
                            value = obs,
                            onValueChange = { obs = it },
                            label = { Text("Observações (opcional)") },
                            minLines = 2,
                            maxLines = 4,
                            shape = RoundedCornerShape(12.dp),
                            colors = coresCampo(),
                            modifier = Modifier.fillMaxWidth()
                        )

                        if (erro.isNotEmpty()) {
                            Text(erro, color = VermelhoAlerta, fontSize = 12.sp)
                        }

                        Button(
                            onClick = {
                                if (petAtual == null) {
                                    erro = "Escolha um pet."
                                } else if (!regexData.matches(data)) {
                                    erro = "Data inválida. Use dd/mm/aaaa."
                                } else if (!regexHora.matches(horario)) {
                                    erro = "Horário inválido. Use hh:mm (ex.: 14:30)."
                                } else {
                                    dados.adicionarAgendamento(petAtual.id, servico, data, horario, obs.trim())
                                    data = ""
                                    horario = ""
                                    obs = ""
                                    erro = ""
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = VerdePrincipal),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Text("Agendar", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        //lista
        item {
            Text(
                "Meus agendamentos (${dados.agendamentos.size})",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color.Black
            )
        }

        if (dados.agendamentos.isEmpty()) {
            item { Text("Nenhum agendamento ainda.", color = Color.Gray) }
        }

        items(dados.agendamentos, key = { it.id }) { ag ->
            AgendamentoCard(
                agendamento = ag,
                nomePet = dados.buscarPet(ag.petId)?.nome ?: "Pet removido",
                onClick = { onAgendamentoClick(ag.id) },
                onConcluir = { dados.alternarConcluido(ag.id) },
                onRemover = { dados.removerAgendamento(ag.id) }
            )
        }
    }
}

@Composable
fun AgendamentoCard(
    agendamento: Agendamento,
    nomePet: String,
    onClick: () -> Unit,
    onConcluir: () -> Unit,
    onRemover: () -> Unit
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = agendamento.concluido,
                onCheckedChange = { onConcluir() },
                colors = CheckboxDefaults.colors(checkedColor = VerdePrincipal)
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "${Servicos.emoji(agendamento.servico)} ${agendamento.servico} • $nomePet",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = if (agendamento.concluido) Color.Gray else Color.Black,
                    textDecoration = if (agendamento.concluido) TextDecoration.LineThrough else TextDecoration.None
                )
                Text(
                    "${agendamento.data} às ${agendamento.horario}",
                    color = Color.Gray,
                    fontSize = 12.sp
                )
                Text(
                    if (agendamento.concluido) "Concluído" else "Pendente",
                    color = if (agendamento.concluido) Color.Gray else VerdePrincipal,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
            IconButton(onClick = onRemover) {
                Icon(Icons.Default.Delete, contentDescription = "Remover agendamento", tint = VermelhoAlerta)
            }
        }
    }
}
