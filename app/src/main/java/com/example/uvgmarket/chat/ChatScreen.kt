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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.uvgmarket.chat.components.ChatInputBar
import com.example.uvgmarket.chat.components.MessageBubble
import com.example.uvgmarket.pantallainicio.components.ProfileAvatar
import com.example.uvgmarket.ui.theme.UvgMarketTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    recipientName: String = "Hamburguesas kawaii",
    recipientProfileImage: String? = "fotodeperfilhamburger",
    currentUserId: String = "currentUser",
    recipientId: String = "otherUser",
    onBackClick: () -> Unit = {},
    chatViewModel: ChatViewModel = viewModel()
) {
    val messages by chatViewModel.messages.collectAsState()
    val messageText by chatViewModel.messageText.collectAsState()
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    // Auto-scroll al último mensaje
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            coroutineScope.launch {
                listState.animateScrollToItem(messages.size - 1)
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {

        // Header
        ChatHeader(
            recipientName = recipientName,
            recipientProfileImage = recipientProfileImage,
            onBackClick = onBackClick
        )

        // Lista de mensajes
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(Color(0xFFE5E5E5)),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(
                items = messages,
                key = { it.id }
            ) { message ->
                MessageBubble(
                    message = message,
                    isCurrentUser = message.senderId == currentUserId
                )
            }
        }

        // Barra de entrada de mensajes
        ChatInputBar(
            messageText = messageText,
            onMessageTextChange = { chatViewModel.onMessageTextChange(it) },
            onSendClick = { chatViewModel.sendMessage(currentUserId, recipientId) },
            modifier = Modifier.navigationBarsPadding()
        )
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
                        color = Color.White
                    )
                    Text(
                        text = "Online",
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
            }
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver",
                    tint = Color.White
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
