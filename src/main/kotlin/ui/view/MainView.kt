package ui.view

import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Pos
import tornadofx.*

class MainView : View("Cup and Spoon") {

    var labelText = SimpleStringProperty("/tornado.jpg")
    var imageName = "/tornado.jpg"

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
                if (labelText.get() == "/tornado.jpg")
                    labelText.set("/god.jpeg")
                else
                    labelText.set("/tornado.jpg")
            }
        }

    }
}