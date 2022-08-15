package raymarching.solids

import raymarching.math.Ray
import raymarching.math.Vector3
import raymarching.pixels.Color

class Cylinders(position: Vector3, private val radius: Float, private val height: Float, color: Color, reflectivity: Float, emission: Float):
    Solid(position, color, reflectivity, emission) {

    override fun calculateIntersection(ray: Ray?): Vector3? {
        val a: Float = (ray!!.direction.x * ray.direction.x) + (ray.direction.z * ray.direction.z)

        val b: Float = 2 * (ray.direction.x * (ray.origin.x - position.x) +
                ray.direction.z * (ray.origin.z - position.z))

        val c: Float = (ray.origin.x - position.x) * (ray.origin.x - position.x) +
                (ray.origin.z - position.z) * (ray.origin.z - position.z) - (radius * radius)

        val delta: Float = b * b - 4 * (a * c)
        if (Math.abs(delta) < 0.001 && delta < 0.0) return null

        val t1: Float = (-b - kotlin.math.sqrt(delta)) / (2 * a)
        val t2: Float = (-b + kotlin.math.sqrt(delta)) / (2 * a)
        val t: Float = if (t1 > t2) t2 else t1

        val r: Float = ray.origin.y + t * ray.direction.y

        return if ((r >= position.y) && (r <= position.y + height)) ray.origin.add(ray.direction.multiply(t)) else null

    }

    override fun getNormalAt(point: Vector3): Vector3 {
        return Vector3(point.x - position.x, 0f, point.z - position.z).normalize()
    }

    override fun getTextureColor(point: Vector3?): Color {
        return color
    }

}