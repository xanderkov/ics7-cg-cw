package ui.view

import javafx.geometry.Pos
import tornadofx.*

class SettingsView() : View("Settings") {

    var resolution_slider = slider()
    var lightPosX = textfield()
    var lightPosY = textfield()
    var lightPosZ = textfield()
    var cupSliderR = slider()
    var cupSliderF = slider()

    var waterSliderR = slider()
    var waterSliderF = slider()

    var boxSliderR = slider()
    var boxSliderF = slider()

    override val root = hbox {

        alignment = Pos.CENTER
        spacing = 10.0
        val sliders = vbox {
            alignment = Pos.CENTER
            spacing = 10.0
            label { text = "Разрешающая способность" }
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
                        fieldset("Позиция света") {
                            field("Позиция света x") {
                                lightPosX = textfield("-1")
                            }
                             field("Позиция света y") {
                                lightPosY = textfield("4")
                            }
                            field("Позиция света z") {
                                lightPosZ = textfield("1")
                            }
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

                    label { text = "Коэффициент поглощения \nчаши (внешний цилиндр)" }
                    cupSliderR = slider {
                        min = 0.01
                        max = 1.0
                        value = 0.1
                    }

                    label { text = "Коэффициент преломления \nчаши (внешний цилиндр)" }
                    cupSliderF = slider {
                        min = 0.01
                        max = 1.0
                        value = 0.8
                    }

                    label { text = "Коэффициент поглощения \nжидкости (внетренний цилиндр)" }
                    waterSliderR = slider {
                        min = 0.01
                        max = 1.0
                        value = 0.1
                    }

                    label { text = "Коэффициент преломления \nжидкости (внетренний цилиндр)" }
                    waterSliderF = slider {
                        min = 0.01
                        max = 1.0
                        value = 0.8
                    }

                    label { text = "Коэффициент поглощения \nСтержня" }
                    boxSliderR = slider {
                        min = 0.01
                        max = 1.0
                        value = 0.8
                    }

                    label { text = "Коэффициент преломления \nСтержня" }
                    boxSliderF = slider {
                        min = 0.01
                        max = 1.0
                        value = 0.8
                    }
                }
            }
        }

        fun makeImageProperties() {
            MainView.resolutionf = resolution_slider.value.toFloat()

            MainView.scene!!.light.position.x = lightPosX.text.toFloat()
            MainView.scene!!.light.position.y = lightPosY.text.toFloat()
            MainView.scene!!.light.position.z = lightPosZ.text.toFloat()

            MainView.scene!!.solids[3].reflectivity = waterSliderR.value.toFloat()
            MainView.scene!!.solids[3].fractivity = waterSliderF.value.toFloat()

            MainView.scene!!.solids[4].reflectivity = cupSliderR.value.toFloat()
            MainView.scene!!.solids[4].fractivity = cupSliderF.value.toFloat()

            MainView.scene!!.solids[0].reflectivity = boxSliderR.value.toFloat()
            MainView.scene!!.solids[0].fractivity = boxSliderF.value.toFloat()
            MainView.scene!!.solids[1].reflectivity = boxSliderR.value.toFloat()
            MainView.scene!!.solids[1].fractivity = boxSliderF.value.toFloat()
            MainView.scene!!.solids[2].reflectivity = boxSliderR.value.toFloat()
            MainView.scene!!.solids[2].fractivity = boxSliderF.value.toFloat()

            MainView.renderToImage(MainView.WIDTH, MainView.HEIGHT)
        }


        val camangles = vbox {
            alignment = Pos.CENTER
            spacing = 10.0
            run {
                label { text = "Камера \n Углы поворота" }
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
                label { text = "Камера \n Позиция" }
                button {
                    text = "Вперед"
                    action {
                        MainView.camera.position.z += 0.2f
                        makeImageProperties()
                    }
                }
                hbox {
                    alignment = Pos.CENTER
                    spacing = 10.0
                    button {
                        text = "Влево"
                        action {
                            MainView.camera.position.x -= 0.2f
                            makeImageProperties()
                        }
                    }
                    button {
                        text = "Вправо"
                        action {
                            MainView.camera.position.x += 0.2f
                            makeImageProperties()
                        }
                    }

                }
                button {
                    text = "Назад"
                    action {
                        MainView.camera.position.z -= 0.2f
                        makeImageProperties()
                    }
                }
                hbox {
                    alignment = Pos.CENTER
                    spacing = 10.0
                    button {
                        text = "Вниз"
                        action {
                            MainView.camera.position.y -= 0.2f
                            makeImageProperties()
                        }
                    }
                    button {
                        text = "Вверх"
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