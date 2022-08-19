package ui.view

import javafx.beans.property.SimpleFloatProperty
import javafx.beans.property.SimpleStringProperty
import javafx.embed.swing.SwingFXUtils
import javafx.geometry.Pos
import javafx.scene.Parent
import javafx.scene.image.ImageView
import org.w3c.dom.events.EventTarget
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

    var resolution_slider = slider()
    override val root = hbox {

        alignment = Pos.CENTER
        spacing = 10.0
        val sliders = vbox {
            alignment = Pos.CENTER
            spacing = 10.0
            label { text = "resolution" }
            resolution_slider = slider {
                min = 0.0
                max = 1.0
                value = 0.25
            }
        }

        fun makeImageProperties() {
            MainView.resolutionf = resolution_slider.value.toFloat()
            MainView.renderToImage(MainView.WIDTH, MainView.HEIGHT)
        }


        val camangles = vbox {
            alignment = Pos.CENTER
            spacing = 10.0
            run {
                label { text = "Camera \n Angles" }
                button {
                    text = "↑"
                    action {
                        MainView.camera.pitch -= 20f
                        makeImageProperties()
                    }
                }
                hbox {
                    alignment = Pos.CENTER
                    spacing = 10.0
                    button {
                        text = "←"
                        action {
                            MainView.camera.yaw -= 20f
                            makeImageProperties()
                        }
                    }
                    button {
                        text = "→"
                        action {
                            MainView.camera.yaw += 20f
                            makeImageProperties()
                        }
                    }

                }
                button {
                    text = "↓"
                    action {

                        MainView.camera.pitch += 20f
                        makeImageProperties()
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
                        makeImageProperties()
                    }
                }
                hbox {
                    alignment = Pos.CENTER
                    spacing = 10.0
                    button {
                        text = "A"
                        action {
                            MainView.camera.position.x -= 0.2f
                            makeImageProperties()
                        }
                    }
                    button {
                        text = "D"
                        action {
                            MainView.camera.position.x += 0.2f
                            makeImageProperties()
                        }
                    }

                }
                button {
                    text = "S"
                    action {
                        MainView.camera.position.z -= 0.2f
                        makeImageProperties()
                    }
                }
                hbox {
                    alignment = Pos.CENTER
                    spacing = 10.0
                    button {
                        text = "Shift"
                        action {
                            MainView.camera.position.y -= 0.2f
                            makeImageProperties()
                        }
                    }
                    button {
                        text = "Space"
                        action {
                            MainView.camera.position.y += 0.2f
                            makeImageProperties()
                        }
                    }

                }
            }
        }
    }
}