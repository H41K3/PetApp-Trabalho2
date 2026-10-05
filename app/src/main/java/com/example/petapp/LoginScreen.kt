package com.example.petapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import android.widget.Toast
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val VerdePrincipal = Color(0xFF0F5B43)
val FundoApp = Color(0xFFFFFFFF)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(onNavigateToCadastro: () -> Unit, onNavigateToHome: () -> Unit) {
    var emailOuCpf by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var lembrarMim by remember { mutableStateOf(true) }
    var mostrarEsqueciSenha by remember { mutableStateOf(false) }
    var emailRecuperacao by remember { mutableStateOf("") }
    val context = LocalContext.current

    if (mostrarEsqueciSenha) {
        AlertDialog(
            onDismissRequest = { mostrarEsqueciSenha = false },
            title = { Text("Recuperar senha") },
            text = {
                OutlinedTextField(
                    value = emailRecuperacao,
                    onValueChange = { emailRecuperacao = it },
                    label = { Text("Seu e-mail") },
                    singleLine = true
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    val msg = if (emailRecuperacao.contains("@")) "Link enviado para $emailRecuperacao" else "Digite um e-mail válido"
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                    if (emailRecuperacao.contains("@")) mostrarEsqueciSenha = false
                }) { Text("Enviar link", color = VerdePrincipal) }
            },
            dismissButton = {
                TextButton(onClick = { mostrarEsqueciSenha = false }) { Text("Cancelar") }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FundoApp)
            .padding(24.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        Image(
            painter = painterResource(id = R.drawable.ic_logo),
            contentDescription = "Logo PetApp",
            modifier = Modifier.size(60.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text("Bem-vindo de\nvolta", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color.Black, lineHeight = 36.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Entre para cuidar do seu melhor amigo.", fontSize = 16.sp, color = Color.Gray)

        Spacer(modifier = Modifier.height(32.dp))

        Text("E-mail ou CPF", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
        OutlinedTextField(
            value = emailOuCpf,
            onValueChange = { emailOuCpf = it },
            placeholder = { Text("Digite um dos dois") },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = VerdePrincipal,
                unfocusedBorderColor = Color.LightGray
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Senha", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
        OutlinedTextField(
            value = senha,
            onValueChange = { senha = it },
            placeholder = { Text("••••••••") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = VerdePrincipal,
                unfocusedBorderColor = VerdePrincipal
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = lembrarMim,
                    onCheckedChange = { lembrarMim = it },
                    colors = CheckboxDefaults.colors(checkedColor = VerdePrincipal)
                )
                Text("Lembrar de mim", fontSize = 14.sp)
            }
            Text(
                "Esqueci a senha",
                color = VerdePrincipal,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                modifier = Modifier.clickable { mostrarEsqueciSenha = true }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onNavigateToHome,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = VerdePrincipal),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("Entrar", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            Divider(modifier = Modifier.weight(1f), color = Color.LightGray)
            Text("  ou continue com  ", color = Color.Gray, fontSize = 14.sp)
            Divider(modifier = Modifier.weight(1f), color = Color.LightGray)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp)
                    .border(1.dp, Color.LightGray, RoundedCornerShape(12.dp))
                    .background(Color.White, RoundedCornerShape(12.dp))
                    .clickable { onNavigateToHome() },
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(painter = painterResource(id = R.drawable.ic_apple), contentDescription = "Apple Logo", modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Apple", fontWeight = FontWeight.Bold)
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp)
                    .border(1.dp, Color.LightGray, RoundedCornerShape(12.dp))
                    .background(Color.White, RoundedCornerShape(12.dp))
                    .clickable { onNavigateToHome() },
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(painter = painterResource(id = R.drawable.ic_google), contentDescription = "Google Logo", modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Google", fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Text("Não tem conta? ", color = Color.Gray)
            Text(
                "Cadastre-se grátis",
                color = VerdePrincipal,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { onNavigateToCadastro() }
            )
        }
    }
}