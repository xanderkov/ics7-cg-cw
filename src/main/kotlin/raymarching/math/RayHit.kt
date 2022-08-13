package raymarching.math

import raymarching.solids.Solid

class RayHit(val ray: Ray, val solid: Solid, val position: Vector3) {
    val normal: Vector3

    init {
        normal = solid.getNormalAt(position)
    }
}