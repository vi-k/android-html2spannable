package ru.vik.html2spannable

import android.graphics.Canvas
import android.graphics.Paint
import android.text.Layout
import android.text.TextPaint
import android.text.style.AlignmentSpan
import android.text.style.LeadingMarginSpan
import android.text.style.TabStopSpan

class BlockSpan(var firstLineIndent: Int = 0,
                var restLinesIndent: Int = 0,
                var firstLineIndentCount: Int = 1,
                var align: Layout.Alignment = Layout.Alignment.ALIGN_NORMAL,
                var tabIndent: Int = 60,
                density: Float = 1.0f,
                fontDensity: Float = 1.0f,
                bold: Boolean? = null,
                italic: Boolean? = null,
                strike: Boolean? = null,
                underline: Boolean? = null,
                textSize: Float? = null,
                textSizeScale: Float? = null,
                color: Int? = null,
                bgColor: Int? = null,
                textScaleX: Float? = null,
                onSetTextStyle: ((tp: TextPaint) -> Unit)? = null)
    : InlineSpan(
        density = density,
        fontDensity = fontDensity,
        bold = bold,
        italic = italic,
        strike = strike,
        underline = underline,
        textSize = textSize,
        textSizeScale = textSizeScale,
        color = color,
        bgColor = bgColor,
        textScaleX = textScaleX,
        onSetTextStyle = onSetTextStyle),

        LeadingMarginSpan,
        LeadingMarginSpan.LeadingMarginSpan2,
        AlignmentSpan,
        TabStopSpan {

    override fun drawLeadingMargin(c: Canvas?, p: Paint?, x: Int, dir: Int, top: Int, baseline: Int,
                                   bottom: Int, text: CharSequence?, start: Int, end: Int,
                                   first: Boolean, layout: Layout?) {
    }

    override fun getLeadingMargin(first: Boolean): Int {
        return ((if (first) this.firstLineIndent else this.restLinesIndent) * this.density).toInt()
    }

    override fun getLeadingMarginLineCount(): Int {
        return this.firstLineIndentCount
    }

    override fun getAlignment(): Layout.Alignment {
        return this.align
    }

    override fun getTabStop(): Int {
        return (tabIndent * this.density).toInt()
    }
}
