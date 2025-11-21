package com.fjjukic.zenvio.feature.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fjjukic.zenvio.R
import com.fjjukic.zenvio.feature.chat.model.ChatIntent
import com.fjjukic.zenvio.feature.chat.model.ChatMessage
import com.fjjukic.zenvio.ui.defaults.AppInputDefaults
import com.fjjukic.zenvio.ui.theme.ZenvioTheme

@Composable
fun ChatScreen(
    viewModel: ChatViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
        ) {
            LazyColumn(
                modifier = Modifier.weight(1f),
                reverseLayout = false,
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                items(state.messages) { msg ->
                    when (msg) {
                        is ChatMessage.User -> UserMessage(msg.text)
                        is ChatMessage.Assistant -> AssistantMessage(msg.text)
                        ChatMessage.Loading -> LoadingBubble()
                    }
                }
            }

            MessageInput(
                text = state.input,
                onTextChanged = { viewModel.onIntent(ChatIntent.InputChanged(it)) },
                onSend = { viewModel.onIntent(ChatIntent.SendMessage) }
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun UserMessagePreview() {
    ZenvioTheme {
        UserMessage(stringResource(R.string.title_walkthrough_step_one))
    }
}

@Composable
fun UserMessage(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 60.dp),
        horizontalArrangement = Arrangement.End
    ) {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(12.dp))
                .padding(start = 12.dp, top = 12.dp, end = 12.dp, bottom = 12.dp)
        ) {
            Text(
                text,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onPrimary,
                    textAlign = TextAlign.Start,
                    lineHeight = 28.sp
                )
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun AssistantMessagePreview() {
    ZenvioTheme {
        AssistantMessage(stringResource(R.string.title_walkthrough_step_one))
    }
}


@Composable
fun AssistantMessage(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 60.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .background(AppInputDefaults.boxFillColor, RoundedCornerShape(12.dp))
                .padding(start = 12.dp, top = 12.dp, end = 12.dp, bottom = 12.dp)
        ) {
            Text(
                text,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Normal,
                    color = Color.Black.copy(alpha = 0.85f),
                    textAlign = TextAlign.Start,
                    lineHeight = 28.sp
                )
            )
        }
    }
}

@Composable
fun LoadingBubble() {
    Text("...", modifier = Modifier.padding(12.dp))
}

@Composable
fun MessageInput(
    text: String,
    onTextChanged: (String) -> Unit,
    onSend: () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(8.dp)
            .background(Color(0xFFF1F1F1), RoundedCornerShape(25.dp)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = text,
            onValueChange = onTextChanged,
            modifier = Modifier.weight(1f),
            placeholder = { Text("Type a message...") },
//            colors = TextFieldDefaults.textFieldColors(
//                containerColor = Color.Transparent,
//                focusedIndicatorColor = Color.Transparent,
//                unfocusedIndicatorColor = Color.Transparent
//            )
        )

        IconButton(onClick = onSend) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Send,
                contentDescription = null,
                tint = Color(0xFF85AF55)
            )
        }
    }
}
