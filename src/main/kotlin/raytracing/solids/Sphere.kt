package raytracing.solids

import raytracing.math.Ray
import raytracing.math.Vector3
import raytracing.pixels.Color

class Sphere(position: Vector3, private val radius: Float, color: Color, reflectivity: Float, fractivity: Float, emission: Float):
    Solid(position, color, reflectivity, fractivity, emission) {

    override fun calculateIntersection(ray: Ray): Vector3? {
        val t: Float = Vector3.dot(position - ray.origin, ray.direction)
        val p: Vector3 = ray.origin.add(ray.direction.multiply(t))
        val y = position.subtract(p).length()

        return if (y < radius) {
            val x = kotlin.math.sqrt(radius * radius - y * y)
            val t1 = t - x
            if (t1 > 0) {
                ray.origin.add(ray.direction.multiply(t1))
            } else null
        } else {
            null
        }
    }

    override fun getNormalAt(point: Vector3): Vector3 {
        return point.subtract(position).normalize()
    }

    override fun getTextureColor(point: Vector3): Color {
        return color
    }

}