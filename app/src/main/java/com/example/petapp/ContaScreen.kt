package com.example.petapp

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ContaScreen(
    dados: DadosApp,
    onMeusPets: () -> Unit,
    onMinhaAgenda: () -> Unit,
    onSair: () -> Unit
) {
    val concluidos = dados.agendamentos.count { it.concluido }
    val pendentes = dados.agendamentos.count { !it.concluido }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FundoBege)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Minha conta", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.Black)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(20.dp))
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(Color(0xFF7CB39E), CircleShape),
                contentAlignment = Alignment.Center
            ) { Icon(Icons.Default.Person, contentDescription = null, tint = Color.White) }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text("Usuário", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.Black)
                Text("Cliente PetApp", color = Color.Gray, fontSize = 13.sp)
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Estatistica("Pets", dados.pets.size.toString(), Modifier.weight(1f))
            Estatistica("Pendentes", pendentes.toString(), Modifier.weight(1f))
            Estatistica("Concluídos", concluidos.toString(), Modifier.weight(1f))
        }

        ItemMenu("Meus pets", Icons.Default.Pets, onMeusPets)
        ItemMenu("Minha agenda", Icons.Default.DateRange, onMinhaAgenda)

        Button(
            onClick = onSair,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            colors = ButtonDefaults.buttonColors(containerColor = VermelhoAlerta),
            shape = RoundedCornerShape(16.dp)
        ) { Text("Sair", fontWeight = FontWeight.Bold) }
    }
}

@Composable
private fun Estatistica(rotulo: String, valor: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .background(Color.White, RoundedCornerShape(16.dp))
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(valor, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = VerdePrincipal)
        Text(rotulo, fontSize = 12.sp, color = Color.Gray)
    }
}

@Composable
private fun ItemMenu(titulo: String, icone: ImageVector, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icone, contentDescription = null, tint = VerdePrincipal)
        Spacer(modifier = Modifier.width(16.dp))
        Text(titulo, fontWeight = FontWeight.SemiBold, color = Color.Black, modifier = Modifier.weight(1f))
        Icon(Icons.Default.KeyboardArrowRight, contentDescription = null, tint = Color.Gray)
    }
}
