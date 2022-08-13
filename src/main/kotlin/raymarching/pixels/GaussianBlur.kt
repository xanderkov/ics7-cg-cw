package raymarching.pixels

class GaussianBlur(  // Currently unused due to kernel being hardcoded
    var pixelBuffer: PixelBuffer
) {
    private val kernel: FloatArray
    private val width: Int
    private val height: Int

    init {
        width = pixelBuffer.width
        height = pixelBuffer.height

        // Kernel is currently hardcoded with the help of http://dev.theomader.com/gaussian-kernel-calculator/
        kernel = floatArrayOf(
            0.0093f,
            0.028002f,
            0.065984f,
            0.121703f,
            0.175713f,
            0.198596f,
            0.175713f,
            0.121703f,
            0.065984f,
            0.028002f,
            0.0093f
        )
    }

    fun blurHorizontally(radius: Int) {
        val result = PixelBuffer(width, height)
        for (y in 0 until height) {
            for (x in 0 until width) {
                val blurredColor = Color(0f, 0f, 0f)
                val originalPixel = pixelBuffer.getPixel(x, y)
                for (i in -radius..radius) {
                    val kernelMultiplier = kernel[((i + radius) / (radius * 2f) * (kernel.size - 1)).toInt()]
                    if (x + i >= 0 && x + i < width) {
                        val pixel = pixelBuffer.getPixel(x + i, y)
                        if (pixel != null) blurredColor.addSelf(pixel.color.multiply(kernelMultiplier))
                    }
                }
                result.setPixel(x, y, PixelData(blurredColor, originalPixel!!.depth, originalPixel.emission))
            }
        }
        pixelBuffer = result
    }

    fun blurVertically(radius: Int) {
        val result = PixelBuffer(width, height)
        for (x in 0 until width) {
            for (y in 0 until height) {
                val blurredColor = Color(0f, 0f, 0f)
                val originalPixel = pixelBuffer.getPixel(x, y)
                for (i in -radius..radius) {
                    val kernelMultiplier = kernel[((i + radius) / (radius * 2f) * (kernel.size - 1)).toInt()]
                    if (y + i >= 0 && y + i < height) {
                        val pixel = pixelBuffer.getPixel(x, y + i)
                        if (pixel != null) blurredColor.addSelf(pixel.color.multiply(kernelMultiplier))
                    }
                }
                result.setPixel(x, y, PixelData(blurredColor, originalPixel!!.depth, originalPixel.emission))
            }
        }
        pixelBuffer = result
    }

    fun blur(radius: Int, iterations: Int) {
        for (i in 0 until iterations) {
            blurHorizontally(radius)
            blurVertically(radius)
        }
    }
    //private float gaussianDistribution(float x, float sigma) {
    //    return (float) (1/Math.sqrt(2*Math.PI*sigma*sigma)*Math.exp(-(x*x)/(2*sigma*sigma))); // https://en.wikipedia.org/wiki/Gaussian_blur
    //}
}