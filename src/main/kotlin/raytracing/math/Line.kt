package raytracing.math

class Line(var pointA: Vector3, var pointB: Vector3) {
    fun asRay(): Ray {
        return Ray(pointA, pointB.subtract(pointA).normalize())
    }
}