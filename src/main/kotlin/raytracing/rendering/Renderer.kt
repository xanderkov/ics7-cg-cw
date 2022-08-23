package raytracing.rendering

import raytracing.math.*
import raytracing.pixels.*
import raytracing.solids.Solid
import java.awt.Graphics
import kotlin.math.pow


class Renderer {
    companion object {
        private const val GLOBAL_ILLUMINATION = 0.2f
        private const val SKY_EMISSION = 0.5f
        private const val MAX_REFLECTION_BOUNCES = 15
        private const val SHOW_SKYBOX = true
        private fun getNormalizedScreenCoordinates(x: Int, y: Int, width: Int, height: Int): FloatArray {
            val u: Float
            val v: Float
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
            val sceneLight = scene.light
            // Raytrace to light to check if something blocks the light
            val lightBlocker = scene.raycast(
                Ray(
                    sceneLight.getPosition(),
                    hit.position.subtract(sceneLight.getPosition()).normalize()
                )
            )
            return if (lightBlocker != null && lightBlocker.solid !== hit.solid) {
                GLOBAL_ILLUMINATION // GLOBAL_ILLUMINATION = Minimum brightness
            } else {
                kotlin.math.max(
                    GLOBAL_ILLUMINATION,
                    kotlin.math.min(1f, Vector3.dot(hit.normal, sceneLight.getPosition().subtract(hit.position)))
                )
            }
        }

        private fun getSpecularBrightness(scene: Scene, hit: RayHit): Float {
            val hitPos: Vector3 = hit.position
            val cameraDirection: Vector3 = scene.camera.position.subtract(hitPos).normalize()
            val lightDirection = hitPos.subtract(scene.light.getPosition()).normalize()
            val lightReflectionVector =
                lightDirection.subtract(hit.normal.multiply(2 * Vector3.dot(lightDirection, hit.normal)))
            val specularFactor =
                kotlin.math.max(0f, kotlin.math.min(1f, Vector3.dot(lightReflectionVector, cameraDirection)))
            return specularFactor.toDouble().pow(2.0).toFloat() * hit.solid.reflectivity
        }

        private fun computePixelInfoAtHit(scene: Scene, hit: RayHit, recursionLimit: Int): PixelData {
            val hitPos: Vector3 = hit.position
            val rayDir: Vector3 = hit.ray.direction
            val hitSolid: Solid = hit.solid
            val hitColor: Color = hitSolid.getTextureColor(hitPos.subtract(hitSolid.position))
            val brightness: Float = getDiffuseBrightness(scene, hit)
            val specularBrightness: Float = getSpecularBrightness(scene, hit)
            val reflectivity: Float = hitSolid.reflectivity
            val emission: Float = hitSolid.emission

            val reflection: PixelData
            val reflectionVector = rayDir.subtract(hit.normal!!.multiply(2f * Vector3.dot(rayDir, hit.normal)))

            // Add a little to avoid hitting the same solid again
            val reflectionRayOrigin = hitPos.add(reflectionVector.multiply(0.001f))
            val reflectionHit: RayHit? =
                if (recursionLimit > 0) scene.raycast(Ray(reflectionRayOrigin, reflectionVector)) else null
            reflection = if (reflectionHit != null) {
                computePixelInfoAtHit(scene, reflectionHit, recursionLimit - 1)
            } else {
                val sbColor: Color = scene.skybox.getColor(reflectionVector)
                PixelData(sbColor, Float.POSITIVE_INFINITY, sbColor.luminance * SKY_EMISSION)
            }
            val pixelColor: Color = Color.lerp(hitColor, reflection.color, reflectivity) // Reflected color
                .multiply(brightness) // Diffuse lighting
                .add(specularBrightness) // Specular lighting
                .add(hitColor.multiply(emission)) // Object emission
                .add(reflection.color.multiply(reflection.emission * reflectivity)) // Indirect illumination


            return PixelData(
                pixelColor, Vector3.distance(scene.camera.position, hitPos),
                kotlin.math.min(1.0f, emission + reflection.emission * reflectivity + specularBrightness)
            )
        }

        private fun computePixelInfo(scene: Scene, u: Float, v: Float): PixelData {
            val eyePos = Vector3(0f, 0f, (-1 / kotlin.math.tan(Math.toRadians(scene.camera.fOV / 2.0))).toFloat())
            val cam = scene.camera
            val rayDir = Vector3(u, v, 0f).subtract(eyePos).normalize().rotateYP(cam.yaw, cam.pitch)
            val hit = scene.raycast(Ray(eyePos.add(cam.position), rayDir))
            return if (hit != null) {
                computePixelInfoAtHit(scene, hit, MAX_REFLECTION_BOUNCES)
            } else if (SHOW_SKYBOX) {
                val sbColor: Color = scene.skybox.getColor(rayDir)
                PixelData(sbColor, Float.POSITIVE_INFINITY, sbColor.luminance * SKY_EMISSION)
            } else {
                PixelData(Color.BLACK, Float.POSITIVE_INFINITY, 0f)
            }
        }

        fun renderScene(scene: Scene?, gfx: Graphics, width: Int, height: Int, resolution: Float) {
            val blockSize = (1 / resolution).toInt()
            for (x in 0 until width step blockSize) {
                for (y in 0 until height step blockSize) {
                    val uv: FloatArray = getNormalizedScreenCoordinates(x, y, width, height)
                    val pixelData: PixelData = computePixelInfo(scene!!, uv[0], uv[1])
                    gfx.color = pixelData.color.toAWTColor()
                    gfx.fillRect(x, y, blockSize, blockSize)
                }
            }
        }

    }
}