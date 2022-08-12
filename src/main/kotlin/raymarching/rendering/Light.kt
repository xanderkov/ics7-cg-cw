package raymarching.rendering

import raymarching.math.Vector3

class Light(position: Vector3) {
    private var position: Vector3

    init {
        this.position = position
    }

    fun getPosition(): Vector3 {
        return position
    }

    fun setPosition(position: Vector3) {
        this.position = position
    }
}
