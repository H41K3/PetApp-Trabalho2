@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.petapp

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DetalhePetScreen(
    petId: Int,
    dados: DadosApp,
    onVoltar: () -> Unit,
    onAgendamentoClick: (Int) -> Unit,
    onAgendarClick: () -> Unit
) {
    val pet = dados.buscarPet(petId)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FundoBege)
    ) {
        TopAppBar(
            title = { Text(pet?.nome ?: "Pet", fontWeight = FontWeight.Bold) },
            navigationIcon = {
                IconButton(onClick = onVoltar) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = FundoBege)
        )

        if (pet == null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("Pet não encontrado.", color = Color.Gray)
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = onVoltar,
                    colors = ButtonDefaults.buttonColors(containerColor = VerdePrincipal)
                ) { Text("Voltar") }
            }
        } else {
            ConteudoPet(pet, dados, onAgendamentoClick, onAgendarClick)
        }
    }
}

@Composable
private fun ConteudoPet(
    pet: Pet,
    dados: DadosApp,
    onAgendamentoClick: (Int) -> Unit,
    onAgendarClick: () -> Unit
) {
    var pesoTexto by remember(pet.id) { mutableStateOf(pet.pesoKg.toString().replace('.', ',')) }
    var erroPeso by remember(pet.id) { mutableStateOf("") }
    val agendamentosDoPet = dados.agendamentos.filter { it.petId == pet.id }

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
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(Color(0xFFFF9B70), CircleShape),
                contentAlignment = Alignment.Center
            ) { Text(pet.emoji(), fontSize = 32.sp) }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(pet.nome, color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text("${pet.especie} • ${pet.raca}", color = Color.White.copy(alpha = 0.85f), fontSize = 13.sp)
            }
        }

        CartaoSecao("Informações") {
            LinhaInfo("Idade", "${pet.idadeAnos} ano(s)")
            LinhaInfo("Peso", pet.pesoKg.comoKg())
            LinhaInfo("Idade em anos humanos (aprox.)", "${pet.idadeHumana()} anos")
            LinhaInfo("Porte", pet.porte())
        }

        // preços calculados a partir do porte
        CartaoSecao("Preços para porte ${pet.porte()}") {
            Servicos.precosBase.keys.forEach { servico ->
                LinhaInfo("${Servicos.emoji(servico)} $servico", pet.precoDoServico(servico).comoReais())
            }
            Text(
                "Valores ajustados pelo porte do pet (pequeno x1,0 • médio x1,25 • grande x1,5).",
                fontSize = 11.sp,
                color = Color.Gray
            )
        }

        CartaoSecao("Atualizar peso") {
            OutlinedTextField(
                value = pesoTexto,
                onValueChange = { pesoTexto = it.filter { c -> c.isDigit() || c == ',' || c == '.' }.take(5) },
                label = { Text("Peso (kg)") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                shape = RoundedCornerShape(12.dp),
                colors = coresCampo(),
                modifier = Modifier.fillMaxWidth()
            )
            if (erroPeso.isNotEmpty()) {
                Text(erroPeso, color = VermelhoAlerta, fontSize = 12.sp)
            }
            Button(
                onClick = {
                    val novo = pesoTexto.replace(',', '.').toDoubleOrNull()
                    if (novo == null || novo <= 0.0 || novo > 120.0) {
                        erroPeso = "Informe um peso válido (até 120 kg)."
                    } else {
                        dados.atualizarPeso(pet.id, novo)
                        erroPeso = ""
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = VerdePrincipal),
                shape = RoundedCornerShape(14.dp)
            ) { Text("Salvar peso", fontWeight = FontWeight.Bold) }
        }

        CartaoSecao("Agendamentos de ${pet.nome} (${agendamentosDoPet.size})") {
            if (agendamentosDoPet.isEmpty()) {
                Text("Nenhum agendamento para este pet.", color = Color.Gray, fontSize = 13.sp)
            }
            agendamentosDoPet.forEach { ag ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(VerdeClaro, RoundedCornerShape(12.dp))
                        .clickable { onAgendamentoClick(ag.id) }
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            "${Servicos.emoji(ag.servico)} ${ag.servico}",
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Text("${ag.data} às ${ag.horario}", fontSize = 12.sp, color = Color.Gray)
                    }
                    Text(
                        if (ag.concluido) "Concluído" else "Pendente",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = VerdePrincipal
                    )
                }
            }
            Button(
                onClick = onAgendarClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = VerdePrincipal),
                shape = RoundedCornerShape(14.dp)
            ) { Text("Agendar serviço para ${pet.nome}", fontWeight = FontWeight.Bold) }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun CartaoSecao(titulo: String, conteudo: @Composable ColumnScope.() -> Unit) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(titulo, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.Black)
            conteudo()
        }
    }
}

@Composable
fun LinhaInfo(rotulo: String, valor: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(rotulo, color = Color.Gray, fontSize = 13.sp, modifier = Modifier.weight(1f))
        Text(valor, color = Color.Black, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
    }
}
