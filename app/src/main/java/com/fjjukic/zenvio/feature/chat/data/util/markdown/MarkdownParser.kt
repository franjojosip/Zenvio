package com.fjjukic.zenvio.feature.chat.data.util.markdown


sealed class MarkdownBlock {
    data class Heading(val level: Int, val text: String) : MarkdownBlock()
    data class Paragraph(val text: String) : MarkdownBlock()
    data class CodeBlock(val language: String?, val code: String) : MarkdownBlock()
    data class BlockQuote(val text: String) : MarkdownBlock()
    data class HorizontalRule(val dummy: Unit = Unit) : MarkdownBlock()
    data class UnorderedList(val items: List<ListItem>) : MarkdownBlock()
    data class OrderedList(val items: List<ListItem>) : MarkdownBlock()
}

data class ListItem(
    val text: String,
    val checked: Boolean? = null // null = normal item, true/false = task item
)

object MarkdownParser {

    fun parse(input: String): List<MarkdownBlock> {
        val lines = input.lines()
        val blocks = mutableListOf<MarkdownBlock>()
        var i = 0

        while (i < lines.size) {
            val rawLine = lines[i]
            val line = rawLine.trimEnd()

            if (line.isBlank()) {
                i++
                continue
            }

            // Code block: ``` or ```lang
            if (line.trimStart().startsWith("```")) {
                val fence = line.trimStart()
                val language = fence.removePrefix("```").takeIf { it.isNotBlank() }
                val codeLines = mutableListOf<String>()
                i++

                while (i < lines.size && !lines[i].trimStart().startsWith("```")) {
                    codeLines += lines[i]
                    i++
                }
                // skip closing fence
                if (i < lines.size) i++

                blocks += MarkdownBlock.CodeBlock(language, codeLines.joinToString("\n"))
                continue
            }

            // Horizontal rule: --- or *** or ___
            if (line.trim() in listOf("---", "***", "___")) {
                blocks += MarkdownBlock.HorizontalRule()
                i++
                continue
            }

            // Heading: # to ######
            val headingMatch = Regex("^(#{1,6})\\s+(.+)$").find(line.trimStart())
            if (headingMatch != null) {
                val level = headingMatch.groupValues[1].length
                val text = headingMatch.groupValues[2]
                blocks += MarkdownBlock.Heading(level, text)
                i++
                continue
            }

            // Blockquote: group consecutive lines starting with ">"
            if (line.trimStart().startsWith(">")) {
                val quoteLines = mutableListOf<String>()
                while (i < lines.size && lines[i].trimStart().startsWith(">")) {
                    quoteLines += lines[i].trimStart().removePrefix(">").trimStart()
                    i++
                }
                blocks += MarkdownBlock.BlockQuote(quoteLines.joinToString("\n"))
                continue
            }

            // Unordered / task list
            val unorderedRegex = Regex("""^\s*([-*+])\s+(.+)$""")
            val taskRegex = Regex("""^\s*[-*+]\s+\[( |x|X)]\s+(.+)$""")
            if (unorderedRegex.matches(line)) {
                val items = mutableListOf<ListItem>()
                while (i < lines.size && unorderedRegex.matches(lines[i].trimEnd())) {
                    val taskMatch = taskRegex.matchEntire(lines[i].trimEnd())
                    if (taskMatch != null) {
                        val checked = when (taskMatch.groupValues[1]) {
                            "x", "X" -> true
                            else -> false
                        }
                        val textItem = taskMatch.groupValues[2]
                        items += ListItem(textItem, checked)
                    } else {
                        val m = unorderedRegex.matchEntire(lines[i].trimEnd())!!
                        val textItem = m.groupValues[2]
                        items += ListItem(textItem)
                    }
                    i++
                }
                blocks += MarkdownBlock.UnorderedList(items)
                continue
            }

            // Ordered list
            val orderedRegex = Regex("""^\s*(\d+)\.\s+(.+)$""")
            if (orderedRegex.matches(line)) {
                val items = mutableListOf<ListItem>()
                while (i < lines.size && orderedRegex.matches(lines[i].trimEnd())) {
                    val m = orderedRegex.matchEntire(lines[i].trimEnd())!!
                    val textItem = m.groupValues[2]
                    items += ListItem(textItem)
                    i++
                }
                blocks += MarkdownBlock.OrderedList(items)
                continue
            }

            // Paragraph: collect until blank line or a new block starts
            val paragraphLines = mutableListOf<String>()
            while (i < lines.size && lines[i].isNotBlank() &&
                !lines[i].trimStart().startsWith("```") &&
                !lines[i].trimStart().startsWith("#") &&
                !lines[i].trimStart().startsWith(">") &&
                !unorderedRegex.matches(lines[i].trimEnd()) &&
                !orderedRegex.matches(lines[i].trimEnd()) &&
                lines[i].trim() !in listOf("---", "***", "___")
            ) {
                paragraphLines += lines[i].trimEnd()
                i++
            }
            val paragraphText = paragraphLines.joinToString("\n").trim()
            if (paragraphText.isNotEmpty()) {
                blocks += MarkdownBlock.Paragraph(paragraphText)
            }
        }

        return blocks
    }
}