package raytracing.rendering

import raytracing.math.*
import raytracing.pixels.*
import raytracing.solids.Solid
import java.awt.Graphics
import java.time.LocalTime
import kotlin.Double.Companion.POSITIVE_INFINITY
import kotlin.concurrent.timerTask
import kotlin.math.pow
import kotlin.math.*
import kotlin.time.TimedValue


class Renderer {
    companion object {
        private const val GLOBAL_ILLUMINATION = 0.2f
        private const val SKY_EMISSION = 0.5f
        private const val MAX_REFLECTION_BOUNCES = 9
        private const val SHOW_SKYBOX = true

        private const val minDistance = 0
        private const val maxDistance = POSITIVE_INFINITY

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
                    sceneLight.position,
                    hit.position.subtract(sceneLight.position).normalize()
                )
            )
            return if (lightBlocker != null && lightBlocker.solid !== hit.solid) {
                GLOBAL_ILLUMINATION // GLOBAL_ILLUMINATION = Minimum brightness
            } else {
                max(
                    GLOBAL_ILLUMINATION,
                    min(1f, Vector3.dot(hit.normal, sceneLight.position - hit.position))
                )
            }
        }

        private fun getSpecularBrightness(scene: Scene, hit: RayHit): Float {
            val hitPos: Vector3 = hit.position
            val cameraDirection: Vector3 = scene.camera.position.subtract(hitPos).normalize()
            val lightDirection = hitPos - (scene.light.position).normalize()
            // косинус угла между источником света и нормалью
            val lightcos = Vector3.dot(lightDirection, hit.normal)

            // косинус угла между отраженным лучем и направлением луча
            val lightReflectionVector = lightDirection - (hit.normal.multiply(2 * lightcos))

            val specularFactor = max(0f, min(1f, Vector3.dot(lightReflectionVector, cameraDirection)))
            return specularFactor.pow(2f) * hit.solid.reflectivity
        }

        // Преломление по закону Снеллиуса
        // I - угол падения
        private fun RefractRay(I: Vector3, n: Vector3, cos: Float, theataT: Float, theataI: Float): Vector3 {
            // Ошибка в n должно быть -n
            if (cos < 0) return RefractRay(I, n, -cos, theataI, theataT)
            val eta = theataI / theataT
            val k = 1 - eta * eta * (1 - cos * cos)
            return if (k < 0) Vector3(1f, 0f, 0f) else I.multiply(eta) + n.multiply(eta * cos - sqrt(k))
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
            val fractivity = hitSolid.fractivity

            val reflection: PixelData

            val directionCos: Float = Vector3.dot(rayDir, hit.normal)
            // Отраженный луч
            val reflectionVector = rayDir - (hit.normal.multiply(2f * directionCos))


            // Теститруем преломление
            val refractRay = RefractRay(rayDir, hit.normal, directionCos, fractivity, 1f)

            // Добавим eps, чтобы лучи не попадали в одно и то же место
            val reflectionRayOrigin = hitPos.add(reflectionVector.multiply(0.001f))
            val refractRayOrigin = hitPos.add(refractRay.multiply(0.001f))

            val reflectionHit: RayHit? =
                if (recursionLimit > 0) scene.raycast(Ray(reflectionRayOrigin, reflectionVector)) else null

            val refractHit: RayHit? =
                if (recursionLimit > 0) scene.raycast(Ray(refractRayOrigin, refractRay)) else null

            // Отражаймость
            reflection = if (reflectionHit != null) {
                computePixelInfoAtHit(scene, reflectionHit, recursionLimit - 1)
            } else {
                val sbColor: Color = scene.skybox.getColor(reflectionVector)
                PixelData(sbColor, Float.POSITIVE_INFINITY, sbColor.luminance * SKY_EMISSION)
            }

            // Преломление
            val refraction = if (refractHit != null) {
                computePixelInfoAtHit(scene, refractHit, recursionLimit - 1)
            }
            else {
                val sbColor: Color = scene.skybox.getColor(refractRay)
                PixelData(sbColor, Float.POSITIVE_INFINITY, sbColor.luminance * SKY_EMISSION)
            }

            val pixelColor: Color = Color.lerp(hitColor, reflection.color, reflectivity) // Reflected color
                .multiply(brightness) // Diffuse lighting
                .add(specularBrightness) // Specular lighting
                .add(reflection.color.multiply(reflectivity * emission)) // Indirect illumination
                .add(refraction.color.multiply(fractivity))

            val depthPixel = Vector3.distance(scene.camera.position, hitPos)
            val pixelEmission = min(1.0f, emission + reflection.emission * reflectivity + specularBrightness + fractivity)

            return PixelData(pixelColor, depthPixel, pixelEmission)
        }


        private fun computeRay(scene: Scene, hit: RayHit, recursionLimit: Int): PixelData {
            val hitPos: Vector3 = hit.position
            val rayDir: Vector3 = hit.ray.direction
            TODO()


        }

        private fun computePixelInfo(scene: Scene, u: Float, v: Float): PixelData {
            val eyePos = Vector3(0f, 0f, (-1 / tan(Math.toRadians(scene.camera.fOV / 2.0))).toFloat())
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
            val start = System.nanoTime()
            for (x in 0 until width step blockSize) {
                for (y in 0 until height step blockSize) {
                    val uv: FloatArray = getNormalizedScreenCoordinates(x, y, width, height)
                    val pixelData: PixelData = computePixelInfo(scene!!, uv[0], uv[1])
                    gfx.color = pixelData.color.toAWTColor()
                    gfx.fillRect(x, y, blockSize, blockSize)
                }
            }
            println((System.nanoTime() - start))
        }

    }
}