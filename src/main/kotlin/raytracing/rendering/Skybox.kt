package raytracing.rendering

import raytracing.math.Vector3
import raytracing.pixels.Color
import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import javax.imageio.ImageIO

class Skybox(resourceName: String) {
    private var sphereImage: BufferedImage
    var isLoaded: Boolean
        private set

    init {
        sphereImage = BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB)
        isLoaded = false
        object : Thread("Skybox loader") {
            override fun run() {
                try {
                    println("Loading skybox $resourceName...")
                    sphereImage = ImageIO.read(javaClass.getResourceAsStream("/Sky.jpg"))
                    println("Skybox ready.")
                    isLoaded = true
                } catch (ex: IOException) {
                    try {
                        isLoaded = true
                    } catch (ex2: IOException) {
                        ex2.printStackTrace()
                        System.exit(-1)
                    } catch (ex2: IllegalArgumentException) {
                        ex2.printStackTrace()
                        System.exit(-1)
                    }
                    ex.printStackTrace()
                } catch (ex: IllegalArgumentException) {
                    try {
                        isLoaded = true
                    } catch (ex2: IOException) {
                        ex2.printStackTrace()
                        System.exit(-1)
                    } catch (ex2: IllegalArgumentException) {
                        ex2.printStackTrace()
                        System.exit(-1)
                    }
                    ex.printStackTrace()
                }
            }
        }.start()
    }

    fun getColor(d: Vector3): Color {
        // Convert Unit vector to texture coordinates (Wikipedia UV Unwrapping page)
        val u = (0.5 + Math.atan2(d.z.toDouble(), d.x.toDouble()) / (2 * Math.PI)).toFloat()
        val v = (0.5 - Math.asin(d.y.toDouble()) / Math.PI).toFloat()
        return try {
            Color.fromInt(
                sphereImage.getRGB(
                    (u * (sphereImage.width - 1)).toInt(),
                    (v * (sphereImage.height - 1)).toInt()
                )
            )
        } catch (e: Exception) {
            println("U: $u V: $v")
            e.printStackTrace()
            Color.MAGENTA
        }
    }

    fun reload(resourceName: String) {
        sphereImage = BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB)
        isLoaded = false
        object : Thread("Skybox loader") {
            override fun run() {
                try {
                    println("Loading skybox $resourceName...")
                    println("Skybox ready.")
                    isLoaded = true
                } catch (ex: IOException) {
                    try {
                        isLoaded = true
                    } catch (ex2: IOException) {
                        ex2.printStackTrace()
                        System.exit(-1)
                    } catch (ex2: IllegalArgumentException) {
                        ex2.printStackTrace()
                        System.exit(-1)
                    }
                    ex.printStackTrace()
                } catch (ex: IllegalArgumentException) {
                    try {
                        isLoaded = true
                    } catch (ex2: IOException) {
                        ex2.printStackTrace()
                        System.exit(-1)
                    } catch (ex2: IllegalArgumentException) {
                        ex2.printStackTrace()
                        System.exit(-1)
                    }
                    ex.printStackTrace()
                }
            }
        }.start()
    }

    fun reloadFromFile(file: File) {
        sphereImage = BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB)
        isLoaded = false
        object : Thread("Skybox loader") {
            override fun run() {
                try {
                    println("Loading skybox " + file.name + "...")
                    sphereImage = ImageIO.read(file)
                    println("Skybox ready.")
                    isLoaded = true
                } catch (ex: IOException) {
                    try {
                        isLoaded = true
                    } catch (ex2: IOException) {
                        ex2.printStackTrace()
                        System.exit(-1)
                    } catch (ex2: IllegalArgumentException) {
                        ex2.printStackTrace()
                        System.exit(-1)
                    }
                    ex.printStackTrace()
                } catch (ex: IllegalArgumentException) {
                    try {
                        isLoaded = true
                    } catch (ex2: IOException) {
                        ex2.printStackTrace()
                        System.exit(-1)
                    } catch (ex2: IllegalArgumentException) {
                        ex2.printStackTrace()
                        System.exit(-1)
                    }
                    ex.printStackTrace()
                }
            }
        }.start()
    }
}