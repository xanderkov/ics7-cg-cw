package raymarching.rendering

import raymarching.math.Vector3

class Camera {

    var position: Vector3
    var yaw: Float
    var pitch: Float
    var fOV: Float

    init {
        position = Vector3(0f, 0f, 0f)
        yaw = 0f
        pitch = 0f
        fOV = 60f
    }

    fun translate(vec: Vector3?) {
        position.translate(vec!!)
    }
}