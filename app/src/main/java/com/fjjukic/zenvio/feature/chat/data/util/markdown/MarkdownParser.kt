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
    val checked: Boolean? = null
)
object MarkdownParser {

    // Precompiled regexes (much faster)
    private val headingRegex = Regex("^(#{1,6})\\s+(.+)$")
    private val unorderedRegex = Regex("""^\s*([-*+])\s+(.+)$""")
    private val taskRegex = Regex("""^\s*[-*+]\s+\[( |x|X)]\s+(.+)$""")
    private val orderedRegex = Regex("""^\s*(\d+)\.\s+(.+)$""")
    private val hrRegex = Regex("""^\s*(-{3,}|_{3,}|\*{3,})\s*$""")

    fun parse(input: String): List<MarkdownBlock> {
        val lines = input.lines()
        val blocks = mutableListOf<MarkdownBlock>()
        var i = 0

        while (i < lines.size) {
            val raw = lines[i]
            val line = raw.trimEnd()

            if (line.isBlank()) {
                i++
                continue
            }

            val trimmed = line.trimStart()

            // -------------------------------------------------------------
            // CODE BLOCK
            // -------------------------------------------------------------
            if (trimmed.startsWith("```")) {
                val lang = trimmed.removePrefix("```").ifBlank { null }
                i++
                val codeLines = mutableListOf<String>()

                while (i < lines.size && !lines[i].trimStart().startsWith("```")) {
                    codeLines += lines[i]
                    i++
                }
                if (i < lines.size) i++ // skip closing ```

                blocks += MarkdownBlock.CodeBlock(lang, codeLines.joinToString("\n"))
                continue
            }

            // -------------------------------------------------------------
            // HORIZONTAL RULE
            // -------------------------------------------------------------
            if (hrRegex.matches(trimmed)) {
                blocks += MarkdownBlock.HorizontalRule()
                i++
                continue
            }

            // -------------------------------------------------------------
            // HEADING
            // -------------------------------------------------------------
            val headingMatch = headingRegex.matchEntire(trimmed)
            if (headingMatch != null) {
                val level = headingMatch.groupValues[1].length
                val text = headingMatch.groupValues[2]
                blocks += MarkdownBlock.Heading(level, text)
                i++
                continue
            }

            // -------------------------------------------------------------
            // BLOCKQUOTE
            // -------------------------------------------------------------
            if (trimmed.startsWith(">")) {
                val quoteLines = mutableListOf<String>()
                while (i < lines.size && lines[i].trimStart().startsWith(">")) {
                    quoteLines += lines[i]
                        .trimStart()
                        .removePrefix(">")
                        .trimStart()
                    i++
                }
                blocks += MarkdownBlock.BlockQuote(quoteLines.joinToString("\n"))
                continue
            }

            // -------------------------------------------------------------
            // UNORDERED / TASK LIST
            // -------------------------------------------------------------
            if (unorderedRegex.matches(trimmed)) {
                val items = mutableListOf<ListItem>()

                while (i < lines.size && unorderedRegex.matches(lines[i].trimStart())) {
                    val t = lines[i].trimStart()

                    taskRegex.matchEntire(t)?.let { task ->
                        val checked = task.groupValues[1].equals("x", ignoreCase = true)
                        val text = task.groupValues[2]
                        items += ListItem(text, checked)
                    } ?: run {
                        val match = unorderedRegex.matchEntire(t)!!
                        items += ListItem(match.groupValues[2])
                    }
                    i++
                }

                blocks += MarkdownBlock.UnorderedList(items)
                continue
            }

            // -------------------------------------------------------------
            // ORDERED LIST
            // -------------------------------------------------------------
            if (orderedRegex.matches(trimmed)) {
                val items = mutableListOf<ListItem>()

                while (i < lines.size && orderedRegex.matches(lines[i].trimStart())) {
                    val match = orderedRegex.matchEntire(lines[i].trimStart())!!
                    items += ListItem(match.groupValues[2])
                    i++
                }

                blocks += MarkdownBlock.OrderedList(items)
                continue
            }

            // -------------------------------------------------------------
            // PARAGRAPH
            // -------------------------------------------------------------
            val paragraphLines = mutableListOf<String>()

            while (
                i < lines.size &&
                lines[i].isNotBlank() &&
                !lines[i].trimStart().startsWith("```") &&
                !headingRegex.matches(lines[i].trimStart()) &&
                !lines[i].trimStart().startsWith(">") &&
                !unorderedRegex.matches(lines[i].trimStart()) &&
                !orderedRegex.matches(lines[i].trimStart()) &&
                !hrRegex.matches(lines[i].trimStart())
            ) {
                paragraphLines += lines[i].trimEnd()
                i++
            }

            val paragraph = paragraphLines.joinToString("\n").trim()
            if (paragraph.isNotEmpty()) {
                blocks += MarkdownBlock.Paragraph(paragraph)
            }
        }

        return blocks
    }
}