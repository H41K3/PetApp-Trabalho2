package com.example.petapp

import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

private data class Aba(val rota: String, val rotulo: String, val icone: ImageVector)

private val abas = listOf(
    Aba(Rotas.INICIO, "Início", Icons.Default.Home),
    Aba(Rotas.PETS, "Pets", Icons.Default.Pets),
    Aba(Rotas.AGENDA, "Agenda", Icons.Default.DateRange),
    Aba(Rotas.CONTA, "Conta", Icons.Default.Person)
)

fun NavController.irParaAba(rota: String) {
    navigate(rota) {
        popUpTo(Rotas.INICIO)
        launchSingleTop = true
    }
}

@Composable
fun PetAppNavigation() {
    val navController = rememberNavController()
    val dados = remember { DadosApp() }

    val backStackEntry by navController.currentBackStackEntryAsState()
    val rotaAtual = backStackEntry?.destination?.route

    val mostrarBarra = rotaAtual != null && rotaAtual != Rotas.LOGIN && rotaAtual != Rotas.CADASTRO

    val abaSelecionada = when (rotaAtual) {
        Rotas.DETALHE_PET -> Rotas.PETS
        Rotas.DETALHE_AGENDAMENTO -> Rotas.AGENDA
        else -> rotaAtual
    }

    Scaffold(
        bottomBar = {
            if (mostrarBarra) {
                NavigationBar(containerColor = Color.White) {
                    abas.forEach { aba ->
                        NavigationBarItem(
                            selected = abaSelecionada == aba.rota,
                            onClick = { navController.irParaAba(aba.rota) },
                            icon = { Icon(aba.icone, contentDescription = aba.rotulo) },
                            label = { Text(aba.rotulo, fontSize = 11.sp) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = VerdePrincipal,
                                selectedTextColor = VerdePrincipal,
                                indicatorColor = VerdeClaro,
                                unselectedIconColor = Color.Gray,
                                unselectedTextColor = Color.Gray
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Rotas.LOGIN,
            modifier = Modifier
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
        ) {
            composable(Rotas.LOGIN) {
                LoginScreen(
                    onNavigateToCadastro = { navController.navigate(Rotas.CADASTRO) },
                    onNavigateToHome = {
                        navController.navigate(Rotas.INICIO) {
                            popUpTo(Rotas.LOGIN) { inclusive = true }
                        }
                    }
                )
            }

            composable(Rotas.CADASTRO) {
                CadastroScreen(onNavigateBackToLogin = { navController.popBackStack() })
            }

            composable(Rotas.INICIO) {
                InicioScreen(
                    dados = dados,
                    onServicoClick = { servico ->
                        dados.servicoPreSelecionado = servico
                        navController.irParaAba(Rotas.AGENDA)
                    },
                    onPetClick = { id -> navController.navigate(Rotas.detalhePet(id)) },
                    onPetsClick = { navController.irParaAba(Rotas.PETS) },
                    onAgendaClick = { navController.irParaAba(Rotas.AGENDA) },
                    onAgendamentoClick = { id -> navController.navigate(Rotas.detalheAgendamento(id)) }
                )
            }

            composable(Rotas.PETS) {
                PetsScreen(
                    dados = dados,
                    onPetClick = { id -> navController.navigate(Rotas.detalhePet(id)) }
                )
            }

            composable(Rotas.AGENDA) {
                AgendaScreen(
                    dados = dados,
                    onAgendamentoClick = { id -> navController.navigate(Rotas.detalheAgendamento(id)) }
                )
            }

            composable(Rotas.CONTA) {
                ContaScreen(
                    dados = dados,
                    onMeusPets = { navController.irParaAba(Rotas.PETS) },
                    onMinhaAgenda = { navController.irParaAba(Rotas.AGENDA) },
                    onSair = {
                        navController.navigate(Rotas.LOGIN) {
                            popUpTo(Rotas.INICIO) { inclusive = true }
                        }
                    }
                )
            }

            composable(
                route = Rotas.DETALHE_PET,
                arguments = listOf(navArgument("petId") { type = NavType.IntType })
            ) { entry ->
                val petId = entry.arguments?.getInt("petId") ?: -1
                DetalhePetScreen(
                    petId = petId,
                    dados = dados,
                    onVoltar = { navController.popBackStack() },
                    onAgendamentoClick = { id -> navController.navigate(Rotas.detalheAgendamento(id)) },
                    onAgendarClick = {
                        dados.petPreSelecionado = petId
                        navController.irParaAba(Rotas.AGENDA)
                    }
                )
            }

            composable(
                route = Rotas.DETALHE_AGENDAMENTO,
                arguments = listOf(navArgument("agendamentoId") { type = NavType.IntType })
            ) { entry ->
                val agendamentoId = entry.arguments?.getInt("agendamentoId") ?: -1
                DetalheAgendamentoScreen(
                    agendamentoId = agendamentoId,
                    dados = dados,
                    onVoltar = { navController.popBackStack() },
                    onPetClick = { id -> navController.navigate(Rotas.detalhePet(id)) }
                )
            }
        }
    }
}
