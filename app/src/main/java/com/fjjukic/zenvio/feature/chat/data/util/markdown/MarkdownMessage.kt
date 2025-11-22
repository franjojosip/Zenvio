package com.fjjukic.zenvio.feature.chat.data.util.markdown

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun MarkdownMessage(
    text: String,
    modifier: Modifier = Modifier
) {
    val blocks = remember(text) { MarkdownParser.parse(text) }

    Column(modifier = modifier) {
        blocks.forEachIndexed { index, block ->
            when (block) {
                is MarkdownBlock.Heading -> HeadingBlock(block)
                is MarkdownBlock.Paragraph -> ParagraphBlock(block)
                is MarkdownBlock.CodeBlock -> CodeBlockBlock(block)
                is MarkdownBlock.BlockQuote -> QuoteBlock(block)
                is MarkdownBlock.HorizontalRule -> DividerBlock()
                is MarkdownBlock.UnorderedList -> UnorderedListBlock(block)
                is MarkdownBlock.OrderedList -> OrderedListBlock(block)
            }

            if (index != blocks.lastIndex) {
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}

@Composable
private fun HeadingBlock(block: MarkdownBlock.Heading) {
    val style = when (block.level) {
        1 -> MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
        2 -> MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        else -> MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold)
    }
    Text(
        text = InlineMarkdown.format(block.text),
        style = style
    )
}

@Composable
private fun ParagraphBlock(block: MarkdownBlock.Paragraph) {
    Text(
        text = InlineMarkdown.format(block.text),
        style = MaterialTheme.typography.bodyMedium
    )
}

@Composable
private fun CodeBlockBlock(block: MarkdownBlock.CodeBlock) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF1E1E1E), RoundedCornerShape(6.dp))
            .padding(8.dp)
    ) {
        Text(
            text = block.code,
            style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFFDCDCDC))
        )
    }
}

@Composable
private fun QuoteBlock(block: MarkdownBlock.BlockQuote) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .width(4.dp)
                .heightIn(min = 0.dp)
                .background(Color(0xFFB0BEC5), RoundedCornerShape(2.dp))
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = InlineMarkdown.format(block.text),
            style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF607D8B))
        )
    }
}

@Composable
private fun DividerBlock() {
    Divider(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        color = Color(0xFFE0E0E0)
    )
}

@Composable
private fun UnorderedListBlock(block: MarkdownBlock.UnorderedList) {
    Column {
        block.items.forEach { item ->
            Row {
                Text(text = if (item.checked == null) "• " else if (item.checked) "☑ " else "☐ ")
                Text(
                    text = InlineMarkdown.format(item.text),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
private fun OrderedListBlock(block: MarkdownBlock.OrderedList) {
    Column {
        block.items.forEachIndexed { index, item ->
            Row {
                Text(text = "${index + 1}. ")
                Text(
                    text = InlineMarkdown.format(item.text),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}