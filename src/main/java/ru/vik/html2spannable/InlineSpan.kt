package ru.vik.html2spannable

import android.text.TextPaint
import android.text.style.MetricAffectingSpan

open class InlineSpan(
        var density: Float = 1.0f,
        var fontDensity: Float = 1.0f,
        var bold: Boolean? = null,
        var italic: Boolean? = null,
        var strike: Boolean? = null,
        var underline: Boolean? = null,
        var textSize: Float? = null,
        var textSizeScale: Float? = null,
        var color: Int? = null,
        var bgColor: Int? = null,
        var textScaleX: Float? = null,
        var onSetTextStyle: ((tp: TextPaint) -> Unit)? = null)
    : MetricAffectingSpan() {

    override fun updateMeasureState(tp: TextPaint) {
        updateDrawState(tp)
    }

    override fun updateDrawState(tp: TextPaint) {
        this.bold?.let { tp.isFakeBoldText = it }
        this.italic?.let { tp.textSkewX = if (it) -0.25F else 0.0F }
        this.strike?.let { tp.isStrikeThruText = it }
        this.underline?.let { tp.isUnderlineText = it }
        this.textSize?.let { tp.textSize = it * this.fontDensity }
        this.textSizeScale?.let { tp.textSize *= it }
        this.color?.let { tp.color = it }
        this.bgColor?.let { tp.bgColor = it }
        this.textScaleX?.let { tp.textScaleX = it }

        this.onSetTextStyle?.invoke(tp)
    }
}