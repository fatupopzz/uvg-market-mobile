package com.example.uvgmarket.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.uvgmarket.chat.components.ChatInputBar
import com.example.uvgmarket.chat.components.MessageBubble
import com.example.uvgmarket.pantallainicio.components.ProfileAvatar
import com.example.uvgmarket.ui.theme.UvgMarketTheme
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    otherUserId: String? = null,
    onBackClick: () -> Unit = {},
    chatViewModel: ChatViewModel = viewModel()
) {
    val uiState by chatViewModel.uiState.collectAsState()
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    // Inicializar chat cuando se pasa otherUserId
    LaunchedEffect(otherUserId) {
        if (otherUserId != null) {
            chatViewModel.initializeChat(otherUserId)
        }
    }

    // Auto-scroll al último mensaje
    LaunchedEffect(uiState.messages.size) {
        if (uiState.messages.isNotEmpty()) {
            coroutineScope.launch {
                listState.animateScrollToItem(uiState.messages.size - 1)
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header
            ChatHeader(
                recipientName = uiState.otherUserName.ifEmpty { "Usuario" },
                recipientProfileImage = uiState.otherUserImage,
                onBackClick = onBackClick
            )

            // Mostrar loading o error
            when {
                uiState.isLoading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                uiState.error != null -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = uiState.error ?: "",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
                else -> {
                    // Lista de mensajes
                    LazyColumn(
                        state = listState,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                        contentPadding = PaddingValues(vertical = 8.dp)
                    ) {
                        items(
                            items = uiState.messages,
                            key = { it.id }
                        ) { message ->
                            MessageBubble(
                                message = message,
                                isCurrentUser = message.senderId == currentUserId
                            )
                        }
                    }
                }
            }

            // Barra de entrada de mensajes
            ChatInputBar(
                messageText = uiState.messageText,
                onMessageTextChange = { chatViewModel.onMessageTextChange(it) },
                onSendClick = {
                    if (otherUserId != null) {
                        chatViewModel.sendMessage(otherUserId)
                    }
                },
                modifier = Modifier.navigationBarsPadding(),
                enabled = !uiState.isLoading && uiState.chatId != null
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChatHeader(
    recipientName: String,
    recipientProfileImage: String?,
    onBackClick: () -> Unit
) {
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ProfileAvatar(size = 40, profileImage = recipientProfileImage)
                Column {
                    Text(
                        text = recipientName,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onTertiary
                    )
                }
            }
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver",
                    tint = MaterialTheme.colorScheme.onTertiary
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.tertiary
        )
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ChatScreenPreview() {
    UvgMarketTheme {
        ChatScreen()
    }
}