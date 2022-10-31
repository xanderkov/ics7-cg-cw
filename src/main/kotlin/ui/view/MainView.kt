package ui.view

import javafx.beans.property.SimpleObjectProperty
import javafx.embed.swing.SwingFXUtils
import javafx.geometry.Pos
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import raytracing.math.Vector3
import raytracing.pixels.Color
import raytracing.rendering.Camera
import raytracing.rendering.Renderer
import raytracing.rendering.Scene
import raytracing.solids.Box
import raytracing.solids.Plane
import raytracing.solids.Cylinders
import tornadofx.*
import java.awt.image.BufferedImage
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.imageio.ImageIO

class MainView : View("Cup and Spoon") {


    companion object {
        var scene: Scene? = null
        var camera: Camera = Camera()
        var cameraYaw = 0f
        var cameraPitch = 0f
        val WIDTH: Int = 1366
        val HEIGHT: Int = 710

        var resolutionf: Float = 0.25f

        var cameraMotion: Vector3 = Vector3(0f, 0f, 0f)
        var cameraPosition: Vector3 = Vector3(0f, 0f, 0f)

        var image = BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB)

        val currentImageProperty = SimpleObjectProperty<Image>(SwingFXUtils.toFXImage(image, null))
        fun renderToImage(width: Int, height: Int){
            cameraPosition.translate(
                cameraMotion.rotateYP(camera.yaw, 0f).multiply(1 / 50f * 1f)
            )

            Renderer.renderScene(scene!!, image.graphics, width, height, resolutionf)
            currentImageProperty.set(SwingFXUtils.toFXImage(image, null))
        }

        init {
            scene = Scene()
            camera = scene!!.camera
            scene!!.light.position = (Vector3(-1f, 4f, 2f))
            cameraPosition.translate(Vector3(-0.5f, 0.5f, -3f))

            //scene!!.addSolid(Sphere(Vector3(2f, 0.5f, 0f), 1.4f, Color.WHITE, 0.1f, 0.8f, 0f))

            //scene!!.addSolid(Sphere(Vector3(-2f, 0.7f, -1f), 1.8f, Color.GREEN, 0.5f, 0.5f, 0f))

            scene!!.addSolid(Box(Vector3(0f, 0f, 0f), Vector3(0.7f, 0.7f, 0.7f), Color.DARK_GRAY, 0.1f, 0f, 0.2f))
            scene!!.addSolid(Box(Vector3(0f, 0.7f, 0f), Vector3(0.7f, 1.4f, 0.7f), Color.DARK_GRAY, 0.1f, 0f, 0.2f))

            scene!!.addSolid(Cylinders(Vector3(0f, 0f, 0f), 1f, 1f, Color.DARK_GRAY, 0.1f, 0.8f, 0f))

            scene!!.addSolid(Cylinders(Vector3(0f, -0.5f, 0f), 0.8f, 0.5f, Color.BLUE, 0.1f, 0.8f, 0f))

            //scene!!.addSolid(Cylinders(Vector3(0f, 0f, 0f), 0.2f, 1.5f, Color.DARK_GRAY, 0.5f, 0.1f, 0f))

            scene!!.addSolid(Plane(-1f, Color(0f, 0f, 0f), true, 0.1f, 0f, 0f))
            //scene!!.addSolid(Wall(5f, Color(0f, 0f, 0f), true, 0.1f, 0f, 0f))

            camera.position = cameraPosition
            camera.yaw = 0f
            camera.pitch = -10f

            cameraYaw = camera.yaw
            cameraPitch = camera.pitch
            renderToImage(WIDTH, HEIGHT)
        }

        fun saveImage() {
            Renderer.renderScene(scene!!, image.graphics, WIDTH, HEIGHT, resolutionf)
            val imgFile = File("src/main/resources/output.png")
            ImageIO.write(image, "PNG", FileOutputStream(imgFile))
            println("Image saved.")
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

            keyboard {
                addEventHandler(KeyEvent.KEY_PRESSED) {
                    if (it.code == KeyCode.D) {
                        cameraMotion.x = 5.2f
                    } else if (it.code == KeyCode.A) {
                        cameraMotion.x = -5.2f
                    } else if (it.code == KeyCode.W) {
                        cameraMotion.z = (5.2f)
                    } else if (it.code == KeyCode.S) {
                        cameraMotion.z = -5.2f
                    } else if (it.code == KeyCode.SPACE) {
                        cameraMotion.y = (5.2f)
                        renderToImage(WIDTH, HEIGHT)
                    } else if (it.code == KeyCode.SHIFT) {
                        cameraMotion.y = -5.2f
                    } else if (it.code == KeyCode.NUMPAD1) {
                        resolutionf = 1f
                    } else if (it.code == KeyCode.NUMPAD2) {
                        resolutionf = 0.5f
                    } else if (it.code == KeyCode.NUMPAD3) {
                        resolutionf = 0.25f
                    } else if (it.code == KeyCode.NUMPAD4) {
                        resolutionf = 0.125f
                    } else if (it.code == KeyCode.F12) {
                        try {
                            saveImage()
                        } catch (ex: IOException) {
                            ex.printStackTrace()
                        }
                    } else if (it.code == KeyCode.F1) {
                        SettingsView().openWindow()
                    }
                    renderToImage(WIDTH, HEIGHT)
                }
                addEventHandler(KeyEvent.KEY_RELEASED) {
                    if (it.code == KeyCode.D) {
                        cameraMotion.x = 0f
                    } else if (it.code == KeyCode.A) {
                        cameraMotion.x = 0f
                    } else if (it.code == KeyCode.W) {
                        cameraMotion.z = 0f
                    } else if (it.code == KeyCode.S) {
                        cameraMotion.z = 0f
                    } else if (it.code == KeyCode.SPACE) {
                        cameraMotion.y = 0f
                        renderToImage(WIDTH, HEIGHT)
                    } else if (it.code == KeyCode.SHIFT) {
                        cameraMotion.y = 0f
                    }
                }
            }


        }

    }

}