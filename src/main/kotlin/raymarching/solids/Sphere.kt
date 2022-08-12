package raymarching.solids

import raymarching.math.Ray
import raymarching.math.Vector3


class Sphere(position: Vector3, private val radius: Float, reflectivity: Float, emission: Float):
    Solid(position, reflectivity, emission) {

    override fun calculateIntersection(ray: Ray): Vector3 {
        val t: Float = Vector3.dot(position.subtract(ray.origin), ray.direction)
        val p: Vector3 = ray.origin.add(ray.direction.multiply(t))
        val y = position.subtract(p).length()

        if (y < radius) {
            val x = Math.sqrt((radius * radius - y * y).toDouble()).toFloat()
            val t1 = t - x
            if (t1 > 0) {
                return ray.origin.add(ray.direction.multiply(t1))
            } else return Vector3(0f, 0f, 0f)

        } else {
            return Vector3(0f, 0f, 0f)
        }


    }

    override fun getNormalAt(point: Vector3): Vector3 {
        return point.subtract(position).normalize()
    }

}