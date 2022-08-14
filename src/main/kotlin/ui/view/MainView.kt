package ui.view

import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Pos
import javafx.scene.image.WritableImage
import raymarching.math.*
import raymarching.pixels.Color
import raymarching.pixels.PixelData
import raymarching.rendering.Camera
import raymarching.rendering.Renderer
import raymarching.rendering.Scene
import raymarching.solids.Solid
import raymarching.solids.Sphere
import tornadofx.*
import java.awt.Graphics
import java.awt.image.BufferedImage
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.imageio.ImageIO

class MainView : View("Cup and Spoon") {

    var labelText = SimpleStringProperty("/tornado.jpg")

    private var scene: Scene? = null
    private var camera: Camera? = null
    private var cameraYaw = 0f
    private var cameraPitch = 0f

    val imageok = WritableImage(1000, 500)

    val renderer: Renderer? = null
    init {
        scene = Scene()
        camera = scene!!.camera


        scene!!.addSolid(Sphere(Vector3(-1f, 0f, 0f), 0.4f, Color.RED, 1f, 0f))


        cameraYaw = camera!!.yaw
        cameraPitch = camera!!.pitch

    }

    private val postProcessing = false
    @Throws(IOException::class)
    fun renderToImage(width: Int, height: Int) {
        val image = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)

        println("Rendering to image...")
        Renderer.renderScene(scene!!, image.graphics, width, height, 1f)
        val imgFile = File("src/main/resources/output.png")
        ImageIO.write(image, "PNG", FileOutputStream(imgFile))
        println("Image saved.")
    }

    override val root = hbox {
        val start = System.currentTimeMillis()
        run {
            alignment = Pos.CENTER
            spacing = 10.0
            // 4. Asynchronous way through lazy loading
            println("-- load asynchronously #2 -- ")
            val start = System.currentTimeMillis()
            imageview(labelText) {
                setPrefSize(1200.0, 600.0)
                println("loaded for ${System.currentTimeMillis() - start} msecs")
            }
            println("finished after ${System.currentTimeMillis() - start} msecs")
        }

        button {
            text = "REFRESH"
            print("Image has been refreshed")
            action {
                renderToImage(500, 500)
                if (labelText.get() == "/tornado.jpg")
                    labelText.set("/output.png")
                else
                    labelText.set("/tornado.jpg")
            }
        }

    }
}