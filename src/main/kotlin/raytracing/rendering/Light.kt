package raytracing.rendering

import raytracing.math.Vector3

class Light(position: Vector3) {
    private var position: Vector3

    init {
        this.position = position
    }

    fun getPosition(): Vector3 {
        return position
    }

}
