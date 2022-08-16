package raytracing.solids

import raytracing.math.Ray
import raytracing.math.Vector3
import raytracing.pixels.Color


abstract class Solid(var position: Vector3, var color: Color, var reflectivity: Float, var emission: Float) {

    abstract fun calculateIntersection(ray: Ray?): Vector3?
    abstract fun getNormalAt(point: Vector3): Vector3
    abstract fun getTextureColor(point: Vector3?): Color
}

