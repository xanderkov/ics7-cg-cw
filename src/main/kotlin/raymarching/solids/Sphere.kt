package raymarching.solids

import raymarching.math.Ray
import raymarching.math.Vector3
import raymarching.pixels.Color

class Sphere(position: Vector3, private val radius: Float, color: Color, reflectivity: Float, emission: Float):
    Solid(position, color, reflectivity, emission) {

    override fun calculateIntersection(ray: Ray?): Vector3? {
        val t: Float = Vector3.dot(position.subtract(ray!!.origin), ray.direction)
        val p: Vector3 = ray.origin.add(ray.direction.multiply(t))
        val y = position.subtract(p).length()

        return if (y < radius) {
            val x = Math.sqrt((radius * radius - y * y).toDouble()).toFloat()
            val t1 = t - x
            if (t1 > 0) {
                ray.origin.add(ray.direction.multiply(t1))
            } else null

        } else {
            null
        }


    }

    override fun getNormalAt(point: Vector3?): Vector3? {
        return point?.subtract(position)?.normalize()
    }

}