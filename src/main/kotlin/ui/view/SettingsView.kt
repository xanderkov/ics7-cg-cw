package ui.view

import javafx.geometry.Pos
import tornadofx.*

class SettingsView() : View("Settings") {

    var resolution_slider = slider()
    var lightPosX = textfield()
    var lightPosY = textfield()
    var lightPosZ = textfield()

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


        val lightMotion = vbox {
            alignment = Pos.CENTER
            spacing = 10.0
            run {
                vbox {
                    alignment = Pos.CENTER
                    spacing = 10.0
                    form {
                        fieldset("Light position") {
                            field("Light position x") {
                                lightPosX = textfield("-1")
                            }
                             field("Light position y") {
                                lightPosY = textfield("4")
                            }
                            field("Light position z") {
                                lightPosZ = textfield("1")
                            }
                        }
                    }
                }
            }
        }

        fun makeImageProperties() {
            MainView.resolutionf = resolution_slider.value.toFloat()

            MainView.scene!!.light.position.x = lightPosX.text.toFloat()
            MainView.scene!!.light.position.y = lightPosX.text.toFloat()
            MainView.scene!!.light.position.z = lightPosX.text.toFloat()

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

       vbox {
            alignment = Pos.CENTER
            spacing = 10.0
            run {
                vbox {
                    alignment = Pos.CENTER
                    spacing = 10.0

                }
            }
        }

    }
}