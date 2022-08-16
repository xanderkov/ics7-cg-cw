package raytracing.pixels


class PixelData(var color: Color, var depth: Float, var emission: Float) {

    fun add(other: PixelData) {
        color.addSelf(other.color)
        depth = (depth + other.depth) / 2f
        emission = emission + other.emission
    }

    fun multiply(brightness: Float) {
        color = color.multiply(brightness)
    }
}
