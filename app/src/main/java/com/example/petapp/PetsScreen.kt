@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.petapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PetsScreen(dados: DadosApp, onPetClick: (Int) -> Unit) {
    var nome by rememberSaveable { mutableStateOf("") }
    var raca by rememberSaveable { mutableStateOf("") }
    var especie by rememberSaveable { mutableStateOf("Cão") }
    var idade by rememberSaveable { mutableStateOf("") }
    var peso by rememberSaveable { mutableStateOf("") }
    var erro by rememberSaveable { mutableStateOf("") }
    var petParaRemover by remember { mutableStateOf<Pet?>(null) }

    petParaRemover?.let { pet ->
        AlertDialog(
            onDismissRequest = { petParaRemover = null },
            title = { Text("Remover ${pet.nome}?") },
            text = { Text("Os agendamentos deste pet também serão removidos.") },
            confirmButton = {
                TextButton(onClick = {
                    dados.removerPet(pet.id)
                    petParaRemover = null
                }) { Text("Remover", color = VermelhoAlerta) }
            },
            dismissButton = {
                TextButton(onClick = { petParaRemover = null }) { Text("Cancelar") }
            }
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(FundoBege),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text("Meus pets", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        }

        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Adicionar pet", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.Black)

                    OutlinedTextField(
                        value = nome,
                        onValueChange = { nome = it },
                        label = { Text("Nome") },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = coresCampo(),
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = raca,
                        onValueChange = { raca = it },
                        label = { Text("Raça (opcional)") },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = coresCampo(),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("Cão", "Gato").forEach { opcao ->
                            FilterChip(
                                selected = especie == opcao,
                                onClick = { especie = opcao },
                                label = { Text(if (opcao == "Cão") "🐶 Cão" else "🐱 Gato") }
                            )
                        }
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedTextField(
                            value = idade,
                            onValueChange = { idade = it.filter { c -> c.isDigit() }.take(2) },
                            label = { Text("Idade (anos)") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            shape = RoundedCornerShape(12.dp),
                            colors = coresCampo(),
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = peso,
                            onValueChange = { peso = it.filter { c -> c.isDigit() || c == ',' || c == '.' }.take(5) },
                            label = { Text("Peso (kg)") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            shape = RoundedCornerShape(12.dp),
                            colors = coresCampo(),
                            modifier = Modifier.weight(1f)
                        )
                    }

                    if (erro.isNotEmpty()) {
                        Text(erro, color = VermelhoAlerta, fontSize = 12.sp)
                    }

                    Button(
                        onClick = {
                            val idadeInt = idade.toIntOrNull()
                            val pesoDouble = peso.replace(',', '.').toDoubleOrNull()
                            if (nome.isBlank()) {
                                erro = "Informe o nome do pet."
                            } else if (idadeInt == null || idadeInt > 30) {
                                erro = "Informe uma idade válida (0 a 30 anos)."
                            } else if (pesoDouble == null || pesoDouble <= 0.0 || pesoDouble > 120.0) {
                                erro = "Informe um peso válido (até 120 kg)."
                            } else {
                                dados.adicionarPet(
                                    nome.trim(),
                                    especie,
                                    raca.trim().ifBlank { "SRD" },
                                    idadeInt,
                                    pesoDouble
                                )
                                nome = ""
                                raca = ""
                                idade = ""
                                peso = ""
                                erro = ""
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = VerdePrincipal),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text("Adicionar pet", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        item {
            Text(
                "Pets cadastrados (${dados.pets.size})",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color.Black
            )
        }

        if (dados.pets.isEmpty()) {
            item {
                Text("Nenhum pet cadastrado ainda. Use o formulário acima.", color = Color.Gray)
            }
        }

        items(dados.pets, key = { it.id }) { pet ->
            PetCard(
                pet = pet,
                pendentes = dados.agendamentos.count { it.petId == pet.id && !it.concluido },
                onClick = { onPetClick(pet.id) },
                onRemover = { petParaRemover = pet }
            )
        }
    }
}

@Composable
fun PetCard(pet: Pet, pendentes: Int, onClick: () -> Unit, onRemover: () -> Unit) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .background(VerdeClaro, RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(pet.emoji(), fontSize = 26.sp)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(pet.nome, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.Black)
                Text(
                    "${pet.raca} • ${pet.idadeAnos} ano(s) • ${pet.pesoKg.comoKg()}",
                    color = Color.Gray,
                    fontSize = 12.sp
                )
                Text(
                    if (pendentes == 0) "Sem agendamentos pendentes" else "$pendentes agendamento(s) pendente(s)",
                    color = VerdePrincipal,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
            IconButton(onClick = onRemover) {
                Icon(Icons.Default.Delete, contentDescription = "Remover ${pet.nome}", tint = VermelhoAlerta)
            }
        }
    }
}
