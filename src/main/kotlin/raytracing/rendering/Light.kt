package raytracing.rendering

import raytracing.math.Vector3

class Light(position: Vector3) {
    var position: Vector3

    init {
        this.position = position
    }
}
