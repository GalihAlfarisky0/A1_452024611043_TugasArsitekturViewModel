package com.example.unscramble.ui

import androidx.lifecycle.ViewModel
import com.example.unscramble.data.allWords
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class GameViewModel : ViewModel() {

    companion object {
        const val MAX_NO_OF_WORDS = 10
        const val SCORE_INCREASE = 20
    }

    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    private val usedWords = mutableSetOf<String>()
    private var currentWord = ""

    init {
        resetGame()
    }

    private fun pickRandomWordAndShuffle(): String {
        if (usedWords.size == allWords.size) {
            usedWords.clear()
        }

        var word: String
        do {
            word = allWords.random()
        } while (usedWords.contains(word))

        usedWords.add(word)
        currentWord = word

        return word.toCharArray().apply { shuffle() }.concatToString()
    }

    fun resetGame() {
        usedWords.clear()
        val scrambledWord = pickRandomWordAndShuffle()

        _uiState.value = GameUiState(
            currentScrambledWord = scrambledWord,
            currentWordCount = 1,
            score = 0,
            isGuessedWordWrong = false,
            isGameOver = false
        )
    }

    fun checkUserGuess(userGuess: String) {
        if (userGuess.trim().equals(currentWord, ignoreCase = true)) {
            val updatedScore = _uiState.value.score + SCORE_INCREASE
            _uiState.value = _uiState.value.copy(
                score = updatedScore,
                isGuessedWordWrong = false
            )
            updateGameState()
        } else {
            _uiState.value = _uiState.value.copy(isGuessedWordWrong = true)
        }
    }

    fun skipWord() {
        updateGameState()
    }

    private fun updateGameState() {
        val currentCount = _uiState.value.currentWordCount

        if (currentCount >= MAX_NO_OF_WORDS) {
            _uiState.value = _uiState.value.copy(isGameOver = true)
        } else {
            val scrambledWord = pickRandomWordAndShuffle()
            _uiState.value = _uiState.value.copy(
                currentScrambledWord = scrambledWord,
                currentWordCount = currentCount + 1,
                isGuessedWordWrong = false
            )
        }
    }
}
