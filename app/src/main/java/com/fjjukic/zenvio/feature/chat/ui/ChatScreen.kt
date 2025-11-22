package com.fjjukic.zenvio.feature.chat.ui

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateValue
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fjjukic.zenvio.R
import com.fjjukic.zenvio.core.util.asString
import com.fjjukic.zenvio.feature.chat.ChatViewModel
import com.fjjukic.zenvio.feature.chat.data.util.markdown.MarkdownMessage
import com.fjjukic.zenvio.feature.chat.model.ChatIntent
import com.fjjukic.zenvio.feature.chat.model.ChatMessage
import com.fjjukic.zenvio.feature.chat.model.ChatRole
import com.fjjukic.zenvio.feature.chat.model.ChatStateUi
import com.fjjukic.zenvio.feature.chat.model.UiText
import com.fjjukic.zenvio.ui.theme.ZenvioTheme
import kotlinx.coroutines.launch

@Composable
fun ChatScreen(
    viewModel: ChatViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ChatScreenStateless(
        state = uiState,
        onEvent = viewModel::onIntent,
        onNavigateBack = onNavigateBack
    )
}

@Composable
fun ChatScreenStateless(
    state: ChatStateUi,
    onEvent: (ChatIntent) -> Unit,
    onNavigateBack: () -> Unit
) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    // This effect correctly scrolls to the bottom when a new message appears
    LaunchedEffect(state.messages.size) {
        if (state.messages.isNotEmpty()) {
            coroutineScope.launch {
                listState.animateScrollToItem(state.messages.lastIndex)
            }
        }
    }

    Scaffold(
        topBar = {
            ChatTopBar(
                onNavigateBack = onNavigateBack,
                onEvent = onEvent
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = innerPadding.calculateTopPadding())
                    .navigationBarsPadding()
                    .imePadding()
            ) {

                Box(modifier = Modifier.weight(1f)) {
                    MessageList(
                        messages = state.messages,
                        listState = listState,
                        modifier = Modifier.fillMaxSize()
                    )
                    if (state.isLoading) {
                        LoadingBubble(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(bottom = 20.dp)
                        )
                    }
                }

                HorizontalDivider(
                    thickness = 1.dp,
                    color = Color.LightGray.copy(alpha = 0.3f)
                )
                MessageInput(
                    text = state.input,
                    onTextChanged = { onEvent(ChatIntent.InputChanged(it)) },
                    onSend = { onEvent(ChatIntent.SendMessage) }
                )
            }
        },
        containerColor = Color.White
    )
}

@Composable
fun MessageList(
    messages: List<ChatMessage>,
    listState: LazyListState,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        state = listState,
        modifier = modifier
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(
            top = 16.dp,
            bottom = 60.dp
        )
    ) {
        items(messages, key = { it.id }) { msg ->
            when (msg) {
                is ChatMessage.Standard ->
                    if (msg.role == ChatRole.USER) {
                        UserMessage(msg.content)
                    } else {
                        AssistantMessage(msg.content)
                    }
            }
        }
    }
}

@Composable
fun UserMessage(uiText: UiText) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        Box(
            modifier = Modifier
                .widthIn(max = LocalConfiguration.current.screenWidthDp.dp * 0.7f)
                .background(
                    MaterialTheme.colorScheme.primary,
                    RoundedCornerShape(8.dp, 0.dp, 8.dp, 8.dp)
                )
                .padding(14.dp)
        ) {
            Text(
                uiText.asString(),
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onPrimary,
                    lineHeight = 25.sp
                )
            )
        }
    }
}

@Composable
fun AssistantMessage(text: UiText) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .widthIn(max = LocalConfiguration.current.screenWidthDp.dp * 0.7f)
                .background(Color(0xFFEFEFEF), RoundedCornerShape(0.dp, 8.dp, 8.dp, 8.dp))
                .padding(14.dp)
        ) {
            MarkdownMessage(text.asString())
        }
    }
}

