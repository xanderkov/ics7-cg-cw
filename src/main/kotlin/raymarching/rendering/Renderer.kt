package raymarching.rendering

import raymarching.math.*
import raymarching.pixels.*
import raymarching.solids.Solid
import java.awt.Graphics

class Renderer {
    private val GLOBAL_ILLUMINATION = 0.3f
    private val SKY_EMISSION = 0.5f
    private val MAX_REFLECTION_BOUNCES = 5
    private val SHOW_SKYBOX = false

    val bloomIntensity = 0.5F
    val bloomRadius = 10

    fun getNormalizedScreenCoordinates(x: Int, y: Int, width: Int, height: Int): FloatArray? {
        var u = 0f
        var v = 0f
        if (width > height) {
            u = (x - width / 2 + height / 2).toFloat() / height * 2 - 1
            v = -(y.toFloat() / height * 2 - 1)
        } else {
            u = x.toFloat() / width * 2 - 1
            v = -((y - height / 2 + width / 2).toFloat() / width * 2 - 1)
        }
        return floatArrayOf(u, v)
    }

    private fun getDiffuseBrightness(scene: Scene, hit: RayHit): Float {
        val sceneLight = scene.getLight()

        // Raytrace to light to check if something blocks the light
        val lightBlocker = scene.raycast(
            Ray(
                sceneLight.getPosition(),
                hit.position.subtract(sceneLight.getPosition()).normalize()
            )
        )
        return if (lightBlocker != null && lightBlocker.solid !== hit.solid) {
            GLOBAL_ILLUMINATION // GOBAL_ILLUMINATION = Minimum brightness
        } else {
            Math.max(
                GLOBAL_ILLUMINATION,
                Math.min(1f, Vector3.dot(hit.normal, sceneLight.getPosition().subtract(hit.position)))
            )
        }
    }

    private fun getSpecularBrightness(scene: Scene, hit: RayHit): Float {
        val hitPos: Vector3 = hit.position
        val cameraDirection: Vector3 = scene.getCamera().position.subtract(hitPos).normalize()
        val lightDirection = hitPos.subtract(scene.getLight().getPosition()).normalize()
        val lightReflectionVector =
            lightDirection.subtract(hit.normal.multiply(2 * Vector3.dot(lightDirection, hit.normal)))
        val specularFactor =
            Math.max(0f, Math.min(1f, Vector3.dot(lightReflectionVector, cameraDirection)))
        return Math.pow(specularFactor.toDouble(), 2.0).toFloat() * hit.solid.reflectivity
    }

    private fun computePixelInfoAtHit(scene: Scene, hit: RayHit, recursionLimit: Int): PixelData {
        val hitPos: Vector3 = hit.position
        val rayDir: Vector3 = hit.ray.direction
        val hitSolid: Solid = hit.solid
        val brightness: Float = getDiffuseBrightness(scene, hit)
        val specularBrightness: Float = getSpecularBrightness(scene, hit)
        val reflectivity: Float = hitSolid.reflectivity
        val emission: Float = hitSolid.emission
        val reflection: PixelData
        val reflectionVector = rayDir.subtract(hit.normal.multiply(2f * Vector3.dot(rayDir, hit.normal)))
        val reflectionRayOrigin =
            hitPos.add(reflectionVector.multiply(0.001f)) // Add a little to avoid hitting the same solid again
        val reflectionHit: RayHit? = if (recursionLimit > 0) scene.raycast(Ray(reflectionRayOrigin, reflectionVector)) else null
        reflection = if (reflectionHit != null) {
            computePixelInfoAtHit(scene, reflectionHit, recursionLimit - 1)
        } else {
            PixelData(
                Float.POSITIVE_INFINITY,
                Math.min(1f, emission + 0 * reflectivity + specularBrightness)
            )
        }
        return PixelData(
            Vector3.distance(scene.getCamera().position, hitPos),
            Math.min(1f, emission + reflection.emission * reflectivity + specularBrightness)
        )
    }

    fun computePixelInfo(scene: Scene, u: Float, v: Float): PixelData? {
        val eyePos = Vector3(0f, 0f, (-1 / Math.tan(Math.toRadians(scene.getCamera().fOV / 2.0))).toFloat())
        val cam = scene.getCamera()
        val rayDir = Vector3(u, v, 0f).subtract(eyePos).normalize().rotateYP(cam.yaw, cam.pitch)
        val hit = scene.raycast(Ray(eyePos.add(cam.position), rayDir))
        return if (hit != null) {
            computePixelInfoAtHit(
                scene,
                hit,
                MAX_REFLECTION_BOUNCES
            )
        } else {
            PixelData(Float.POSITIVE_INFINITY, 0f)
        }
    }

    fun renderScene(scene: Scene, width: Int, height: Int): PixelBuffer? {
        val pixelBuffer = PixelBuffer(width, height)
        for (x in 0 until width) {
            for (y in 0 until height) {
                val screenUV: FloatArray? = getNormalizedScreenCoordinates(x, y, width, height)
                pixelBuffer.setPixel(
                    x, y, computePixelInfo(
                        scene,
                        screenUV!![0], screenUV[1]
                    )
                )
            }
        }
        return pixelBuffer
    }

    fun renderScene(scene: Scene, gfx: Graphics, width: Int, height: Int, resolution: Float) {
        val blockSize = (1 / resolution).toInt()
        var x = 0
        while (x < width) {
            var y = 0
            while (y < height) {
                val uv: FloatArray? =
                    getNormalizedScreenCoordinates(x, y, width, height)
                val pixelData: PixelData? = computePixelInfo(scene, uv!![0], uv[1])
                gfx.fillRect(x, y, blockSize, blockSize)
                y += blockSize
            }
            x += blockSize
        }
    }

    fun renderScenePostProcessed(scene: Scene, gfx: Graphics, width: Int, height: Int, resolution: Float) {
        val bufferWidth = Math.round(width * resolution + 0.49f)
        val bufferHeight = Math.round(height * resolution + 0.49f)
        val pixelBuffer: PixelBuffer? = renderScene(scene, bufferWidth, bufferHeight)
        val emissivePixels =
            pixelBuffer?.clone() // The width of this buffer has to remain constant to keep the blur factor the same for all sizes
        emissivePixels?.filterByEmission(0.1f)
        val blockSize = 1 / resolution
        for (x in 0 until bufferWidth) {
            for (y in 0 until bufferHeight) {
                val pixel = pixelBuffer?.getPixel(x, y)
                gfx.fillRect(
                    (x * blockSize).toInt(),
                    (y * blockSize).toInt(),
                    blockSize.toInt() + 1,
                    blockSize.toInt() + 1
                )
            }
        }
    }
}