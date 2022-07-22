package UI.view

import UI.app.Styles
import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Pos
import tornadofx.*

class MainView : View("Hello TornadoFX") {

    var labelText = SimpleStringProperty()
    var texts = "AMOGUS"
    override val root = vbox {
        alignment = Pos.CENTER
        spacing = 10.0


        label(title) {
            addClass(Styles.heading)
        }
        label(labelText) {
            bind(labelText)
            addClass(Styles.heading)
        }
        button {
            this.text = "AAAAAAA"
            action {
                print("AAAAAAAAAAA\n")
                texts += " SUS "
                labelText.set(texts)
            }
        }
    }
}