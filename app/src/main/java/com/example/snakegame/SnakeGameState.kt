package com.example.snakegame

import kotlin.random.Random

// Clase que representa el estado del juego de la serpiente
data class SnakeGameState(
    val xAxisGridSize: Int = 20, // Tamaño del grid en el eje X
    val yAxisGridSize: Int = 30, // Tamaño del grid en el eje Y
    val direction: Direction = Direction.RIGHT, // Dirección actual de la serpiente
    val snake: List<Coordinate> = listOf(Coordinate(x = 5, y = 5)), // Lista de coordenadas que representan la serpiente
    val food: Coordinate = generateRandomFoodCoordinate(), // Coordenada de la comida generada aleatoriamente
    val isGameOver: Boolean = false, // Estado del juego: si ha terminado o no
    val gameState: GameState = GameState.IDLE // Estado del juego (inactivo, iniciado o pausado)
) {
    companion object {
        // Función que genera una coordenada aleatoria para la comida
        fun generateRandomFoodCoordinate(): Coordinate {
            return Coordinate(
                x = Random.nextInt(from = 1, until = 19), // Coordenada X aleatoria entre 1 y 19
                y = Random.nextInt(from = 1, until = 29)  // Coordenada Y aleatoria entre 1 y 29
            )
        }
    }
}

// Enumeración que define los posibles estados del juego
enum class GameState {
    IDLE,    // Estado inactivo
    STARTED, // El juego ha comenzado
    PAUSED   // El juego está en pausa
}

// Enumeración que define las posibles direcciones de la serpiente
enum class Direction {
    UP,    // Arriba
    DOWN,  // Abajo
    LEFT,  // Izquierda
    RIGHT  // Derecha
}

// Clase que representa una coordenada (x, y) en el grid
data class Coordinate(
    val x: Int, // Coordenada X
    val y: Int  // Coordenada Y
)
