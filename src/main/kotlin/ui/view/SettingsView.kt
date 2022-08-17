package ui.view

import javafx.beans.property.SimpleStringProperty
import javafx.embed.swing.SwingFXUtils
import javafx.geometry.Pos
import javafx.scene.Parent
import javafx.scene.image.ImageView
import raytracing.math.Vector3
import raytracing.pixels.Color
import raytracing.rendering.Camera
import raytracing.rendering.Renderer
import raytracing.rendering.Scene
import raytracing.solids.Box
import raytracing.solids.Plane
import raytracing.solids.Sphere
import tornadofx.*
import java.awt.image.BufferedImage
import java.io.IOException

class SettingsView() : View("Settings") {
    override val root = hbox {
        val resolution_slider = slider(0f, 1f, 0.25f)

        val camangles = vbox {
            alignment = Pos.CENTER
            spacing = 10.0
            run {
                label { text = "Camera \n Angles" }
                button {
                    text = "↑"
                    action {
                        MainView.camera.pitch -= 20f
                        val image = MainView.renderToImage(MainView.WIDTH, MainView.HEIGHT)
                        MainView.currentImageProperty.set(SwingFXUtils.toFXImage(image, null))
                    }
                }
                hbox {
                    alignment = Pos.CENTER
                    spacing = 10.0
                    button {
                        text = "←"
                        action {
                            MainView.camera.yaw -= 20f
                            MainView.renderToImage(MainView.WIDTH, MainView.HEIGHT)
                        }
                    }
                    button {
                        text = "→"
                        action {
                            MainView.camera.yaw += 20f
                            MainView.renderToImage(MainView.WIDTH, MainView.HEIGHT)
                        }
                    }

                }
                button {
                    text = "↓"
                    action {
                        MainView.resolutionf = resolution_slider.value.toFloat()
                        MainView.camera.pitch += 20f
                        MainView.renderToImage(MainView.WIDTH, MainView.HEIGHT)
                    }
                }
            }
        }

        val cammotion = vbox {
            alignment = Pos.CENTER
            spacing = 10.0
            run {
                label { text = "Camera \n Motion" }
                button {
                    text = "W"
                    action {
                        MainView.camera.position.z += 0.2f
                        MainView.renderToImage(MainView.WIDTH, MainView.HEIGHT)
                    }
                }
                hbox {
                    alignment = Pos.CENTER
                    spacing = 10.0
                    button {
                        text = "A"
                        action {
                            MainView.camera.position.x -= 0.2f
                            MainView.renderToImage(MainView.WIDTH, MainView.HEIGHT)
                        }
                    }
                    button {
                        text = "D"
                        action {
                            MainView.camera.position.x += 0.2f
                            MainView.renderToImage(MainView.WIDTH, MainView.HEIGHT)
                        }
                    }

                }
                button {
                    text = "S"
                    action {
                        MainView.camera.position.z -= 0.2f
                        MainView.renderToImage(MainView.WIDTH, MainView.HEIGHT)
                    }
                }
                hbox {
                    alignment = Pos.CENTER
                    spacing = 10.0
                    button {
                        text = "Shift"
                        action {
                            MainView.camera.position.y -= 0.2f
                            MainView.renderToImage(MainView.WIDTH, MainView.HEIGHT)
                        }
                    }
                    button {
                        text = "Space"
                        action {
                            MainView.camera.position.y += 0.2f
                            MainView.renderToImage(MainView.WIDTH, MainView.HEIGHT)
                        }
                    }

                }
            }
        }
    }
}