package raytracing.solids

import raytracing.math.*
import raytracing.pixels.Color

class Box(position: Vector3, scale: Vector3, color: Color, reflectivity: Float, emission: Float) :
    Solid(position, color, reflectivity, emission) {
    private val min: Vector3
    private val max: Vector3

    init {
        max = position.add(scale.multiply(0.5f))
        min = position.subtract(scale.multiply(0.5f))
    }

    override fun calculateIntersection(ray: Ray): Vector3? {
        var t1: Float
        var t2: Float
        var tnear = Float.NEGATIVE_INFINITY
        var tfar = Float.POSITIVE_INFINITY
        var temp: Float
        var intersectFlag = true
        val rayDirection: FloatArray = ray.direction.toArray()
        val rayOrigin: FloatArray = ray.origin.toArray()
        val b1: FloatArray = min.toArray()
        val b2: FloatArray = max.toArray()
        for (i in 0..2) {
            if (rayDirection[i] == 0f) {
                if (rayOrigin[i] < b1[i] || rayOrigin[i] > b2[i]) intersectFlag = false
            } else {
                t1 = (b1[i] - rayOrigin[i]) / rayDirection[i]
                t2 = (b2[i] - rayOrigin[i]) / rayDirection[i]
                if (t1 > t2) {
                    temp = t1
                    t1 = t2
                    t2 = temp
                }
                if (t1 > tnear) tnear = t1
                if (t2 < tfar) tfar = t2
                if (tnear > tfar) intersectFlag = false
                if (tfar < 0) intersectFlag = false
            }
        }
        return if (intersectFlag) ray.origin.add(ray.direction.multiply(tnear)) else null
    }

    operator fun contains(point: Vector3): Boolean {
        return point.x >= min.x && point.y >= min.y && point.z >= min.z && point.x <= max.x && point.y <= max.y && point.z <= max.z
    }

    override fun getNormalAt(point: Vector3): Vector3 {
        val direction: FloatArray = point.subtract(position).toArray()
        var biggestValue = Float.NaN
        for (i in 0..2) {
            if (java.lang.Float.isNaN(biggestValue) || biggestValue < Math.abs(direction[i])) {
                biggestValue = Math.abs(direction[i])
            }
        }
        if (biggestValue == 0f) {
            return Vector3(0f, 0f, 0f)
        } else {
            for (i in 0..2) {
                if (Math.abs(direction[i]) == biggestValue) {
                    val normal = floatArrayOf(0f, 0f, 0f)
                    normal[i] = if (direction[i] > 0) 1f else -1f
                    return Vector3(normal[0], normal[1], normal[2])
                }
            }
        }
        return Vector3(0f, 0f, 0f)
    }

    override fun getTextureColor(point: Vector3): Color {
        return color
    }
}