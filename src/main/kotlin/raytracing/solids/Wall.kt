package raytracing.solids

import raytracing.math.Ray
import raytracing.math.Vector3
import raytracing.pixels.Color

class Wall(width: Float, color: Color?, private val checkerPattern: Boolean, reflectivity: Float, emission: Float) :
    Solid(Vector3(0f, 0f, width), color!!, reflectivity, emission) {

    override fun calculateIntersection(ray: Ray?): Vector3? {
        val t: Float = -(ray!!.origin.z - position.z) / ray.direction.z
        return if (t > 0 && t.isFinite()) {
            ray.origin.add(ray.direction.multiply(t))
        } else null
    }

    override fun getNormalAt(point: Vector3): Vector3 {
        return Vector3(0f, 0f, -1f)
    }

    override fun getTextureColor(point: Vector3): Color {
        return if (checkerPattern) {
            // in first or third quadrant of the checkerplane
            if ((point.y > 0) and (point.x > 0) || (point.y < 0) and (point.x < 0)) {
                if ((point.y.toInt() % 2 == 0) xor (point.x.toInt() % 2 != 0)) {
                    Color.CORNSILS
                } else {
                    Color.BURGUNDY
                }
            } else {
                // in second or fourth quadrant of the checkerplane
                if ((point.y.toInt() % 2 == 0) xor (point.x.toInt() % 2 != 0)) {
                    Color.BURGUNDY
                } else {
                    Color.CORNSILS
                }
            }
        } else {
            color
        }
    }
}