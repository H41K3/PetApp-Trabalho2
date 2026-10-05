package com.example.petapp

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CadastroScreen(onNavigateBackToLogin: () -> Unit) {
    var nome by remember { mutableStateOf("") }
    var cpf by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var celular by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var confirmarSenha by remember { mutableStateOf("") }
    var aceiteTermos by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFFFF))
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.Start
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        IconButton(
            onClick = onNavigateBackToLogin,
            modifier = Modifier.background(Color.White, RoundedCornerShape(12.dp))
        ) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Criar conta", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        Text(text = "Leva menos de um minuto.", fontSize = 16.sp, color = Color.Gray)

        Spacer(modifier = Modifier.height(24.dp))

        Text("Nome completo", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
        OutlinedTextField(
            value = nome,
            onValueChange = { nome = it },
            placeholder = { Text("Ex: João Silva") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))

        Text("CPF", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
        OutlinedTextField(
            value = cpf,
            onValueChange = {
                val apenasNumeros = it.filter { char -> char.isDigit() }
                if (apenasNumeros.length <= 11) cpf = apenasNumeros
            },
            placeholder = { Text("000.000.000-00") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            visualTransformation = CpfVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))

        Text("E-mail", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            placeholder = { Text("usuario@email.com") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))

        Text("Celular", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
        OutlinedTextField(
            value = celular,
            onValueChange = {
                val apenasNumeros = it.filter { char -> char.isDigit() }
                if (apenasNumeros.length <= 11) celular = apenasNumeros
            },
            placeholder = { Text("(00) 00000-0000") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            visualTransformation = TelefoneVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))

        Text("Senha", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
        OutlinedTextField(
            value = senha,
            onValueChange = { senha = it },
            placeholder = { Text("Mínimo 8 caracteres") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))

        Text("Confirmar Senha", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
        OutlinedTextField(
            value = confirmarSenha,
            onValueChange = { confirmarSenha = it },
            placeholder = { Text("Repita sua senha") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFE0F2EB), RoundedCornerShape(12.dp))
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("🎁", fontSize = 24.sp)
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text("Primeiro banho com 30% off", fontWeight = FontWeight.Bold, color = Color(0xFF0F5B43))
                Text("Cupom PATINHAS30 aplicado no cadastro.", fontSize = 12.sp, color = Color(0xFF0F5B43))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = aceiteTermos,
                onCheckedChange = { aceiteTermos = it },
                colors = CheckboxDefaults.colors(checkedColor = Color(0xFF0F5B43))
            )
            Text("Aceito os Termos de uso e a Política de privacidade.", fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onNavigateBackToLogin,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F5B43)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("Cadastrar", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Text("Já tenho conta · ", color = Color.Gray)
            Text(
                "Entrar",
                color = Color(0xFF0F5B43),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { onNavigateBackToLogin() }
            )
        }
        Spacer(modifier = Modifier.height(32.dp))
    }
}

class CpfVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val trimmed = if (text.text.length >= 11) text.text.substring(0..10) else text.text
        var out = ""
        for (i in trimmed.indices) {
            out += trimmed[i]
            if (i == 2 || i == 5) out += "."
            if (i == 8) out += "-"
        }
        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (offset <= 2) return offset
                if (offset <= 5) return offset + 1
                if (offset <= 8) return offset + 2
                if (offset <= 11) return offset + 3
                return 14
            }
            override fun transformedToOriginal(offset: Int): Int {
                if (offset <= 3) return offset
                if (offset <= 7) return offset - 1
                if (offset <= 11) return offset - 2
                if (offset <= 14) return offset - 3
                return 11
            }
        }
        return TransformedText(AnnotatedString(out), offsetMapping)
    }
}

class TelefoneVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val trimmed = if (text.text.length >= 11) text.text.substring(0..10) else text.text
        var out = ""
        for (i in trimmed.indices) {
            if (i == 0) out += "("
            out += trimmed[i]
            if (i == 1) out += ") "
            if (i == 6) out += "-"
        }
        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (offset <= 0) return offset
                if (offset <= 2) return offset + 1
                if (offset <= 7) return offset + 3
                if (offset <= 11) return offset + 4
                return 15
            }
            override fun transformedToOriginal(offset: Int): Int {
                if (offset <= 1) return offset
                if (offset <= 4) return offset - 1
                if (offset <= 9) return offset - 3
                if (offset <= 15) return offset - 4
                return 11
            }
        }
        return TransformedText(AnnotatedString(out), offsetMapping)
    }
}