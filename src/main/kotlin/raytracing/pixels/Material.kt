package raytracing.pixels

import java.lang.Float

class Material(refractive: kotlin.Float, diffuse: kotlin.Float, specular: kotlin.Float) {
    var refractive: kotlin.Float

    var diffuse: kotlin.Float

    var specular: kotlin.Float


    init {
        this.refractive = refractive
        this.diffuse = diffuse
        this.specular = specular
    }
}