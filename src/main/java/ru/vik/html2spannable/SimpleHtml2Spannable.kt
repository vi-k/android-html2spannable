package ru.vik.html2spannable

import ru.vik.html.*

class SimpleHtml2Spannable(density: Float = 1.0f,
                           fontDensity: Float = 1.0f)
    : Html2Spannable(density, fontDensity) {

    init {
        addTag("b", SpanConfig(type = Tag.Type.CHARACTER,
                onCreateSpan = { InlineSpan(bold = true) }))

        addTag("br", SpanConfig(type = Tag.Type.BR))

        addTag("div", SpanConfig(type = Tag.Type.PARAGRAPH))

        addTag("h1", SpanConfig(type = Tag.Type.PARAGRAPH,
//                onTextTransform = { it.toUpperCase() },
                onCreateSpan = {
                    BlockSpan(textSizeScale = 1.6F, bold = true)
                }))

        addTag("h2", SpanConfig(type = Tag.Type.PARAGRAPH,
//                onTextTransform = { it.toUpperCase() },
                onCreateSpan = {
                    BlockSpan(textSizeScale = 1.4F, bold = true)
                }))

        addTag("h3", SpanConfig(type = Tag.Type.PARAGRAPH,
//                onTextTransform = { it.toUpperCase() },
                onCreateSpan = {
                    BlockSpan(textSizeScale = 1.2F, bold = true)
                }))

        addTag("i", SpanConfig(type = Tag.Type.CHARACTER,
                onCreateSpan = { InlineSpan(italic = true) }))

        addTag("p", SpanConfig(type = Tag.Type.PARAGRAPH,
                onCreateSpan = {
                    BlockSpan(density = density,
                            firstLineIndent = 20)
                }))

        addTag("s", SpanConfig(type = Tag.Type.CHARACTER,
                onCreateSpan = { InlineSpan(strike = true) }))

        addTag("span", SpanConfig(type = Tag.Type.CHARACTER))

        addTag("strike", SpanConfig(type = Tag.Type.CHARACTER,
                onCreateSpan = { InlineSpan(strike = true) }))

        addTag("sub", SpanConfig(type = Tag.Type.CHARACTER,
                onSetTextStyle = { _, tp ->
                    tp.baselineShift += (tp.textSize * 0.2F).toInt()
                    tp.textSize *= 0.7F
                }))

        addTag("sup", SpanConfig(type = Tag.Type.CHARACTER,
                onSetTextStyle = { _, tp ->
                    tp.baselineShift -= (tp.textSize * 0.3F).toInt()
                    tp.textSize *= 0.7F
                }))

        addTag("small", SpanConfig(type = Tag.Type.CHARACTER,
                onCreateSpan = {
                    InlineSpan(textSizeScale = 0.85F)
                }))

        addTag("u", SpanConfig(type = Tag.Type.CHARACTER,
                onCreateSpan = {
                    InlineSpan(underline = true)
                })
        )
    }
}