package ui.view

import javafx.collections.FXCollections
import javafx.geometry.Pos
import raytracing.pixels.Color
import tornadofx.*

class SettingsView() : View("Settings") {

    private val allColors = FXCollections.observableArrayList<String>(
        "Темно серый", "Черный", "Голубой", "Красный", "Зеленый"
    )

    private var resolutionSlider = slider()
    private var lightPosX = textfield()
    private var lightPosY = textfield()
    private var lightPosZ = textfield()
    private var cupSliderR = slider()
    private var cupSliderF = slider()

    private var waterSliderR = slider()
    var waterSliderF = slider()

    var boxSliderR = slider()
    var boxSliderF = slider()

    private var cupColor = combobox<String>()
    private var boxColor = combobox<String>()
    private var waterColor = combobox<String>()

    override val root = hbox {

        alignment = Pos.CENTER
        spacing = 10.0
        vbox {
            alignment = Pos.CENTER
            spacing = 10.0
            label { text = "Разрешающая способность" }
            resolutionSlider = slider {
                min = 0.0
                max = 1.0
                value = 0.25
            }
        }


        vbox {
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
                        value = 0.1
                    }

                    label { text = "Коэффициент преломления \nСтержня" }
                    boxSliderF = slider {
                        min = 0.01
                        max = 1.0
                        value = 0.1
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

                    label { text = "Цвет чаши\n (внешний цилиндр)" }
                    cupColor = combobox {
                        value = "Темной серый"
                        items = allColors
                    }

                    label { text = "Цвет жидкости\n" +
                            " (внешний цилиндр)" }
                    waterColor = combobox {
                        value = "Голубой"
                        items = allColors
                    }

                    label { text = "Цвет стержня\n"}
                    boxColor = combobox {
                        value = "Темной серый"
                        items = allColors
                    }
                }
            }
        }

        fun stringToColor(SomeCombo: String): Color {
            if (SomeCombo == "Темно серый") {
                return Color.DARK_GRAY
            }
            if (SomeCombo == "Голубой") {
                return Color.BLUE
            }
            if (SomeCombo == "Черный") {
                return Color.BLACK
            }
            if (SomeCombo == "Красный") {
                return Color.RED
            }
            if (SomeCombo == "Зеленый") {
                return Color.GREEN
            }
            return Color.BLACK
        }

        fun makeImageProperties() {
            MainView.resolutionf = resolutionSlider.value.toFloat()

            MainView.scene!!.light.position.x = lightPosX.text.toFloat()
            MainView.scene!!.light.position.y = lightPosY.text.toFloat()
            MainView.scene!!.light.position.z = lightPosZ.text.toFloat()

            // Стержень
            MainView.scene!!.solids[0].reflectivity = boxSliderR.value.toFloat()
            MainView.scene!!.solids[0].fractivity = boxSliderF.value.toFloat()
            MainView.scene!!.solids[1].reflectivity = boxSliderR.value.toFloat()
            MainView.scene!!.solids[1].fractivity = boxSliderF.value.toFloat()

            MainView.scene!!.solids[0].color = stringToColor(boxColor.value)
            MainView.scene!!.solids[1].color = stringToColor(boxColor.value)
            // Чаша
            MainView.scene!!.solids[2].reflectivity = cupSliderR.value.toFloat()
            MainView.scene!!.solids[2].fractivity = cupSliderF.value.toFloat()

            MainView.scene!!.solids[2].color = stringToColor(cupColor.value)
            // Жидкость
            MainView.scene!!.solids[3].reflectivity = waterSliderR.value.toFloat()
            MainView.scene!!.solids[3].fractivity = waterSliderF.value.toFloat()

            MainView.scene!!.solids[3].color = stringToColor(waterColor.value)



            MainView.renderToImage(MainView.WIDTH, MainView.HEIGHT)
        }


        vbox {
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

        vbox {
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
                    button {
                        text = "Обновить\n изображение"
                        action { makeImageProperties() }
                    }
                }
            }
        }

    }
}