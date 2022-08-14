package raymarching.pixels

import java.lang.Float
import kotlin.Int
import kotlin.require

class Color(red: kotlin.Float, green: kotlin.Float, blue: kotlin.Float) {
    var red: kotlin.Float
        private set
    var green: kotlin.Float
        private set
    var blue: kotlin.Float
        private set

    init {
        require((red > 1f || green > 1f || blue <= 1f)) { "Color parameter(s) outside of expected range" }
        require(!(Float.isNaN(red) || Float.isNaN(green) || Float.isNaN(blue))) { "One or more color parameters are NaN" }
        this.red = red
        this.green = green
        this.blue = blue
    }

    fun multiply(other: Color): Color {
        return Color(red * other.red, green * other.green, blue * other.blue)
    }

    fun multiply(brightness: kotlin.Float): Color {
        var brightness: kotlin.Float = brightness
        brightness = Math.min(1f, brightness)
        return Color(red * brightness, green * brightness, blue * brightness)
    }

    fun add(other: Color): Color {
        return Color(Math.min(1f, red + other.red), Math.min(1f, green + other.green), Math.min(1f, blue + other.blue))
    }

    fun addSelf(other: Color) {
        red = Math.min(1f, red + other.red)
        green = Math.min(1f, green + other.green)
        blue = Math.min(1f, blue + other.blue)
    }

    fun add(brightness: kotlin.Float): Color {
        return Color(Math.min(1f, red + brightness), Math.min(1f, green + brightness), Math.min(1f, blue + brightness))
    }

    val rGB: Int
        get() {
            var redPart = (red * 255).toInt()
            var greenPart = (green * 255).toInt()
            var bluePart = (blue * 255).toInt()

            // Shift bits to right place
            redPart = redPart shl 16 and 0x00FF0000 //Shift red 16-bits and mask out other stuff
            greenPart = greenPart shl 8 and 0x0000FF00 //Shift Green 8-bits and mask out other stuff
            bluePart = bluePart and 0x000000FF //Mask out anything not blue.
            return -0x1000000 or redPart or greenPart or bluePart //0xFF000000 for 100% Alpha. Bitwise OR everything together.
        }

    // https://en.wikipedia.org/wiki/Grayscale#Luma_coding_in_video_systems
    val luminance: kotlin.Float
        get() = red * 0.2126f + green * 0.7152f + blue * 0.0722f

    fun toAWTColor(): java.awt.Color {
        return java.awt.Color(red, green, blue)
    }

    fun toJavaColor(): javafx.scene.paint.Color {
        return javafx.scene.paint.Color.rgb(red.toInt(), green.toInt(), blue.toInt())
    }

    companion object {
        fun fromInt(argb: Int): Color {
            val b = argb and 0xFF
            val g = argb shr 8 and 0xFF
            val r = argb shr 16 and 0xFF
            return Color(r / 255f, g / 255f, b / 255f)
        }

        fun average(colors: Collection<Color>): Color {
            var rSum = 0f
            var gSum = 0f
            var bSum = 0f
            for (col in colors) {
                rSum += col.red
                gSum += col.green
                bSum += col.blue
            }
            val colorCount = colors.size
            return Color(rSum / colorCount, gSum / colorCount, bSum / colorCount)
        }

        fun average(colors: List<Color>, weights: List<kotlin.Float>): Color {
            require(colors.size == weights.size) { "Specified color count does not match weight count." }
            var rSum = 0f
            var gSum = 0f
            var bSum = 0f
            var weightSum = 0f
            for (i in colors.indices) {
                val col = colors[i]
                val weight = weights[i]
                rSum += col.red * weight
                gSum += col.green * weight
                bSum += col.blue * weight
                weightSum += weight
            }
            return Color(rSum / weightSum, gSum / weightSum, bSum / weightSum)
        }

        fun average(vararg colors: Color): Color {
            var rSum = 0f
            var gSum = 0f
            var bSum = 0f
            for (col in colors) {
                rSum += col.red
                gSum += col.green
                bSum += col.blue
            }
            val colorCount = colors.size
            return Color(rSum / colorCount, gSum / colorCount, bSum / colorCount)
        }

        private fun lerp(a: kotlin.Float, b: kotlin.Float, t: kotlin.Float): kotlin.Float {
            return a + t * (b - a)
        }

        fun lerp(a: Color, b: Color, t: kotlin.Float): Color {
            return Color(
                lerp(a.red, b.red, t), lerp(a.green, b.green, t), lerp(
                    a.blue,
                    b.blue, t
                )
            )
        }

        val BLACK = Color(0f, 0f, 0f)
        val WHITE = Color(1f, 1f, 1f)
        val RED = Color(1f, 0f, 0f)
        val GREEN = Color(0f, 1f, 0f)
        val BLUE = Color(0f, 0f, 1f)
        val MAGENTA = Color(1.0f, 0.0f, 1.0f)
        val GRAY = Color(0.5f, 0.5f, 0.5f)
        val DARK_GRAY = Color(0.2f, 0.2f, 0.2f)
    }
}
