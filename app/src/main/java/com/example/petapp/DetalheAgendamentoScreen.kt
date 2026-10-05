@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.petapp

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DetalheAgendamentoScreen(
    agendamentoId: Int,
    dados: DadosApp,
    onVoltar: () -> Unit,
    onPetClick: (Int) -> Unit
) {
    val ag = dados.buscarAgendamento(agendamentoId)
    val pet = ag?.let { dados.buscarPet(it.petId) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FundoBege)
    ) {
        TopAppBar(
            title = { Text("Detalhes do agendamento", fontWeight = FontWeight.Bold) },
            navigationIcon = {
                IconButton(onClick = onVoltar) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = FundoBege)
        )

        if (ag == null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("Agendamento não encontrado.", color = Color.Gray)
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = onVoltar,
                    colors = ButtonDefaults.buttonColors(containerColor = VerdePrincipal)
                ) { Text("Voltar") }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(VerdePrincipal, RoundedCornerShape(24.dp))
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(Servicos.emoji(ag.servico), fontSize = 40.sp)
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(ag.servico, color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                        Text(
                            if (ag.concluido) "Concluído" else "Pendente",
                            color = Color.White.copy(alpha = 0.85f),
                            fontSize = 13.sp
                        )
                    }
                }

                CartaoSecao("Quando") {
                    LinhaInfo("Data", ag.data)
                    LinhaInfo("Horário", ag.horario)
                    if (ag.observacoes.isNotBlank()) {
                        LinhaInfo("Observações", ag.observacoes)
                    }
                }

                CartaoSecao("Pet") {
                    if (pet == null) {
                        Text("Este pet foi removido.", color = Color.Gray, fontSize = 13.sp)
                    } else {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(VerdeClaro, RoundedCornerShape(12.dp))
                                .clickable { onPetClick(pet.id) }
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(pet.emoji(), fontSize = 28.sp)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(pet.nome, fontWeight = FontWeight.Bold, color = Color.Black)
                                Text(
                                    "${pet.raca} • ${pet.pesoKg.comoKg()} • porte ${pet.porte()}",
                                    fontSize = 12.sp,
                                    color = Color.Gray
                                )
                            }
                            Icon(Icons.Default.KeyboardArrowRight, contentDescription = "Ver pet", tint = Color.DarkGray)
                        }
                    }
                }

                if (pet != null) {
                    CartaoSecao("Valor estimado") {
                        val base = Servicos.precosBase[ag.servico] ?: 0.0
                        LinhaInfo("Preço base", base.comoReais())
                        LinhaInfo("Ajuste por porte (${pet.porte()})", "x${pet.fatorPreco().toString().replace('.', ',')}")
                        LinhaInfo("Total", pet.precoDoServico(ag.servico).comoReais())
                    }
                }

                Button(
                    onClick = { dados.alternarConcluido(ag.id) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = VerdePrincipal),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        if (ag.concluido) "Reabrir agendamento" else "Marcar como concluído",
                        fontWeight = FontWeight.Bold
                    )
                }

                OutlinedButton(
                    onClick = {
                        dados.removerAgendamento(ag.id)
                        onVoltar()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Cancelar agendamento", color = VermelhoAlerta, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
