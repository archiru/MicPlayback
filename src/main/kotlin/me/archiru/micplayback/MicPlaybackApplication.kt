package me.archiru.micplayback

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage

class MicPlaybackApplication : Application() {
    override fun start(stage: Stage) {
        val fxmlLoader = FXMLLoader(MicPlaybackApplication::class.java.getResource("main-view.fxml"))
        val scene = Scene(fxmlLoader.load(), 320.0, 480.0)
        stage.title = "Archie's Mic Playback"
        stage.scene = scene
        stage.show()
    }
}

fun main() {
    Application.launch(MicPlaybackApplication::class.java)
}