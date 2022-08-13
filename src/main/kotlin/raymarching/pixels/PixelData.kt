package raymarching.pixels



class PixelData(depth: Float, emission: Float) {
    var depth: Float
        private set
    var emission: Float
        private set
    init {
        this.depth = depth
        this.emission = emission
    }

    fun add(other: PixelData) {
        depth = (depth + other.depth) / 2f
        emission = emission + other.emission
    }

}