@Composable
fun LoadingBubble(
    modifier: Modifier = Modifier
) {
    val bounceUp = (-8).dp
    val bounceDown = 0.dp
    val infiniteTransition = rememberInfiniteTransition(label = "loading-bubble-transition")
    val delays = listOf(0, 150, 300)
    val animatedValues = delays.map { delay ->
        infiniteTransition.animateValue(
            initialValue = bounceDown,
            targetValue = bounceUp,
            typeConverter = Dp.VectorConverter,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 400, easing = LinearOutSlowInEasing),
                repeatMode = RepeatMode.Reverse,
                initialStartOffset = StartOffset(delay)
            ),
            label = "dot-bounce-$delay"
        )
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .shadow(
                    elevation = 3.dp,
                    shape = RoundedCornerShape(8.dp),
                    clip = false
                )
                .widthIn(max = LocalConfiguration.current.screenWidthDp.dp * 0.7f)
                .border(
                    width = 1.dp,
                    color = Color.LightGray.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(8.dp)
                )
                .background(Color.White, RoundedCornerShape(8.dp))
                .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                animatedValues.forEach { animatedValue ->
                    Box(
                        modifier = Modifier
                            .graphicsLayer { translationY = animatedValue.value.toPx() }
                            .size(8.dp)
                            .background(
                                color = Color.Gray.copy(alpha = 0.3f),
                                shape = CircleShape
                            )
                    )
                }
            }
        }
    }
}


@Composable
fun MessageInput(
    text: String,
    onTextChanged: (String) -> Unit,
    onSend: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = text,
            onValueChange = onTextChanged,
            modifier = Modifier.weight(1f),
            placeholder = { Text(stringResource(R.string.label_type_a_message)) },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFEFEFEF),
                unfocusedContainerColor = Color(0xFFEFEFEF),
                unfocusedPlaceholderColor = Color.Gray,
                focusedPlaceholderColor = Color.Gray,
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent
            ),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
            keyboardActions = KeyboardActions(onSend = { if (text.isNotBlank()) onSend() })
        )
        Spacer(modifier = Modifier.size(8.dp))
        IconButton(
            onClick = onSend,
            enabled = text.isNotBlank(),
            modifier = Modifier
                .size(48.dp)
                .background(
                    color = if (text.isNotBlank()) MaterialTheme.colorScheme.primary else Color.LightGray,
                    shape = RoundedCornerShape(50)
                )
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Send,
                contentDescription = stringResource(R.string.cd_send_message),
                tint = if (text.isNotBlank()) Color.White else Color.DarkGray,
                modifier = Modifier.graphicsLayer { translationX = 5f }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatTopBar(
    onNavigateBack: () -> Unit,
    onEvent: (ChatIntent) -> Unit
) {
    var menuExpanded by remember { mutableStateOf(false) }
    CenterAlignedTopAppBar(
        title = {
            Text(
                stringResource(R.string.title_chat_with_zenvio),
                fontWeight = FontWeight.Bold
            )
        },
        navigationIcon = {
            IconButton(onClick = onNavigateBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, stringResource(R.string.cd_navigate_back))
            }
        },
        actions = {
            IconButton(onClick = { menuExpanded = true }) {
                Icon(Icons.Default.MoreVert, stringResource(R.string.cd_more_actions))
            }
            DropdownMenu(expanded = menuExpanded, onDismissRequest = { menuExpanded = false }) {
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.label_search)) },
                    onClick = { onEvent(ChatIntent.Search); menuExpanded = false },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Search,
                            stringResource(R.string.cd_search_in_chat)
                        )
                    }
                )
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.label_export_chat)) },
                    onClick = { onEvent(ChatIntent.ExportChat); menuExpanded = false },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Share,
                            stringResource(R.string.cd_export_chat)
                        )
                    }
                )
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.label_clear_chat)) },
                    onClick = { onEvent(ChatIntent.ClearChat); menuExpanded = false },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Delete,
                            stringResource(R.string.cd_clear_chat)
                        )
                    }
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.White,
            scrolledContainerColor = Color.White
        ),
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ChatScreenContentPreview() {
    ZenvioTheme {
        val sampleMessages = listOf(
            ChatMessage.Standard(
                role = ChatRole.ASSISTANT,
                content = UiText.StringResource(R.string.assistant_intro_message)
            ),
            ChatMessage.Standard(
                role = ChatRole.USER,
                content = UiText.StringResource(R.string.user_example_message)
            )
        )
        ChatScreenStateless(
            state = ChatStateUi(
                messages = sampleMessages,
                input = stringResource(R.string.plan_intro_meditation)
            ),
            onEvent = {},
            onNavigateBack = {}
        )
    }
}
