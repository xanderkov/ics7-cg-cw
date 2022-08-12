package raymarching.rendering

import javafx.scene.image.PixelBuffer

class Renderer {
    private val GLOBAL_ILLUMINATION = 0.3f
    private val SKY_EMISSION = 0.5f
    private val MAX_REFLECTION_BOUNCES = 5
    private val SHOW_SKYBOX = true

    val bloomIntensity = 0.5F
    val bloomRadius = 10

    fun renderScene(scene: Scene?, width: Int, height: Int): PixelBuffer? {
        val pixelBuffer = PixelBuffer<Any>(width, height)
        for (x in 0 until width) {
            for (y in 0 until height) {
                val screenUV: FloatArray =
                    carlvbn.raytracing.rendering.Renderer.getNormalizedScreenCoordinates(x, y, width, height)
                pixelBuffer.setPixel(
                    x, y, carlvbn.raytracing.rendering.Renderer.computePixelInfo(
                        scene,
                        screenUV[0], screenUV[1]
                    )
                )
            }
        }
        return pixelBuffer
    }
}