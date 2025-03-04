package com.example.snakegame

import androidx.compose.ui.geometry.Offset

// Clase sellada que define los eventos que pueden ocurrir en el juego de la serpiente
sealed class SnakeGameEvent {

    // Evento para iniciar el juego
    data object StartGame : SnakeGameEvent()

    // Evento para pausar el juego
    data object PauseGame : SnakeGameEvent()

    // Evento para reiniciar el juego
    data object ResetGame : SnakeGameEvent()

    // Evento para actualizar la dirección de la serpiente según la posición tocada
    data class UpdateDirection(val offset: Offset, val canvasWidth: Int) : SnakeGameEvent()
}
