package ui.view

import javafx.beans.property.SimpleStringProperty
import javafx.embed.swing.SwingFXUtils
import javafx.geometry.Pos
import javafx.scene.image.ImageView
import raymarching.math.*
import raymarching.pixels.Color
import raymarching.rendering.Camera
import raymarching.rendering.Renderer
import raymarching.rendering.Scene
import raymarching.solids.Box
import raymarching.solids.Plane
import raymarching.solids.Sphere
import tornadofx.*
import java.awt.image.BufferedImage
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.imageio.ImageIO

class MainView : View("Cup and Spoon") {

    var labelText = SimpleStringProperty("/output.png")

    private var scene: Scene? = null
    private var camera: Camera = Camera()
    private var cameraYaw = 0f
    private var cameraPitch = 0f
    private val WIDTH: Int = 1000
    private val HEIGHT: Int = 600
    val captureCursor = false

    private val cameraMotion: Vector3? = null
    private val cameraPosition: Vector3 = Vector3(0f, 0f, 0f)

    var image = BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB)

    val renderer: Renderer? = null
    init {
        scene = Scene()
        camera = scene!!.camera

        cameraPosition.translate(Vector3(-0.5f, 0f, -1f))
        scene!!.addSolid(Sphere(Vector3(0f, -0.5f, 0f), 0.4f, Color.RED, 0.4f, 0f))
        scene!!.addSolid(Sphere(Vector3(0.5f, -0.5f, 0f), 0.4f, Color.GREEN, 0.4f, 0f))
        scene!!.addSolid(Sphere(Vector3(0.25f, 0f, 0f), 0.4f, Color.BLUE, 0.5f, 0f))
        scene!!.addSolid(Sphere(Vector3(0.25f, 0.5f, 0f), 0.4f, Color.BLUE, 0.5f, 0f))

        scene!!.addSolid(Box(Vector3(0f, 0f, 0f), Vector3(-1f, 1f, 1f), Color.BLUE, 0.5f, 0f))

        scene!!.addSolid(Plane(-1f, Color(0f, 0f, 0f), true, 0.1f, 0f))

        camera.position = cameraPosition
        camera.yaw = 0f
        camera.pitch = -10f

        cameraYaw = camera.yaw
        cameraPitch = camera.pitch

    }

    private val postProcessing = false
    @Throws(IOException::class)
    fun renderToImage(width: Int, height: Int) {

        println("Rendering to image...")
        Renderer.renderScene(scene!!, image.graphics, width, height, 1f)
        val imgFile = File("src/main/resources/output.png")
        ImageIO.write(image, "PNG", FileOutputStream(imgFile))
        println("Image saved.")
    }

    override val root = hbox {
        run {
            alignment = Pos.CENTER
            spacing = 10.0
            prefWidth = 1200.0
            prefHeight = 700.0
            var img: ImageView = imageview(SwingFXUtils.toFXImage(image, null))

            vbox {
                alignment = Pos.CENTER
                spacing = 10.0
                run {
                    button {
                        text = "REFRESH"
                        action {
                            renderToImage(WIDTH, HEIGHT)
                            img.setImage(SwingFXUtils.toFXImage(image, null))
                        }
                    }

                    label { text = "Camera Angles" }

                    button {
                        text = "->"
                        action {
                            camera.yaw += 20f
                            renderToImage(WIDTH, HEIGHT)
                            img.setImage(SwingFXUtils.toFXImage(image, null))
                        }
                    }
                    button {
                        text = "<-"
                        action {
                            camera.yaw -= 20f
                            renderToImage(WIDTH, HEIGHT)
                            img.setImage(SwingFXUtils.toFXImage(image, null))
                        }
                    }

                    button {
                        text = "v"
                        action {
                            camera.pitch += 20f
                            renderToImage(WIDTH, HEIGHT)
                            img.setImage(SwingFXUtils.toFXImage(image, null))
                        }
                    }
                    button {
                        text = "/"
                        action {
                            camera.pitch -= 20f
                            renderToImage(WIDTH, HEIGHT)
                            img.setImage(SwingFXUtils.toFXImage(image, null))
                        }
                    }
                }
            }
        }
    }

}