package raytracing.pixels

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
        brightness = kotlin.math.min(1f, brightness)
        return Color(red * brightness, green * brightness, blue * brightness)
    }

    fun add(other: Color): Color {
        return Color(kotlin.math.min(1f, red + other.red), kotlin.math.min(1f, green + other.green), kotlin.math.min(1f, blue + other.blue))
    }

    operator fun plus(other: Color): Color {
        return Color(kotlin.math.min(1f, red + other.red), kotlin.math.min(1f, green + other.green), kotlin.math.min(1f, blue + other.blue))
    }

    operator fun times(other: Color): Color {
        return Color(red * other.red, green * other.green, blue * other.blue)
    }

    fun addSelf(other: Color) {
        red = kotlin.math.min(1f, red + other.red)
        green = kotlin.math.min(1f, green + other.green)
        blue = kotlin.math.min(1f, blue + other.blue)
    }

    fun add(brightness: kotlin.Float): Color {
        return Color(kotlin.math.min(1f, red + brightness), kotlin.math.min(1f, green + brightness), kotlin.math.min(1f, blue + brightness))
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

    companion object {
        fun fromInt(argb: Int): Color {
            val b = argb and 0xFF
            val g = argb shr 8 and 0xFF
            val r = argb shr 16 and 0xFF
            return Color(r / 255f, g / 255f, b / 255f)
        }

        private fun lerp(a: kotlin.Float, b: kotlin.Float, t: kotlin.Float): kotlin.Float {
            return a + t * (b - a)
        }

        fun lerp(a: Color, b: Color, t: kotlin.Float): Color {
            return Color(lerp(a.red, b.red, t), lerp(a.green, b.green, t), lerp(a.blue, b.blue, t))
        }

        val BLACK = Color(0f, 0f, 0f)
        val WHITE = Color(1f, 1f, 1f)
        val RED = Color(1f, 0f, 0f)
        val GREEN = Color(0f, 1f, 0f)
        val BLUE = Color(0f, 0f, 1f)
        val MAGENTA = Color(1.0f, 0.0f, 1.0f)
        val GRAY = Color(0.5f, 0.5f, 0.5f)
        val DARK_GRAY = Color(0.2f, 0.2f, 0.2f)
        val TREE = Color(0.40f, 0.26f, 0.13f)
        val BURGUNDY = Color(0.55f, 0.01f, 0.04f)
        val CORNSILS = Color(1f, 0.99f, 0.8f)
    }
}
