package com.example.petapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.petapp.ui.theme.PetAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PetAppTheme {
                PetAppNavigation()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppPreview() {
    PetAppTheme {
        LoginScreen(
            onNavigateToCadastro = {},
            onNavigateToHome = {}
        )
    }
}
