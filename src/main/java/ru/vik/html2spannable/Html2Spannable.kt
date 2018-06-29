package ru.vik.html2spannable

import android.text.Layout
import android.text.SpannableStringBuilder
import android.text.TextPaint

import ru.vik.utils.html.BaseHtml
import ru.vik.utils.html.Tag

open class Html2Spannable(var density: Float = 1.0f,
                          var fontDensity: Float = 1.0f,
                          private val html: BaseHtml = BaseHtml()) {

    class SpanConfig(type: Tag.Type,
                     val onSetTextStyle: ((tag: Tag, tp: TextPaint) -> Unit)? = null,
//                     val onTextTransform: ((text: String) -> String?)? = null,
                     val onCreateSpan: ((tag: Tag) -> Any?)? = null,
                     val onAfterCreateSpan: ((tag: Tag, span: Any) -> Unit)? = null
    ) : BaseHtml.TagConfig(type)

    class SpanItem(val span: Any, var start: Int, var end: Int = 0)

    private fun getSpanConfig(name: String): SpanConfig? {
        return this.html.getTagConfig(name)?.let {
            it as? SpanConfig
        }
    }

    fun addTag(name: String, config: SpanConfig) {
        this.html.config[name] = config
    }

    fun toSpannable(html: String, beforeSetSpans: ((spanList: MutableList<SpanItem>) -> Unit)? = null): SpannableStringBuilder {
        this.html.parse(html)

        this.html.root?.let {
            val output = StringBuilder()
            val spanList = mutableListOf<SpanItem>()

            tagToSpannable(it, spanList, output)

            val spannable = SpannableStringBuilder(output)

            beforeSetSpans?.invoke(spanList)

            for (item in spanList) {
                spannable.setSpan(item.span, item.start, item.end, 0)
            }

            return spannable
        }

        return SpannableStringBuilder()
    }

    private fun tagToSpannable(tag: Tag,
                               spanList: MutableList<SpanItem>,
                               output: StringBuilder,
                               needForLineBreak: Boolean = false): Boolean {
        val config = getSpanConfig(tag.name)
        @Suppress("NAME_SHADOWING")
        var needForLineBreak = needForLineBreak

        // Блочным элементам обязательно нужны разрывы строки в начале и конце
        // (кроме начала текста и конца текста, соответственно)
        if (tag.type.isBlock()) {
            needForLineBreak = true
        }

        // Создаём спан
        val span = config?.let { createSpan(tag, it) }
        val spanItem: SpanItem? = span?.let {
            SpanItem(it, output.length)
        }
        spanItem?.let { spanList.add(it) }

        val text = StringBuilder()
//        config?.beforeText?.let { text.append(tag.getText(it)) }
        if (tag.text.isNotEmpty()) {
            text.append(tag.text)
        }
//        config?.afterText?.let { text.append(tag.getText(it)) }

        if (text.isNotEmpty()) {
            if (needForLineBreak && output.isNotEmpty()) {
                output.append('\n')
            }
            needForLineBreak = false
            output.append(text)
        }

        // Добавляем "детей"
        for (child in tag.children) {
            needForLineBreak = tagToSpannable(child, spanList, output, needForLineBreak)
        }

        // Закрываем спан
        spanItem?.end = output.length

        if (tag.type.isBlock()) {
            needForLineBreak = true
        }

        return needForLineBreak
    }

    open fun createSpan(tag: Tag, config: SpanConfig): Any? {
        // Установка обработчика onSetTextStyle для ручной установки параметров текста.
        // В TagConfig лямбда onSetTextStyle лежит с параметрами Tag и TextPaint,
        // а в спанах только с TextPaint
        val tagSpan: Any? = config.onCreateSpan?.invoke(tag) ?: let {
            val onSetTextStyle = config.onSetTextStyle?.let {
                { tp: TextPaint -> it(tag, tp) }
            }

            when {
                tag.type.isInline() -> InlineSpan(
                        density = density,
                        fontDensity = fontDensity,
                        onSetTextStyle = onSetTextStyle)
                tag.type.isBlock()  -> BlockSpan(
                        density = density,
                        fontDensity = fontDensity,
                        onSetTextStyle = onSetTextStyle)
                else                -> null
            }
        }

        tagSpan?.let { span ->
            if (span is BlockSpan) {
                tag.attributes["align"]?.let {
                    when (it) {
                        "left"   -> span.align = Layout.Alignment.ALIGN_NORMAL
                        "right"  -> span.align = Layout.Alignment.ALIGN_OPPOSITE
                        "center" -> span.align = Layout.Alignment.ALIGN_CENTER
                    }
                }
            }
            config.onAfterCreateSpan?.invoke(tag, span)
        }

        return tagSpan
    }

}