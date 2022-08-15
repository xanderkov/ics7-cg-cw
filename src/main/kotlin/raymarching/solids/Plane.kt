package raymarching.solids

import raymarching.math.Ray
import raymarching.math.Vector3
import raymarching.pixels.Color

class Plane(height: Float, color: Color?, private val checkerPattern: Boolean, reflectivity: Float, emission: Float) :
    Solid(Vector3(0f, height, 0f), color!!, reflectivity, emission) {

    override fun calculateIntersection(ray: Ray?): Vector3? {
        val t: Float = -(ray!!.origin.y - position.y) / ray.direction.y
        return if (t > 0 && t.isFinite()) {
            ray.origin.add(ray.direction.multiply(t))
        } else null
    }

    override fun getNormalAt(point: Vector3): Vector3 {
        return Vector3(0f, 1f, 0f)
    }

    override fun getTextureColor(point: Vector3?): Color {
        return if (checkerPattern) {
            Color.TREE
        } else {
            color
        }
    }
}
