package raytracing.math

class Ray(origin: Vector3, direction: Vector3) {
    val origin: Vector3
    val direction: Vector3

    init {
        var dir = direction
        this.origin = origin
        if (direction.length() != 1.0f) {
            dir = direction.normalize()
        }
        this.direction = dir
    }

    fun asLine(length: Float): Line {
        return Line(origin, origin.add(direction.multiply(length)))
    }

}
