package com.fjjukic.zenvio.feature.chat.data.util.markdown

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.HorizontalDivider
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
                Spacer(Modifier.height(10.dp))
            }
        }
    }
}

private val paragraphStyle
    @Composable get() = MaterialTheme.typography.bodyMedium.copy(
        lineHeight = MaterialTheme.typography.bodyMedium.fontSize * 1.35f
    )

@Composable
private fun HeadingBlock(block: MarkdownBlock.Heading) {
    val style = when (block.level) {
        1 -> MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
        2 -> MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        else -> MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold)
    }

    Text(
        text = InlineMarkdown.format(block.text),
        style = style,
        modifier = Modifier.padding(bottom = 6.dp) // extra spacing
    )
}

@Composable
private fun ParagraphBlock(block: MarkdownBlock.Paragraph) {
    Text(
        text = InlineMarkdown.format(block.text),
        style = paragraphStyle
    )
}

@Composable
private fun CodeBlockBlock(block: MarkdownBlock.CodeBlock) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF1E1E1E), RoundedCornerShape(8.dp))
            .padding(12.dp) // ← bigger padding
    ) {
        Text(
            text = block.code,
            style = MaterialTheme.typography.bodySmall.copy(
                color = Color(0xFFDCDCDC),
                lineHeight = MaterialTheme.typography.bodySmall.fontSize * 1.5f
            )
        )
    }
}

@Composable
private fun QuoteBlock(block: MarkdownBlock.BlockQuote) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .width(4.dp)
                .heightIn(min = 0.dp)
                .background(Color(0xFFB0BEC5), RoundedCornerShape(2.dp))
        )
        Spacer(Modifier.width(8.dp))

        Text(
            text = InlineMarkdown.format(block.text),
            style = paragraphStyle.copy(color = Color(0xFF607D8B))
        )
    }
}

@Composable
private fun DividerBlock() {
    HorizontalDivider(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        color = Color(0xFFE0E0E0)
    )
}

@Composable
private fun UnorderedListBlock(block: MarkdownBlock.UnorderedList) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        block.items.forEach { item ->
            Row(verticalAlignment = androidx.compose.ui.Alignment.Top) {

                // Bullet / checkbox symbol
                Text(
                    text = when (item.checked) {
                        null -> "• "
                        true -> "☑ "
                        false -> "☐ "
                    },
                    style = paragraphStyle,
                )

                // Item text
                Text(
                    text = InlineMarkdown.format(item.text),
                    style = paragraphStyle
                )
            }
        }
    }
}

@Composable
private fun OrderedListBlock(block: MarkdownBlock.OrderedList) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        block.items.forEachIndexed { index, item ->
            Row(
                verticalAlignment = androidx.compose.ui.Alignment.Top,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "${index + 1}.",
                    style = paragraphStyle,
                    modifier = Modifier.width(14.dp)
                )

                Text(
                    text = InlineMarkdown.format(item.text),
                    style = paragraphStyle
                )
            }
        }
    }
}