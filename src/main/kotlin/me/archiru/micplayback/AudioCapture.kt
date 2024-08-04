package me.archiru.micplayback

import javax.sound.sampled.*


class AudioCapture {
    private var microphone: TargetDataLine? = null
    private var speakers: SourceDataLine? = null

    val availableMicrophones: List<Mixer.Info>
        get() {
            val microphones: MutableList<Mixer.Info> = ArrayList()
            val mixers = AudioSystem.getMixerInfo()
            for (mixerInfo in mixers) {
                val mixer = AudioSystem.getMixer(mixerInfo)
                if (mixer.isLineSupported(
                        DataLine.Info(
                            TargetDataLine::class.java,
                            audioFormat
                        )
                    )
                ) {
                    microphones.add(mixerInfo)
                }
            }
            return microphones
        }

    val availableOutputs: List<Mixer.Info>
        get() {
            val outputs: MutableList<Mixer.Info> = ArrayList()
            val mixers = AudioSystem.getMixerInfo()
            for (mixerInfo in mixers) {
                val mixer = AudioSystem.getMixer(mixerInfo)
                if (mixer.isLineSupported(
                        DataLine.Info(
                            SourceDataLine::class.java,
                            audioFormat
                        )
                    )
                ) {
                    outputs.add(mixerInfo)
                }
            }
            return outputs
        }

    @Throws(LineUnavailableException::class)
    fun start(selectedMicrophone: Mixer.Info?, selectedOutput: Mixer.Info?) {
        val format = audioFormat
        val targetInfo = DataLine.Info(TargetDataLine::class.java, format)
        val sourceInfo = DataLine.Info(SourceDataLine::class.java, format)
        val microphoneMixer = AudioSystem.getMixer(selectedMicrophone)
        val outputMixer = AudioSystem.getMixer(selectedOutput)
        microphone = microphoneMixer.getLine(targetInfo) as TargetDataLine
        speakers = outputMixer.getLine(sourceInfo) as SourceDataLine
        microphone!!.open(format)
        speakers!!.open(format)
        microphone!!.start()
        speakers!!.start()
        val thread = Thread {
            val buffer = ByteArray(1024)
            var bytesRead: Int
            while (!Thread.interrupted()) {
                bytesRead = microphone!!.read(buffer, 0, buffer.size)
                if (bytesRead > 0) {
                    speakers!!.write(buffer, 0, bytesRead)
                }
            }
        }
        thread.start()
    }

    fun stop() {
        if (microphone != null) {
            microphone!!.stop()
            microphone!!.close()
        }
        if (speakers != null) {
            speakers!!.stop()
            speakers!!.close()
        }
    }

    private val audioFormat: AudioFormat = AudioFormat(44100F, 16, 2, true, true);
}
