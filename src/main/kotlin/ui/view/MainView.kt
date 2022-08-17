package ui.view

import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.embed.swing.SwingFXUtils
import javafx.geometry.Pos
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import raytracing.math.Vector3
import raytracing.pixels.Color
import raytracing.rendering.Camera
import raytracing.rendering.Renderer
import raytracing.rendering.Scene
import raytracing.solids.Box
import raytracing.solids.Plane
import raytracing.solids.Sphere
import raytracing.solids.Wall
import tornadofx.*
import java.awt.image.BufferedImage
import java.io.File
import java.io.FileOutputStream
import javax.imageio.ImageIO

class MainView : View("Cup and Spoon") {


    companion object {
        var labelText = SimpleStringProperty("/output.png")
        var scene: Scene? = null
        var camera: Camera = Camera()
        var cameraYaw = 0f
        var cameraPitch = 0f
        val WIDTH: Int = 1000
        val HEIGHT: Int = 700
        val captureCursor = false

        var resolutionf: Float = 1f

        var cameraMotion: Vector3 = Vector3(0f, 0f, 0f)
        var cameraPosition: Vector3 = Vector3(0f, 0f, 0f)

        var image = BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB)

        val currentImageProperty = SimpleObjectProperty<Image>(SwingFXUtils.toFXImage(image, null))
        fun renderToImage(width: Int, height: Int): BufferedImage {
            Renderer.renderScene(scene!!, image.graphics, width, height, resolutionf)
            val imgFile = File("target/classes/output.png")
            ImageIO.write(image, "PNG", FileOutputStream(imgFile))
            return image
        }

        init {
            scene = Scene()
            camera = scene!!.camera
            scene!!.light.setPosition(Vector3(-1f, 2f, 1f))
            cameraPosition.translate(Vector3(-0.5f, 0f, -1f))
            scene!!.addSolid(Sphere(Vector3(2f, 1.5f, 0f), 0.4f, Color.DARK_GRAY, 1f, 0f))

            scene!!.addSolid(Box(Vector3(0f, 0f, 2f), Vector3(1f, 1f, 1f), Color.BLUE, 0.5f, 0.2f))

            //scene!!.addSolid(Cylinders(Vector3(0f, -0.5f, 2f), 2f, 2f, Color.WHITE, 0.6f, 0f))

            scene!!.addSolid(Plane(-1f, Color(0f, 0f, 0f), true, 0.1f, 0f))
            scene!!.addSolid(Wall(-5f, Color(0f, 0f, 0f), true, 0.1f, 0f))

            camera.position = cameraPosition
            camera.yaw = 0f
            camera.pitch = -10f

            cameraYaw = camera.yaw
            cameraPitch = camera.pitch

            image = renderToImage(WIDTH, HEIGHT)

        }

        fun runMainLoop() {
            while (true) {
                currentImageProperty.set(SwingFXUtils.toFXImage(image, null))
            }
        }

    }
    var img: ImageView = imageview(SwingFXUtils.toFXImage(image, null))


    override val root = hbox {
        run {
            alignment = Pos.CENTER
            spacing = 10.0
            prefWidth = 1200.0
            prefHeight = 700.0
            img = imageview(currentImageProperty)

        }

    }


}