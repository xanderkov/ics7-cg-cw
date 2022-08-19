package raytracing.solids

import javafx.fxml.FXMLLoader
import raytracing.math.Ray
import raytracing.math.Vector3
import raytracing.pixels.Color

class Cylinders(position: Vector3, private val radius: Float, private val height: Float, color: Color, reflectivity: Float, emission: Float):
    Solid(position, color, reflectivity, emission) {

    override fun calculateIntersection(ray: Ray): Vector3? {

        val intersectionPoint = ray.origin.subtract(position)

        var isBelongToCylinderBase = false

        val ts1 = (height - ray.origin.y + position.y) / ray.direction.y
        var point = intersectionPoint.add(ray.direction.multiply(ts1))

        if (point.x * point.x + point.z * point.z - radius * radius < 1e-4)
            isBelongToCylinderBase = true

        val ts2 = (-height - ray.origin.y + position.y) / ray.direction.y
        point = intersectionPoint.add(ray.direction.multiply(ts2))

        if (point.x * point.x + point.z * point.z - radius * radius < 1e-4)
            isBelongToCylinderBase = true

        val a: Float = ray.direction.x * ray.direction.x + ray.direction.z * ray.direction.z

        val b: Float = ray.origin.x * ray.direction.x - ray.direction.x * position.x + ray.origin.z * ray.direction.z -
                ray.direction.z * position.z

        val c: Float = ray.origin.x * ray.origin.x + position.x * position.x + ray.origin.z * ray.origin.z +
                position.z * position.z - 2 * (position.x * ray.origin.x + ray.origin.z * position.z) - radius * radius

        val delta: Float = b * b -  (a * c)

        var t: Float = 0f

        if (delta < 1e-4) {
            if (isBelongToCylinderBase) {
                t = kotlin.math.min(ts1, ts2)
                return ray.origin.add(ray.direction.multiply(t))
            }
            return null
        }

        val t1: Float = (-b - kotlin.math.sqrt(delta)) / (a)
        val t2: Float = (-b + kotlin.math.sqrt(delta)) / (a)
        t = if (t1 < 1e-4) t2 else t1

        if (kotlin.math.abs(ray.origin.y + t * ray.direction.y - position.y) < height)
            if (t > 1e-4)
                return ray.origin.add(ray.direction.multiply(t))
            else return null

        if (!isBelongToCylinderBase) return null
        t = kotlin.math.min(ts1, ts2)
        return ray.origin.add(ray.direction.multiply(t))

    }

    override fun getNormalAt(point: Vector3): Vector3 {
        val normalIntersectionPoint = point.subtract(position)
        return if (kotlin.math.abs(normalIntersectionPoint.y) < height)
            Vector3(normalIntersectionPoint.x, 0f, normalIntersectionPoint.z).normalize()
        else (point.subtract(position).normalize())
    }

    override fun getTextureColor(point: Vector3): Color {
        return color
    }

}