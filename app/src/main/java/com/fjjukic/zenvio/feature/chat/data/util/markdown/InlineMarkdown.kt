package com.fjjukic.zenvio.feature.chat.data.util.markdown

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration

object InlineMarkdown {

    fun format(text: String): AnnotatedString = buildAnnotatedString {
        var i = 0
        val n = text.length

        fun safeStartsWith(token: String, pos: Int): Boolean {
            if (pos + token.length > n) return false
            return text.startsWith(token, pos)
        }

        fun findClosing(token: String, start: Int): Int {
            return text.indexOf(token, start + token.length)
        }

        while (i < n) {

            // --- STRIKETHROUGH: ~~text~~ ---
            if (safeStartsWith("~~", i)) {
                val end = findClosing("~~", i)
                if (end != -1) {
                    pushStyle(SpanStyle(textDecoration = TextDecoration.LineThrough))
                    append(text.substring(i + 2, end))
                    pop()
                    i = end + 2
                    continue
                }
            }

            // --- BOLD: **text** or __text__ ---
            if (safeStartsWith("**", i) || safeStartsWith("__", i)) {
                val token = text.substring(i, i + 2)
                val end = findClosing(token, i)
                if (end != -1) {
                    pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
                    append(text.substring(i + 2, end))
                    pop()
                    i = end + 2
                    continue
                }
            }

            // --- ITALIC: *text* or _text_ ---
            // Ensure this is not a list item "* item"
            val isListItem = (i == 0 || text[i - 1] == '\n') &&
                    (safeStartsWith("* ", i) || safeStartsWith("- ", i))

            if (!isListItem && (safeStartsWith("*", i) || safeStartsWith("_", i))) {
                val token = text.substring(i, i + 1)
                val end = text.indexOf(token, i + 1)
                if (end != -1) {
                    pushStyle(SpanStyle(fontStyle = FontStyle.Italic))
                    append(text.substring(i + 1, end))
                    pop()
                    i = end + 1
                    continue
                }
            }

            // --- INLINE CODE: `code` ---
            if (safeStartsWith("`", i)) {
                val end = text.indexOf("`", i + 1)
                if (end != -1) {
                    pushStyle(
                        SpanStyle(
                            background = Color(0xFFEAEAEA),
                            color = Color(0xFF333333)
                        )
                    )
                    append(text.substring(i + 1, end))
                    pop()
                    i = end + 1
                    continue
                }
            }

            // --- LINKS: [label](url) ---
            if (safeStartsWith("[", i)) {
                val closeBracket = text.indexOf("]", i + 1)
                if (closeBracket != -1 && closeBracket + 1 < n && text[closeBracket + 1] == '(') {
                    val closeParen = text.indexOf(")", closeBracket + 2)
                    if (closeParen != -1) {
                        val label = text.substring(i + 1, closeBracket)
                        val url = text.substring(closeBracket + 2, closeParen)

                        pushStringAnnotation(tag = "URL", annotation = url)
                        pushStyle(
                            SpanStyle(
                                color = Color(0xFF2962FF),
                                textDecoration = TextDecoration.Underline
                            )
                        )
                        append(label)
                        pop()
                        pop()

                        i = closeParen + 1
                        continue
                    }
                }
            }

            // --- FALLBACK ---
            append(text[i])
            i++
        }
    }
}