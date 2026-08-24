package com.example.unscramble.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun GameScreen(
    gameViewModel: GameViewModel,
    modifier: Modifier = Modifier
) {
    val gameUiState by gameViewModel.uiState.collectAsStateWithLifecycle()

    GameLayout(
        gameUiState = gameUiState,
        onSubmit = { guess -> gameViewModel.checkUserGuess(guess) },
        onSkip = { gameViewModel.skipWord() },
        onRestart = { gameViewModel.resetGame() },
        modifier = modifier
    )
}

@Composable
fun GameLayout(
    gameUiState: GameUiState,
    onSubmit: (String) -> Unit,
    onSkip: () -> Unit,
    onRestart: () -> Unit,
    modifier: Modifier = Modifier
) {
    var userGuess by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Unscramble",
                style = MaterialTheme.typography.headlineLarge
            )

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Score: ${gameUiState.score}",
                        style = MaterialTheme.typography.titleLarge
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "${gameUiState.currentWordCount}/10",
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = gameUiState.currentScrambledWord,
                        style = MaterialTheme.typography.displayMedium,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    OutlinedTextField(
                        value = userGuess,
                        onValueChange = { userGuess = it },
                        singleLine = true,
                        label = { Text("Masukkan jawaban") },
                        supportingText = {
                            if (gameUiState.isGuessedWordWrong) {
                                Text("Jawaban salah!")
                            }
                        },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = { onSubmit(userGuess) }),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                onSkip()
                                userGuess = ""
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Skip")
                        }

                        Button(
                            onClick = {
                                onSubmit(userGuess)
                                userGuess = ""
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Submit")
                        }
                    }
                }
            }
        }

        if (gameUiState.isGameOver) {
            GameOverDialog(score = gameUiState.score, onRestart = onRestart)
        }
    }
}

@Composable
fun GameOverDialog(
    score: Int,
    onRestart: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Game Over",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Score kamu: $score")
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onRestart) {
                Text("Play Again")
            }
        }
    }
}
