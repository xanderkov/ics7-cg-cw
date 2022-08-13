package raymarching.pixels

import java.util.Arrays


class PixelBuffer(val width: Int, val height: Int) {
    private val pixels: Array<Array<PixelData?>?>

    init {
        pixels = Array(width) { arrayOfNulls(height) }
    }

    fun setPixel(x: Int, y: Int, pixelData: PixelData?) {
        pixels[x]!![y] = pixelData
    }

    fun getPixel(x: Int, y: Int): PixelData? {
        return pixels[x]!![y]
    }

    fun filterByEmission(minEmission: Float) {
        for (i in pixels.indices) {
            for (j in pixels[i]!!.indices) {
                val pxl = pixels[i]!![j]
                if (pxl != null && pxl.emission < minEmission) {
                    pixels[i]!![j] = PixelData(Color.BLACK, pxl.depth, pxl.emission)
                }
            }
        }
    }

    fun add(other: PixelBuffer): PixelBuffer {
        for (i in pixels.indices) {
            for (j in pixels[i]!!.indices) {
                val pxl = pixels[i]!![j]
                val otherPxl = other.pixels[i]!![j]
                if (pxl != null && otherPxl != null) {
                    pixels[i]!![j]!!.add(otherPxl)
                }
            }
        }
        return this
    }

    fun resize(newWidth: Int, newHeight: Int, linear: Boolean): PixelBuffer {
        val copy = PixelBuffer(newWidth, newHeight)
        for (i in 0 until newWidth) {
            for (j in 0 until  newHeight) {
                copy.pixels[i]!![j] =
                    pixels[(i.toFloat() / newWidth * width).toInt()]!![(j.toFloat() / newHeight * height).toInt()]
            }
        }
        return copy
    }

    fun clone(): PixelBuffer {
        val clone = PixelBuffer(width, height)
        for (i in pixels.indices) {
            System.arraycopy(pixels[i], 0, clone.pixels[i], 0, pixels[i]!!.size)
        }
        return clone
    }
}