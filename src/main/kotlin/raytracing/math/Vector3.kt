package raytracing.math

import java.lang.Float
import kotlin.FloatArray
import kotlin.String
import kotlin.floatArrayOf
import kotlin.require
import kotlin.math.*

class Vector3(x: kotlin.Float, y: kotlin.Float, z: kotlin.Float) {
    var x: kotlin.Float
    var y: kotlin.Float
    var z: kotlin.Float

    init {
        require(!(Float.isNaN(x) || Float.isNaN(y) || Float.isNaN(z))) { "One or more parameters are NaN" }
        this.x = x
        this.y = y
        this.z = z
    }

    fun add(vec: Vector3): Vector3 {
        return Vector3(x + vec.x, y + vec.y, z + vec.z)
    }

    fun subtract(vec: Vector3): Vector3 {
        return Vector3(x - vec.x, y - vec.y, z - vec.z)
    }

    fun multiply(scalar: kotlin.Float): Vector3 {
        return Vector3(x * scalar, y * scalar, z * scalar)
    }

    fun multiply(vec: Vector3): Vector3 {
        return Vector3(x * vec.x, y * vec.y, z * vec.z)
    }

    fun divide(vec: Vector3): Vector3 {
        return Vector3(x / vec.x, y / vec.y, z / vec.z)
    }

    fun length(): kotlin.Float {
        return sqrt(x * x + y * y + z * z)
    }

    fun normalize(): Vector3 {
        val length = length()
        return Vector3(x / length, y / length, z / length)
    }

    fun rotateYP(yaw: kotlin.Float, pitch: kotlin.Float): Vector3 {
        // Convert to radians
        val yawRads = Math.toRadians(yaw.toDouble())
        val pitchRads = Math.toRadians(pitch.toDouble())

        // Step one: Rotate around X axis (pitch)
        val _y = (y * Math.cos(pitchRads) - z * Math.sin(pitchRads)).toFloat()
        var _z = (y * Math.sin(pitchRads) + z * Math.cos(pitchRads)).toFloat()

        // Step two: Rotate around the Y axis (yaw)
        val _x = (x * Math.cos(yawRads) + _z * Math.sin(yawRads)).toFloat()
        _z = (-x * Math.sin(yawRads) + _z * Math.cos(yawRads)).toFloat()
        return Vector3(_x, _y, _z)
    }

    /** Does the same as Vector3.add but changes the vector itself instead of returning a new one  */
    fun translate(vec: Vector3) {
        x += vec.x
        y += vec.y
        z += vec.z
    }

    fun clone(): Vector3 {
        return Vector3(x, y, z)
    }

    override fun toString(): String {
        return "Vector3{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}'
    }

    fun toArray(): FloatArray {
        return floatArrayOf(x, y, z)
    }

    operator fun plus(vec: Vector3): Vector3 {
        return Vector3(x + vec.x, y + vec.y, z + vec.z)
    }

    operator fun minus(vec: Vector3): Vector3 {
        return Vector3(x - vec.x, y - vec.y, z - vec.z)
    }

    operator fun times(vec: Vector3): Vector3 {
        return Vector3(x * vec.x, y * vec.y, z * vec.z)
    }

    operator fun unaryMinus(): Vector3 {
        return Vector3(-x, -y, -z)
    }

    companion object {
        fun distance(a: Vector3, b: Vector3): kotlin.Float {
            return Math.sqrt(
                Math.pow((a.x - b.x).toDouble(), 2.0) + Math.pow(
                    (a.y - b.y).toDouble(),
                    2.0
                ) + Math.pow((a.z - b.z).toDouble(), 2.0)
            ).toFloat()
        }

        fun dot(a: Vector3, b: Vector3): kotlin.Float {
            return a.x * b.x + a.y * b.y + a.z * b.z
        }

        fun lerp(a: Vector3, b: Vector3, t: kotlin.Float): Vector3 {
            return a.add(b.subtract(a).multiply(t))
        }
    }

}