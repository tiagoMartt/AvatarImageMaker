package com.tiagomartt.avatarimagemakerlib

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.GradientDrawable
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.FontRes
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.ColorUtils
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import androidx.core.view.setPadding
import java.util.*

class AvatarImageMaker(private val builder: AvatarImageBuilder) {

    class AvatarImageBuilder(private val context: Context) {

        companion object {
            val OVAL = 1
            val RECTANGLE = 0
        }

        private var width = -1
        private var height = -1
        private var fitSize = false

        private var randomColors = true
        private var shape = OVAL
        private var backgroundColor = 0
        private var strokeColor = 0
        private var strokeWidth = 0
        private var cornerRadius = 0f

        private var substringEndIndex = 2
        private var text = ""
        private var textColor = 0
        private var textSizeUnit = TypedValue.COMPLEX_UNIT_PX
        private var textSize = 0f
        private var isAllCaps = true
        private var fontFamilyResId = 0

        init {

            width = context.resources.getDimension(R.dimen.width).toInt()
            height = context.resources.getDimension(R.dimen.height).toInt()

            strokeWidth = context.resources.getDimension(R.dimen.strokeWidth).toInt()
            cornerRadius = context.resources.getDimension(R.dimen.cornerRadius)

            textSize = context.resources.getDimension(R.dimen.textSize)
        }

        fun setBuilder(builder: AvatarImageBuilder) = apply {

            width = builder.width
            height = builder.height
            fitSize = builder.fitSize

            randomColors = builder.randomColors
            shape = builder.shape
            backgroundColor = builder.backgroundColor
            strokeColor = builder.strokeColor
            strokeWidth = builder.strokeWidth
            cornerRadius = builder.cornerRadius

            substringEndIndex = builder.substringEndIndex
            text = builder.text
            textColor = builder.textColor
            textSizeUnit = builder.textSizeUnit
            textSize = builder.textSize
            isAllCaps = builder.isAllCaps
            fontFamilyResId = builder.fontFamilyResId
        }

        fun setSize(width: Int, height: Int) = apply {
            this.width = width
            this.height = height
        }

        fun setFitSize(fitSize: Boolean) = apply {
            this.fitSize = fitSize
        }

        fun setRandomColors(randomColors: Boolean) = apply {
            this.randomColors = randomColors
        }

        fun setShape(shape: Int) = apply {
            this.shape = shape
        }

        fun setBackgroundColor(@ColorInt backgroundColor: Int) = apply {
            this.backgroundColor = backgroundColor
        }

        fun setStrokeColor(@ColorInt strokeColor: Int) = apply {
            this.strokeColor = strokeColor
        }

        fun setStrokeWidth(strokeWidth: Int) = apply {
            this.strokeWidth = strokeWidth
        }

        fun setCornerRadius(cornerRadius: Float) = apply {
            this.cornerRadius = cornerRadius
        }

        fun setInitials(initials: Int) = apply {
            this.substringEndIndex = initials
        }

        fun setText(text: String, substringEndIndex: Int) = apply {
            this.text = text
            this.substringEndIndex = substringEndIndex
        }

        fun setText(text: String) = apply {
            setText(text, 2)
        }

        fun setTextColor(@ColorInt textColor: Int) = apply {
            this.textColor = textColor
        }

        fun setTextSize(textSizeUnit: Int, textSize: Float) = apply {
            this.textSizeUnit = textSizeUnit
            this.textSize = textSize
        }

        fun setTextSize(textSize: Float) = apply {
            setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize)
        }

        fun setIsAllCaps(isAllCaps: Boolean) = apply {
            this.isAllCaps = isAllCaps
        }

        fun setFontFamily(@FontRes fontFamilyResId: Int) = apply {
            this.fontFamilyResId = fontFamilyResId
        }

        fun build(): BitmapDrawable {

            if (randomColors) {

                val colors = getRandomColor()
                backgroundColor = colors[0]
                strokeColor = colors[1]
                textColor = colors[2]
            }

            return avatarImageGenerate(context, shape, cornerRadius, backgroundColor, strokeWidth, strokeColor, text, textColor, textSizeUnit, textSize, width, height)
        }

        private fun avatarImageGenerate(context: Context, shape: Int, cornerRadius: Float, backgroundColor: Int, strokeWidth: Int, strokeColor: Int, text: String, textColor: Int, textSizeUnit: Int, textSize: Float, width: Int, height: Int): BitmapDrawable {

            val textView = TextView(context)

            val gradientDrawable = GradientDrawable()
            gradientDrawable.shape = shape
            gradientDrawable.cornerRadius = cornerRadius
            gradientDrawable.setColor(backgroundColor)
            gradientDrawable.setStroke(strokeWidth, strokeColor)

            textView.background = gradientDrawable
            textView.gravity = Gravity.CENTER
            textView.setPadding(24)
            textView.text = getFirstCharacters(text)
            textView.setTextColor(textColor)
            textView.setTextSize(textSizeUnit, textSize)
            textView.isAllCaps = isAllCaps
            if (fontFamilyResId > 0) {textView.typeface = ResourcesCompat.getFont(context, fontFamilyResId)}

            var size: Int
            var mode: Int

            if (width <= -1) {
                size = 0
                mode = View.MeasureSpec.UNSPECIFIED
            } else {
                size = width
                mode = View.MeasureSpec.EXACTLY
            }

            val specWidth = View.MeasureSpec.makeMeasureSpec(size, mode)

            if (height <= -1) {
                size = 0
                mode = View.MeasureSpec.UNSPECIFIED
            } else {
                size = height
                mode = View.MeasureSpec.EXACTLY
            }

            val specHeight = View.MeasureSpec.makeMeasureSpec(size, mode)

            textView.measure(specWidth, specHeight)

            if (fitSize) {

                val max = maxOf(textView.measuredWidth, textView.measuredHeight)

                val w = View.MeasureSpec.makeMeasureSpec(max, View.MeasureSpec.EXACTLY)
                val h = View.MeasureSpec.makeMeasureSpec(max, View.MeasureSpec.EXACTLY)

                textView.measure(w, h)
            }

            val bitmap = Bitmap.createBitmap(textView.measuredWidth, textView.measuredHeight, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)

            textView.layout(0, 0, textView.measuredWidth, textView.measuredHeight)
            textView.draw(canvas)

            return BitmapDrawable(context.resources, bitmap)
        }

        private fun getRandomColor(): IntArray {

            val colors = IntArray(3)

            val rnd = Random()

            colors[0] = Color.rgb(rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
            colors[1] = Color.rgb(colors[0].red - 20, colors[0].green - 20, colors[0].blue - 20)

            if (ColorUtils.calculateLuminance(colors[0]) > 0.5f) {
                colors[2] = Color.rgb(34, 34, 34)
            } else {
                colors[2] = Color.rgb(255, 255, 255)
            }

            return colors
        }

        private fun getFirstCharacters(string: String) : String {

            if (string.isEmpty()) return "-"

            if (string.length < substringEndIndex) return string

            return if (substringEndIndex == -1) {
                string
            } else {
                string.substring(0, substringEndIndex)
            }
        }
    }
}