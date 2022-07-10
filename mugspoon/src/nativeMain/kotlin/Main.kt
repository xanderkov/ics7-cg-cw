import org.gtk.dsl.gio.onCreateUI
import org.gtk.dsl.gtk.application
import org.gtk.dsl.gtk.applicationWindow
import org.gtk.dsl.gtk.button
import org.gtk.dsl.gtk.onClicked

fun main() {
    application("org.gnome.kotlin.test") {
        onCreateUI {
            applicationWindow {
                button ("Create new winodow") {
                    onClicked {
                            println("Hehe now window")
                        }
                    }
            }.show()
        }
    }
}