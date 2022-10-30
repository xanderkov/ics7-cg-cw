package raytracing.rendering

import raytracing.math.Ray
import raytracing.math.RayHit
import raytracing.math.Vector3
import raytracing.solids.Solid
import java.util.concurrent.CopyOnWriteArrayList


class Scene {
    val camera: Camera = Camera()
    val light: Light = Light(Vector3(-1f, 2f, -1f))
    val solids: CopyOnWriteArrayList<Solid> = CopyOnWriteArrayList()
    val skybox: Skybox = Skybox("Sky.jpg")

    fun addSolid(solid: Solid) {
        solids.add(solid)
    }

    fun raycast(ray: Ray): RayHit? {
        var closestHit: RayHit? = null
        for (solid in solids) {
            if (solid == null) continue
            val hitPos: Vector3? = solid.calculateIntersection(ray)
            if (hitPos != null) {
                val oldRay = Vector3.distance(hitPos, ray.origin)
                if (closestHit == null || Vector3.distance(closestHit.position, ray.origin) > oldRay) {
                    closestHit = RayHit(ray, solid, hitPos)
                }
            }
        }
        return closestHit
    }
}
