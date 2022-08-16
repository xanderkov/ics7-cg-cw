package raytracing.rendering

import raytracing.math.Ray
import raytracing.math.RayHit
import raytracing.math.Vector3
import raytracing.solids.Solid
import java.util.concurrent.CopyOnWriteArrayList


class Scene {
    val camera: Camera
    val light: Light
    private val solids: CopyOnWriteArrayList<Solid>
    val skybox: Skybox

    init {
        solids = CopyOnWriteArrayList()
        camera = Camera()
        light = Light(Vector3(-1f, 2f, -1f))
        skybox = Skybox("Sky.jpg")
    }

    fun addSolid(solid: Solid) {
        solids.add(solid)
    }

    fun clearSolids() {
        solids.clear()
    }

    fun raycast(ray: Ray): RayHit? {
        var closestHit: RayHit? = null
        for (solid in solids) {
            if (solid == null) continue
            val hitPos: Vector3? = solid.calculateIntersection(ray)
            if (hitPos != null && (closestHit == null || Vector3.distance(
                    closestHit.position,
                    ray.origin
                ) > Vector3.distance(hitPos, ray.origin))
            ) {
                closestHit = RayHit(ray, solid, hitPos)
            }
        }
        return closestHit
    }
}
