package ui

import ui.app.Styles
import ui.view.MainView
import javafx.stage.Stage
import tornadofx.*
import ui.view.SettingsView
import java.util.Arrays


class MyApp: App(MainView::class, Styles::class) {
    override fun start(stage: Stage) {
        with(stage) {
            width = 1200.0
            height = 700.0

        }
        super.start(stage)
        SettingsView().openWindow()
    }

}
fun main(args: Array<String>) {
    launch<MyApp>(args)
}