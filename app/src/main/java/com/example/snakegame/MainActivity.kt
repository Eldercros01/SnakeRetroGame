package com.example.snakegame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.snakegame.ui.theme.SnakeGameTheme

// Actividad principal del juego de la serpiente
class MainActivity : ComponentActivity() {
    // Método llamado cuando se crea la actividad
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Establece el contenido de la actividad usando Compose
        setContent {
            // Aplica el tema del juego
            SnakeGameTheme {
                // Obtiene el ViewModel del juego
                val viewModel = viewModel<SnakeGameViewModel>()

                // Observa el estado del juego usando el estado de flujo con el ciclo de vida
                val state by viewModel.state.collectAsStateWithLifecycle()

                // Muestra la pantalla del juego y pasa el estado y el evento a la pantalla
                SnakeGameScreen(
                    state = state,
                    onEvent = viewModel::onEvent
                )
            }
        }
    }
}
