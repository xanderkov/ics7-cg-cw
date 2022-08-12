package raymarching.rendering

import raymarching.math.Ray
import raymarching.math.RayHit
import raymarching.math.Vector3
import raymarching.solids.Solid
import java.util.concurrent.CopyOnWriteArrayList

class Scene {
    private val camera: Camera
    private val light: Light
    private val solids: CopyOnWriteArrayList<Solid>
    private val skybox: Skybox

    init {
        solids = CopyOnWriteArrayList<Solid>()
        camera = Camera()
        light = Light(Vector3(-1, 2, -1))
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
            val hitPos: Vector3 = solid.calculateIntersection(ray)
            if (hitPos != null && (closestHit == null || Vector3.distance(
                    closestHit.getPosition(),
                    ray.getOrigin()
                ) > Vector3.distance(hitPos, ray.getOrigin()))
            ) {
                closestHit = RayHit(ray, solid, hitPos)
            }
        }
        return closestHit
    }

    fun getCamera(): Camera {
        return camera
    }

    fun getLight(): Light {
        return light
    }

    fun getSkybox(): Skybox {
        return skybox
    }
}

class Scene {
    private val camera: Camera
    private val light: Light
    private val solids: CopyOnWriteArrayList<Solid>

    init {
        solids = CopyOnWriteArrayList<Solid>()
        camera = Camera()
        light = Light(Vector3(-1f, 2f, -1f))
    }

    fun addSolid(solid: Solid) {
        solids.add(solid)
    }

    fun clearSolids() {
        solids.clear()
    }

    fun raycast(ray: Ray): RayHit {
        val closestHit: RayHit? = null
        for (solid in solids) {
            if (solid == null) continue
            val hitPos: Vector3 = solid.calculateIntersection(ray)
            if (hitPos != null )
        }
    }
}