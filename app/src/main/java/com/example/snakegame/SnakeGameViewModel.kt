package com.example.snakegame

import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Clase ViewModel que gestiona el estado y los eventos del juego de la serpiente
class SnakeGameViewModel : ViewModel() {

    // Estado del juego, que se maneja mediante un flujo mutable
    private val _state = MutableStateFlow(SnakeGameState())
    val state = _state.asStateFlow() // Estado observable

    // Función que maneja los eventos que ocurren en el juego
    fun onEvent(event: SnakeGameEvent) {
        when (event) {
            // Cuando se inicia el juego
            SnakeGameEvent.StartGame -> {
                _state.update { it.copy(gameState = GameState.STARTED) }
                viewModelScope.launch {
                    // Mientras el juego esté iniciado, mueve la serpiente
                    while (state.value.gameState == GameState.STARTED) {
                        // Ajusta la velocidad de la serpiente según su tamaño
                        val delayMillis = when (state.value.snake.size) {
                            in 1..5 -> 120L
                            in 6..10 -> 110L
                            else -> 100L
                        }
                        delay(delayMillis) // Espera para hacer el movimiento
                        _state.value = updateGame(state.value) // Actualiza el estado del juego
                    }
                }
            }

            // Cuando se pausa el juego
            SnakeGameEvent.PauseGame -> {
                _state.update { it.copy(gameState = GameState.PAUSED) }
            }

            // Cuando se reinicia el juego
            SnakeGameEvent.ResetGame -> {
                _state.value = SnakeGameState()
            }

            // Cuando se actualiza la dirección de la serpiente
            is SnakeGameEvent.UpdateDirection -> {
                updateDirection(event.offset, event.canvasWidth)
            }
        }
    }

    // Función que actualiza la dirección de la serpiente según el toque del usuario
    private fun updateDirection(offset: Offset, canvasWidth: Int) {
        if (!state.value.isGameOver) {
            // Calcula el tamaño de cada celda en la cuadrícula
            val cellSize = canvasWidth / state.value.xAxisGridSize
            val tapX = (offset.x / cellSize).toInt() // Calcula la posición en X donde se tocó
            val tapY = (offset.y / cellSize).toInt() // Calcula la posición en Y donde se tocó
            val head = state.value.snake.first() // Obtiene la cabeza de la serpiente

            // Actualiza la dirección en función de la posición tocada
            _state.update {
                it.copy(
                    direction = when (state.value.direction) {
                        Direction.UP, Direction.DOWN -> {
                            if (tapX < head.x) Direction.LEFT else Direction.RIGHT
                        }

                        Direction.LEFT, Direction.RIGHT -> {
                            if (tapY < head.y) Direction.UP else Direction.DOWN
                        }
                    }
                )
            }
        }
    }

    // Función que actualiza el estado del juego (movimiento de la serpiente, colisiones, comida, etc.)
    private fun updateGame(currentGame: SnakeGameState): SnakeGameState {
        if (currentGame.isGameOver) {
            return currentGame // Si el juego terminó, no actualiza el estado
        }

        val head = currentGame.snake.first() // Obtiene la cabeza de la serpiente
        val xAxisGridSize = currentGame.xAxisGridSize // Tamaño en el eje X de la cuadrícula
        val yAxisGridSize = currentGame.yAxisGridSize // Tamaño en el eje Y de la cuadrícula

        // Actualiza el movimiento de la serpiente según la dirección
        val newHead = when (currentGame.direction) {
            Direction.UP -> Coordinate(x = head.x, y = (head.y - 1))
            Direction.DOWN -> Coordinate(x = head.x, y = (head.y + 1))
            Direction.LEFT -> Coordinate(x = head.x - 1, y = (head.y))
            Direction.RIGHT -> Coordinate(x = head.x + 1, y = (head.y))
        }

        // Verifica si la serpiente colisiona consigo misma o sale de los límites
        if (
            currentGame.snake.contains(newHead) ||
            !isWithinBounds(newHead, xAxisGridSize, yAxisGridSize)
        ) {
            return currentGame.copy(isGameOver = true) // Si colisiona o sale de los límites, termina el juego
        }

        // Verifica si la serpiente ha comido la comida
        var newSnake = mutableListOf(newHead) + currentGame.snake
        val newFood = if (newHead == currentGame.food) SnakeGameState.generateRandomFoodCoordinate()
        else currentGame.food

        // Si no ha comido la comida, elimina la última parte de la serpiente
        if (newHead != currentGame.food) {
            newSnake = newSnake.toMutableList()
            newSnake.removeAt(newSnake.size - 1)
        }

        // Devuelve el nuevo estado del juego
        return currentGame.copy(snake = newSnake, food = newFood)
    }

    // Función que verifica si una coordenada está dentro de los límites del juego
    private fun isWithinBounds(
        coordinate: Coordinate,
        xAxisGridSize: Int,
        yAxisGridSize: Int
    ): Boolean {
        return coordinate.x in 1 until xAxisGridSize - 1
                && coordinate.y in 1 until yAxisGridSize - 1
    }
}
