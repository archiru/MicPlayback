package me.archiru.micplayback

import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.ComboBox
import javafx.scene.control.Label
import javax.sound.sampled.LineUnavailableException
import javax.sound.sampled.Mixer


class MicPlaybackController {
    @FXML
    lateinit var outComboBox: ComboBox<Mixer.Info>

    @FXML
    lateinit var playbackButton: Button

    @FXML
    lateinit var micComboBox: ComboBox<Mixer.Info>

    @FXML
    private lateinit var statusText: Label

    private var started: Boolean = false

    private val audioCapture = AudioCapture()

    @FXML
    private fun initialize() {
        val microphones = audioCapture.availableMicrophones
        val outputs = audioCapture.availableOutputs
        micComboBox.items.addAll(microphones)
        outComboBox.items.addAll(outputs)
    }

    @FXML
    private fun onPlaybackButtonClick() {
        started = !started
        if (started) {
            statusText.text = "Playing back your mic"
            playbackButton.text = "Stop playback"
            if (micComboBox.value != null && outComboBox.value != null) {
                val selectedMicrophone: Mixer.Info = micComboBox.value
                val selectedOutput: Mixer.Info = outComboBox.value
                try {
                    audioCapture.start(selectedMicrophone, selectedOutput)
                } catch (e: LineUnavailableException) {
                    e.printStackTrace()
                }
            } else {
                statusText.text = "Please select an input / output"
            }
        } else {
            audioCapture.stop()
            statusText.text = "Stopped playing back your mic"
            playbackButton.text = "Start playback"
        }
    }
}