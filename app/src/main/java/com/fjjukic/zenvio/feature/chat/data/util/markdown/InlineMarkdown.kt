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
        val s = text

        fun matchAround(token: String, start: Int): Int {
            return s.indexOf(token, start + token.length)
        }

        while (i < s.length) {
            // Bold: **text** or __text__
            if (s.startsWith("**", i) || s.startsWith("__", i)) {
                val token = s.substring(i, i + 2)
                val end = matchAround(token, i)
                if (end != -1) {
                    pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
                    append(s.substring(i + 2, end))
                    pop()
                    i = end + 2
                    continue
                }
            }

            // Italic: *text* or _text_
            if (s.startsWith("*", i) || s.startsWith("_", i)) {
                val token = s.substring(i, i + 1)
                val end = s.indexOf(token, i + 1)
                if (end != -1) {
                    pushStyle(SpanStyle(fontStyle = FontStyle.Italic))
                    append(s.substring(i + 1, end))
                    pop()
                    i = end + 1
                    continue
                }
            }

            // Strikethrough: ~~text~~
            if (s.startsWith("~~", i)) {
                val end = s.indexOf("~~", i + 2)
                if (end != -1) {
                    pushStyle(SpanStyle(textDecoration = TextDecoration.LineThrough))
                    append(s.substring(i + 2, end))
                    pop()
                    i = end + 2
                    continue
                }
            }

            // Inline code: `code`
            if (s.startsWith("`", i)) {
                val end = s.indexOf("`", i + 1)
                if (end != -1) {
                    pushStyle(
                        SpanStyle(
                            background = Color(0xFFEAEAEA),
                            color = Color(0xFF333333)
                        )
                    )
                    append(s.substring(i + 1, end))
                    pop()
                    i = end + 1
                    continue
                }
            }

            // Link: [text](url)
            if (s.startsWith("[", i)) {
                val closeBracket = s.indexOf("]", i)
                val openParen = s.indexOf("(", closeBracket)
                val closeParen = s.indexOf(")", openParen)

                if (closeBracket != -1 && openParen == closeBracket + 1 && closeParen != -1) {
                    val label = s.substring(i + 1, closeBracket)
                    val url = s.substring(openParen + 1, closeParen)

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

            // Fallback: normal char
            append(s[i])
            i++
        }
    }
}
