package raytracing.math

class Ray(origin: Vector3, direction: Vector3) {
    val origin: Vector3
    val direction: Vector3

    init {
        var direction = direction
        this.origin = origin
        if (direction.length() !== 1.0f) {
            direction = direction.normalize()
        }
        this.direction = direction
    }

    fun asLine(length: Float): Line {
        return Line(origin, origin.add(direction.multiply(length)))
    }

}
