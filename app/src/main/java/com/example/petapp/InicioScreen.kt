package com.example.petapp

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val coresServico = listOf(
    Color(0xFFE2F4EC), Color(0xFFFFEBEA), Color(0xFFFFF3DE),
    Color(0xFFF2E8FF), Color(0xFFE0F6F5), Color(0xFFFFEBE0)
)

@Composable
fun InicioScreen(
    dados: DadosApp,
    onServicoClick: (String) -> Unit,
    onPetClick: (Int) -> Unit,
    onPetsClick: () -> Unit,
    onAgendaClick: () -> Unit,
    onAgendamentoClick: (Int) -> Unit
) {
    val pendentes = dados.agendamentos.count { !it.concluido }
    val proximo = dados.agendamentos.firstOrNull { !it.concluido }
    val pet = dados.pets.firstOrNull()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FundoBege)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF7CB39E)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Person, contentDescription = null, tint = Color.White)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text("Olá,", fontSize = 14.sp, color = Color.Gray)
                    Text("Usuário", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                }
            }

            Box(modifier = Modifier.size(46.dp)) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .align(Alignment.BottomStart)
                        .clip(CircleShape)
                        .background(Color.White)
                        .border(1.dp, Color(0xFFE8E8E8), CircleShape)
                        .clickable { onAgendaClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Notifications,
                        contentDescription = "Ver agenda",
                        tint = Color.Black,
                        modifier = Modifier.size(20.dp)
                    )
                }
                if (pendentes > 0) {
                    Box(
                        modifier = Modifier
                            .size(18.dp)
                            .align(Alignment.TopEnd)
                            .background(VermelhoAlerta, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("$pendentes", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(VerdePrincipal)
                .clickable { if (pet != null) onPetClick(pet.id) else onPetsClick() }
                .padding(20.dp)
        ) {
            if (pet == null) {
                Text("Nenhum pet cadastrado. Toque para cadastrar.", color = Color.White, fontWeight = FontWeight.Bold)
            } else {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(52.dp)
                                    .background(Color(0xFFFF9B70), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(pet.emoji(), fontSize = 24.sp)
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(pet.nome, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                                Text(
                                    "${pet.raca} • ${pet.idadeAnos} ano(s) • ${pet.pesoKg.comoKg()}",
                                    color = Color.White.copy(alpha = 0.8f),
                                    fontSize = 12.sp
                                )
                            }
                        }

                        Row(
                            modifier = Modifier
                                .clip(CircleShape)
                                .border(1.dp, Color.White.copy(alpha = 0.3f), CircleShape)
                                .clickable { onPetsClick() }
                                .padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Trocar", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                Icons.Default.KeyboardArrowRight,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row {
                        Row(
                            modifier = Modifier
                                .background(Color.White.copy(alpha = 0.15f), CircleShape)
                                .padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("📅", fontSize = 12.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                "$pendentes pendente(s)",
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Row(
                            modifier = Modifier
                                .background(Color.White.copy(alpha = 0.15f), CircleShape)
                                .padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.FavoriteBorder,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Porte ${pet.porte()}", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Serviços", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            Text(
                "Ver agenda",
                color = VerdePrincipal,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { onAgendaClick() }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Servicos.precosBase.entries.toList().chunked(3).forEachIndexed { linha, grupo ->
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                grupo.forEachIndexed { i, entrada ->
                    ServiceCard(
                        title = entrada.key,
                        price = "a partir R$ ${entrada.value.toInt()}",
                        icon = Servicos.emoji(entrada.key),
                        bgColor = coresServico[(linha * 3 + i) % coresServico.size],
                        modifier = Modifier.weight(1f),
                        onClick = { onServicoClick(entrada.key) }
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text("Próximo agendamento", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        Spacer(modifier = Modifier.height(16.dp))

        if (proximo == null) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(20.dp))
                    .padding(16.dp)
            ) {
                Text("Nenhum agendamento pendente.", color = Color.Gray)
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = onAgendaClick,
                    colors = ButtonDefaults.buttonColors(containerColor = VerdePrincipal)
                ) { Text("Agendar agora") }
            }
        } else {
            val nomePet = dados.buscarPet(proximo.petId)?.nome ?: "Pet removido"
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.White)
                    .border(1.dp, Color(0xFFF0F0F0), RoundedCornerShape(20.dp))
                    .clickable { onAgendamentoClick(proximo.id) }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .background(VerdeClaro, RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(Servicos.emoji(proximo.servico), fontSize = 24.sp)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(proximo.servico, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.Black)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("${proximo.data} • ${proximo.horario} • $nomePet", color = Color.Gray, fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .background(VerdeClaro, CircleShape)
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text("Pendente", color = VerdePrincipal, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
                Icon(
                    Icons.Default.KeyboardArrowRight,
                    contentDescription = "Abrir detalhes",
                    tint = Color.DarkGray,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun ServiceCard(
    title: String,
    price: String,
    icon: String,
    bgColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .border(1.dp, Color(0xFFF7F7F7), RoundedCornerShape(20.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(52.dp)
                .background(bgColor, RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(icon, fontSize = 24.sp)
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.Black)
        Spacer(modifier = Modifier.height(2.dp))
        Text(price, color = Color(0xFF8A8A8A), fontSize = 10.sp)
    }
}
