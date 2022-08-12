package raymarching.solids

import raymarching.math.Ray
import raymarching.math.Vector3


abstract class Solid(var position: Vector3, reflectivity: Float, emission: Float) {
    var reflectivity: Float
        protected set
    var emission: Float
        protected set

    init {
        this.reflectivity = reflectivity
        this.emission = emission
    }

    abstract fun calculateIntersection(ray: Ray): Vector3
    abstract fun getNormalAt(point: Vector3): Vector3

}
