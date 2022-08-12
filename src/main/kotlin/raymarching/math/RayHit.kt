package raymarching.math

import raymarching.solids.Solid

class RayHit(val ray: Ray, hitSolid: Solid, hitPos: Vector3) {
    private val hitSolid: Solid
    val position: Vector3
    val normal: Vector3

    init {
        this.hitSolid = hitSolid
        position = hitPos
        normal = hitSolid.getNormalAt(hitPos)
    }

    val solid: Solid
        get() = hitSolid
}